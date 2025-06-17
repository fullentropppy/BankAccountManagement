package ru.dgritsenko.bam.userinterface;

import ru.dgritsenko.bam.bank.Account;
import ru.dgritsenko.bam.bank.TransactionStatus;
import ru.dgritsenko.bam.bank.TransactionProcessor;
import ru.dgritsenko.bam.bank.TransactionType;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleProcessor {
    private final ArrayList<Account> accounts = new ArrayList<>();
    private Account currentFromAccount;

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. INITIALIZATION
    // -----------------------------------------------------------------------------------------------------------------

    public void initialize() {
        showMainPage();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. PAGE. MAIN
    // -----------------------------------------------------------------------------------------------------------------

    private void showMainPage() {
        printNewPageHeader("Главное меню");

        String pageMenu = """
                \t1. Счета
                \t2. Транзакции
                \t3. Выход
                """;
        System.out.println(pageMenu);

        int option = getOptionFromMenu(pageMenu, "Введите номер пункта");

        if (option == 1) {
            showAccountPage();
        } else if (option == 2) {
            showTransactionPage();
        } else if (option == 3) {
            System.exit(0);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. PAGE. ACCOUNT. SHOWING
    // -----------------------------------------------------------------------------------------------------------------

    private void showAccountPage() {
        printNewPageHeader("Меню счетов");

        String pageMenu = """
                \t1. Создать
                \t2. Список
                \t3. Главное меню
                """;
        System.out.println(pageMenu);

        int option = getOptionFromMenu(pageMenu, "Введите номер пункта");

        if (option == 1) {
            showAccountCreatingPage();
        } else if (option == 2) {
            showAccountListPage();
        } else if (option == 3) {
            showMainPage();
        }
    }

    private void showAccountCreatingPage() {
        printNewPageHeader("Создание счета");

        String actionMessage = "\n> Введите ФИО владельца нового счета: ";
        System.out.print(actionMessage);

        Scanner fromAccountNameScanner = new Scanner(System.in);
        String fromAccountName = fromAccountNameScanner.nextLine();

        Account account = new Account(fromAccountName);
        accounts.add(account);

        String pageMenu = MessageFormat.format("""
                
                \tСоздан новый счет: {0}
                
                \t1. Создать еще
                \t2. Операции
                \t3. Список счетов
                \t4. Главное меню
                """,
                account);
        System.out.println(pageMenu);

        int option = getOptionFromMenu(pageMenu, "Введите номер пункта");

        if (option == 1) {
            showAccountCreatingPage();
        } else if (option == 2) {
            showAccountOperationPage();
        } else if (option == 3) {
            showAccountListPage();
        } else if (option == 4) {
            showMainPage();
        }
    }

    private void showAccountListPage() {
        printNewPageHeader("Список счетов");

        StringBuilder pageMenuOptions = new StringBuilder();

        int i = 1;

        for (Account account : accounts) {
            String accountOptionTemplate = (i == 1) ? "\t{0}. {1}" : "\n\t{0}. {1}";
            String accountOption = MessageFormat.format(accountOptionTemplate, i, account);
            pageMenuOptions.append(accountOption);
            i++;
        }

        if (pageMenuOptions.isEmpty()) {
            String missingMessage = "\tСписок счетов пуст...";
            System.out.println(missingMessage);

            waitForInputToContinue("Нажмите Enter для возврата в меню счетов");

            showAccountPage();
        } else {
            String goToMainMenuOption = MessageFormat.format("\n\n\t{0}. Главное меню", i);
            pageMenuOptions.append(goToMainMenuOption);

            String pageMenu = pageMenuOptions.toString();
            System.out.println(pageMenu);

            int option = getOptionFromMenu(pageMenu, "Введите номер пункта");

            int optionsAmount = accounts.size() + 1;

            if (option > 0 && option < optionsAmount) {
                currentFromAccount = accounts.get(option - 1);
                showAccountOperationPage();
            } else if (option == optionsAmount) {
                showMainPage();
            }
        }
    }

    private void showAccountOperationPage() {
        printNewAccountOperationPage();

        String pageMenu = """
                \t1. Пополнить
                \t2. Оплатить
                \t3. Перевести
                \t4. Снять наличные
                
                \t5. Транзакции счета
                \t6. Список счетов
                \t7. Меню счетов
                \t8. Главное меню
                """;
        System.out.println(pageMenu);

        int option = getOptionFromMenu(pageMenu, "Введите номер пункта");

        if (option == 1) {
            processOperationWithOnlyFromAccount(TransactionType.DEPOSIT);
        } else if (option == 2) {
            processOperationWithToAccount(TransactionType.DEBIT);
        } else if (option == 3) {
            processOperationWithToAccount(TransactionType.TRANSFER);
        } else if (option == 4) {
            processOperationWithOnlyFromAccount(TransactionType.WITHDRAW);
        } else if (option == 5 ) {
            showAccountTransactionPage();
        } else if (option == 6) {
            showAccountListPage();
        } else if (option == 7) {
            showAccountPage();
        } else if (option == 8) {
            showMainPage();
        }
    }

    private void showAccountTransactionPage() {
        printNewAccountOperationPage();

        currentFromAccount.printTransactions();
        System.out.println();

        waitForInputToContinue("Нажмите Enter для возврата в меню операций со счетом");

        showAccountOperationPage();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. PAGE. ACCOUNT. PROCESSING
    // -----------------------------------------------------------------------------------------------------------------

    private void printNewAccountOperationPage() {
        String title = MessageFormat.format(
                "Операции со счетом: {0}, баланс: {1}",
                currentFromAccount, currentFromAccount.getBalance());
        printNewPageHeader(title);
    }

    private void processOperationWithOnlyFromAccount(TransactionType transactionType) {
        printNewAccountOperationPage();

        String inputTitle = "";
        String resultTitleTemplate = "";

        if (transactionType == TransactionType.DEPOSIT) {
            inputTitle = "Введите сумму пополнения";
            resultTitleTemplate = "Статус транзакции пополнения: {0}";
        } else if (transactionType == TransactionType.WITHDRAW){
            inputTitle = "Введите сумму снимаемых наличных";
            resultTitleTemplate = "Статус транзакции снятия наличных: {0}";
        }

        double amount = getAmount(inputTitle);

        TransactionStatus result = null;

        if (transactionType == TransactionType.DEPOSIT) {
           result = TransactionProcessor.deposit(currentFromAccount, amount);
        } else if (transactionType == TransactionType.WITHDRAW){
            result = TransactionProcessor.withdrawal(currentFromAccount, amount);
        }

        String resultMessage = MessageFormat.format(resultTitleTemplate, result);
        System.out.println(resultMessage);

        waitForInputToContinue("Нажмите Enter для продолжения");

        showAccountOperationPage();
    }

    private void processOperationWithToAccount(TransactionType transactionType) {
        printNewAccountOperationPage();

        StringBuilder toAccountMenu = new StringBuilder();

        int i = 1;

        for (Account toAccount : accounts) {
            if (!(currentFromAccount == toAccount)) {
                String accountOptionTemplate = (i == 1) ? "\t{0}. {1}" : "\n\t{0}. {1}";
                String accountOption = MessageFormat.format(accountOptionTemplate, i, toAccount);
                toAccountMenu.append(accountOption);
                i++;
            }
        }

        if (toAccountMenu.isEmpty()) {
            String missingMessage = "\tСписок получателей пуст...";
            System.out.println(missingMessage);

            waitForInputToContinue("Нажмите Enter для продолжения");
        } else {
            String inputOptionTittle = "";
            String inputAmountTittle = "";
            String resultTitleTemplate = "";

            if (transactionType == TransactionType.DEBIT) {
                inputOptionTittle = "Введите номер получателя оплаты";
                inputAmountTittle = "Введите сумму оплаты";
                resultTitleTemplate = "Статус транзакции оплаты: {0}";
            } else if (transactionType == TransactionType.TRANSFER) {
                inputOptionTittle = "Введите номер получателя перевода";
                inputAmountTittle = "Введите сумму перевода";
                resultTitleTemplate = "Статус транзакции перевода: {0}";
            }

            String cancellationOption = MessageFormat.format("\n\n\t{0}. Отмена", i);
            toAccountMenu.append(cancellationOption);

            String pageMenu = toAccountMenu.toString();
            System.out.println(pageMenu);

            int option = getOptionFromMenu(pageMenu, inputOptionTittle);

            int optionsAmount = accounts.size();

            if (option > 0 && option < optionsAmount) {
                Account toAccount = accounts.get(option - 1); // Если пропущен текущий счет, индекс сдвигается, поправить!

                double amount = getAmount(inputAmountTittle);

                TransactionStatus result = null;

                if (transactionType == TransactionType.DEBIT) {
                    result = TransactionProcessor.debit(currentFromAccount, amount, toAccount);
                } else if (transactionType == TransactionType.TRANSFER){
                    result = TransactionProcessor.transfer(currentFromAccount, amount, toAccount);
                }

                String resultMessage = MessageFormat.format(resultTitleTemplate, result);
                System.out.println(resultMessage);

                waitForInputToContinue("Нажмите Enter для продолжения");
            }
        }

        showAccountOperationPage();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. PAGE. TRANSACTION
    // -----------------------------------------------------------------------------------------------------------------

    private void showTransactionPage() {
        printNewPageHeader("Транзакции");

        if (accounts.isEmpty()) {
            String message = "\tСписок транзакций пуст...\n";
            System.out.println(message);
        } else {
            for (Account account : accounts) {
                account.printTransactions();
                System.out.println();
            }
        }

        waitForInputToContinue("Нажмите Enter для возврата в главное меню");

        showMainPage();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. CONSOLE. PRINTING
    // -----------------------------------------------------------------------------------------------------------------

    private void printNewPageHeader(String title) {
        clearText();

        String header = MessageFormat.format(
                """
                ====================================================================================================
                \tБанковское приложение / {0}
                ====================================================================================================
                """,
                title);
        System.out.println(header);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. CONSOLE. PROCESSING
    // -----------------------------------------------------------------------------------------------------------------

    private double getAmount(String title) {
        double amount = 0;

        String actionMessage = MessageFormat.format("> {0}: ", title);
        Scanner scanner = new Scanner(System.in);

        while (amount <= 0) {
            System.out.print(actionMessage);
            amount = scanner.nextDouble();
        }

        return amount;
    }

    private int getOptionFromMenu(String menu, String title) {
        int option = -1;
        List<Integer> validOptions = getValuesFromMenu(menu);

        String actionMessage = MessageFormat.format("> {0}: ", title);
        Scanner scanner = new Scanner(System.in);

        while (!validOptions.contains(option)) {
            System.out.print(actionMessage);
            option = scanner.nextInt();
        }

        return option;
    }

    private List<Integer> getValuesFromMenu(String menu) {
        List<Integer> values = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\d+(?=\\.)");
        Matcher matcher = pattern.matcher(menu);

        // Поиск и вывод всех совпадений
        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group());
            values.add(value);
        }

        return values;
    }

    private void waitForInputToContinue(String title) {
        String pageActionMessage = MessageFormat.format("> {0}...", title);
        System.out.print(pageActionMessage);

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. CONSOLE. MISC
    // -----------------------------------------------------------------------------------------------------------------

    private void clearText() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException _) {}
    }
}