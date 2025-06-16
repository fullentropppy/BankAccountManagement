package ru.dgritsenko.bam.userinterfacenew;

import ru.dgritsenko.bam.bank.Account;
import ru.dgritsenko.bam.bank.OperationStatus;
import ru.dgritsenko.bam.bank.Transaction;
import ru.dgritsenko.bam.bank.TransactionProcessor;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleProcessor {
    private final ArrayList<Account> accounts = new ArrayList<>();

    public void initialize() {
        showMainPage();
    }

    private void showMainPage() {
        clearText();

        String mainPageHeader = """
                ====================================================================================================
                    Банковское приложение / Главная
                ====================================================================================================
                """;
        System.out.println(mainPageHeader);

        String pageMenu = """
                    1. Счета
                    2. Транзакции
                    3. Выход
                """;
        System.out.println(pageMenu);

        String pageActionMessage = "> Введите номер пункта: ";
        System.out.print(pageActionMessage);

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();

        if (option == 1) {
            showAccountPage();
        } else if (option == 2) {
            showTransactionPage();
        } else if (option == 3) {
            System.exit(0);
        } else {
            // обработка ошибки ввода
        }
    }

    private void showAccountPage() {
        clearText();

        String mainPageHeader = """
                ====================================================================================================
                    Банковское приложение / Счета
                ====================================================================================================
                """;
        System.out.println(mainPageHeader);

        String pageMenu = """
                    1. Создать
                    2. Список
                    3. Главная
                """;
        System.out.println(pageMenu);

        String pageActionMessage = "> Введите номер пункта: ";
        System.out.print(pageActionMessage);

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();

        if (option == 1) {
            showAccountCreatingPage();
        } else if (option == 2) {
            showAccountListPage();
        } else if (option == 3) {
            showMainPage();
        } else {
            // обработка ошибки ввода
        }
    }

    private void showAccountCreatingPage() {
        clearText();

        String mainPageHeader = """
                ====================================================================================================
                    Банковское приложение / Создание счета
                ====================================================================================================
                """;
        System.out.print(mainPageHeader);

        String pageActionMessage = "> Введите ФИО владельца нового счета: ";
        System.out.print(pageActionMessage);

        Scanner holderNameScanner = new Scanner(System.in);
        String holderName = holderNameScanner.nextLine();

        Account account = new Account(holderName);
        accounts.add(account);

        String pageMenu = MessageFormat.format("""
                    Создан новый счет: {0}
                    1. Создать еще
                    2. Операции
                    3. Список
                    4. Главная
                """,
                account);

        System.out.println(pageMenu);

        pageActionMessage = "> Введите номер пункта: ";
        System.out.print(pageActionMessage);

        Scanner optionScanner = new Scanner(System.in);
        int option = optionScanner.nextInt();

        if (option == 1) {
            showAccountCreatingPage();
        } else if (option == 2) {
            showAccountOperationPage(account);
        } else if (option == 3) {
            showAccountListPage();
        } else if (option == 4) {
            showMainPage();
        } else {
            // обработка ошибки ввода
        }
    }

    private void showAccountListPage() {
        clearText();

        String mainPageHeader = """
                ====================================================================================================
                    Банковское приложение / Счета
                ====================================================================================================
                """;
        System.out.println(mainPageHeader);

        StringBuilder pageMenu = new StringBuilder();

        int i = 1;

        for (Account account : accounts) {
            String accountOption = MessageFormat.format("   {0}. {1}\n", i, account);
            pageMenu.append(accountOption);
            i++;
        }

        if (pageMenu.isEmpty()) {
            String missingMessage = "Список счетов пуст...";
            System.out.println(missingMessage);

            String pageActionMessage = "> Нажмите Enter для возврата на главную...";
            System.out.println(pageActionMessage);

            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();

            showMainPage();
        } else {
            String goToMainMenuOption = MessageFormat.format("   {0}. Главная\n", i);
            pageMenu.append(goToMainMenuOption);

            String pageText = pageMenu.toString();
            System.out.println(pageText);

            String pageActionMessage = "> Введите номер пункта: ";
            System.out.print(pageActionMessage);

            Scanner scanner = new Scanner(System.in);
            int option = scanner.nextInt();

            int optionsAmount = accounts.size() + 1;

            if (option > 0 && option < optionsAmount) {
                Account account = accounts.get(option - 1);
                showAccountOperationPage(account);
            } else if (option == optionsAmount) {
                showMainPage();
            } else {
                // обработка ошибки ввода
            }
        }
    }

    private void showAccountOperationPage(Account account) {
        clearText();

        String mainPageHeader = MessageFormat.format("""
                ====================================================================================================
                    Банковское приложение / Операции со счетом: {0}, баланс: {1}
                ====================================================================================================
                """,
                account, account.getBalance());
        System.out.print(mainPageHeader);

        String pageMenu = """
                    1. Пополнить
                    2. Оплатить
                    3. Перевести
                    4. Снять наличные
                    5. Главная
                """;
        System.out.println(pageMenu);

        String pageActionMessage = "> Введите номер пункта: ";
        System.out.print(pageActionMessage);

        Scanner optionScanner = new Scanner(System.in);
        int option = optionScanner.nextInt();

        if (option == 1) {
            pageActionMessage = "> Введите сумму пополнения: ";
            System.out.print(pageActionMessage);

            Scanner amountScanner = new Scanner(System.in);
            double amount = amountScanner.nextInt();

            OperationStatus result = TransactionProcessor.deposit(account, amount);

            String resultMessage = MessageFormat.format("Статус транзакции пополнения: {0}", result);
            System.out.println(resultMessage);

            pageActionMessage = "> Нажмите Enter для продолжения...";
            System.out.println(pageActionMessage);

            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();

            showAccountOperationPage(account);
        } else if (option == 2) {
            StringBuilder beneficiaryMenu = new StringBuilder();

            int i = 1;

            for (Account beneficiary : accounts) {
                if (!(account == beneficiary)) {
                    String accountOption = MessageFormat.format("   {0}. {1}\n", i, beneficiary);
                    beneficiaryMenu.append(accountOption);
                    i++;
                }
            }

            if (beneficiaryMenu.isEmpty()) {
                String missingMessage = "Список получателей пуст...";
                System.out.println(missingMessage);

                pageActionMessage = "> Нажмите Enter для продолжения...";
                System.out.println(pageActionMessage);

                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();

                showAccountOperationPage(account);
            } else {
                String canselationOption = MessageFormat.format("   {0}. Отмена\n", i);
                beneficiaryMenu.append(canselationOption);

                String pageText = beneficiaryMenu.toString();
                System.out.println(pageText);

                pageActionMessage = "> Введите номер получателя: ";
                System.out.print(pageActionMessage);

                Scanner scanner = new Scanner(System.in);
                option = scanner.nextInt();

                int optionsAmount = accounts.size();

                if (option > 0 && option < optionsAmount) {
                    Account beneficiary = accounts.get(option - 1);

                    pageActionMessage = "> Введите сумму оплаты: ";
                    System.out.print(pageActionMessage);

                    Scanner amountScanner = new Scanner(System.in);
                    double amount = amountScanner.nextInt();

                    OperationStatus result = TransactionProcessor.debit(account, amount, beneficiary);

                    String resultMessage = MessageFormat.format("Статус транзакции оплаты: {0}", result);
                    System.out.println(resultMessage);

                    pageActionMessage = "> Нажмите Enter для продолжения...";
                    System.out.println(pageActionMessage);

                    Scanner continuationScanner = new Scanner(System.in);
                    continuationScanner.nextLine();

                    showAccountOperationPage(account);
                } else if (option == optionsAmount) {
                    showAccountOperationPage(account);
                } else {
                    // обработка ошибки ввода
                }
            }
        } else if (option == 3) {
            StringBuilder beneficiaryMenu = new StringBuilder();

            int i = 1;

            for (Account beneficiary : accounts) {
                if (!(account == beneficiary)) {
                    String accountOption = MessageFormat.format("   {0}. {1}\n", i, beneficiary);
                    beneficiaryMenu.append(accountOption);
                    i++;
                }
            }

            if (beneficiaryMenu.isEmpty()) {
                String missingMessage = "Список получателей пуст...";
                System.out.println(missingMessage);

                pageActionMessage = "> Нажмите Enter для продолжения...";
                System.out.println(pageActionMessage);

                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();

                showAccountOperationPage(account);
            } else {
                String canselationOption = MessageFormat.format("   {0}. Отмена\n", i);
                beneficiaryMenu.append(canselationOption);

                String pageText = beneficiaryMenu.toString();
                System.out.println(pageText);

                pageActionMessage = "> Введите номер получателя: ";
                System.out.print(pageActionMessage);

                Scanner scanner = new Scanner(System.in);
                option = scanner.nextInt();

                int optionsAmount = accounts.size();

                if (option > 0 && option < optionsAmount) {
                    Account beneficiary = accounts.get(option - 1);

                    pageActionMessage = "> Введите сумму перевода: ";
                    System.out.print(pageActionMessage);

                    Scanner amountScanner = new Scanner(System.in);
                    double amount = amountScanner.nextInt();

                    OperationStatus result = TransactionProcessor.debit(account, amount, beneficiary);

                    String resultMessage = MessageFormat.format("Статус транзакции переводы: {0}", result);
                    System.out.println(resultMessage);

                    pageActionMessage = "> Нажмите Enter для продолжения...";
                    System.out.println(pageActionMessage);

                    Scanner continuationScanner = new Scanner(System.in);
                    continuationScanner.nextLine();

                    showAccountOperationPage(account);
                } else if (option == optionsAmount) {
                    showAccountOperationPage(account);
                } else {
                    // обработка ошибки ввода
                }
            }
        } else if (option == 4) {
            pageActionMessage = "> Введите сумму наличных: ";
            System.out.print(pageActionMessage);

            Scanner amountScanner = new Scanner(System.in);
            double amount = amountScanner.nextInt();

            OperationStatus result = TransactionProcessor.withdrawal(account, amount);

            String resultMessage = MessageFormat.format("Статус транзакции снятия наличных: {0}", result);
            System.out.println(resultMessage);

            pageActionMessage = "> Нажмите Enter для продолжения...";
            System.out.println(pageActionMessage);

            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();

            showAccountOperationPage(account);
        } else if (option == 5) {
            showMainPage();
        } else {
            // обработка ошибки ввода
        }
    }

    private void showTransactionPage() {
        clearText();

        String mainPageHeader = """
                ====================================================================================================
                    Банковское приложение / Транзакции
                ====================================================================================================
                """;
        System.out.println(mainPageHeader);

        StringBuilder accountsData = new StringBuilder();

        for (Account account : accounts) {
            ArrayList<Transaction> accountTransactions = account.getTransactions();

            if (!accountTransactions.isEmpty()) {
                String accountHeader = MessageFormat.format("Счет: {0}:\n", account);
                accountsData.append(accountHeader);

                int i = 1;

                for (Transaction transaction : accountTransactions) {
                    String transactionView = MessageFormat.format(
                            "   {0} - {1}, {2}: {3}\n",
                            i, transaction, transaction.getOperationType(), transaction.getAmount());
                    accountsData.append(transactionView);
                    i++;
                }
            }
        }

        if (accountsData.isEmpty()) {
            String missingMessage = "Список транзакций пуст...\n";
            accountsData.append(missingMessage);
        }

        String pageText = accountsData.toString();
        System.out.println(pageText);

        String pageActionMessage = "> Нажмите Enter для возврата на главную...";
        System.out.println(pageActionMessage);

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        showMainPage();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. MISC
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