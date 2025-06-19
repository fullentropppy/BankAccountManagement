package ru.dgritsenko.bam.userinterface;

import ru.dgritsenko.bam.bank.*;
import ru.dgritsenko.bam.main.BankService;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс для обработки взаимодействия с пользователем через консоль.
 * Реализует пользовательский интерфейс банковского приложения.
 */
public class ConsoleService {
    private final BankService bankService;
    private Account currentFromAccount;

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Создаёт обработчик консольного ввода с указанным сервисом банка.
     *
     * @param bankService сервис для работы с банковскими операциями
     */
    public ConsoleService(BankService bankService) {
        this.bankService = bankService;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. MAIN
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Запускает главный цикл обработки пользовательского ввода.
     */
    public void run() {
        showMainPage();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. PAGE. MAIN
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Отображает главное меню приложения.
     */
    private void showMainPage() {
        printNewPageHeader("Главное меню");

        String pageMenu = """
           
                \t1. Счета
                \t2. Транзакции
                \t3. Выход""";
        System.out.println(pageMenu);

        int option = getOptionFromMenu(pageMenu, "Введите номер пункта");

        switch (option) {
            case 1 -> showAccountPage();
            case 2 -> showTransactionPage();
            default -> System.exit(0);
        };
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. PAGE. ACCOUNT. SHOWING
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Отображает меню работы со счетами.
     */
    private void showAccountPage() {
        printNewPageHeader("Меню счетов");

        String pageMenu = """
                
                \t1. Создать
                \t2. Список
                \t3. Главное меню""";
        System.out.println(pageMenu);

        int option = getOptionFromMenu(pageMenu, "Введите номер пункта");

        switch (option) {
            case 1 -> showAccountCreatingPage();
            case 2 -> showAccountListPage();
            default -> showMainPage();
        };
    }

    /**
     * Отображает страницу создания нового счета.
     */
    private void showAccountCreatingPage() {
        printNewPageHeader("Создание счета");

        String actionMessage = "\n> Введите ФИО владельца нового счета: ";
        System.out.print(actionMessage);

        Scanner fromAccountHolderNameScanner = new Scanner(System.in);
        String fromAccountHolderName = fromAccountHolderNameScanner.nextLine();

        currentFromAccount = bankService.createAccount(fromAccountHolderName);

        String pageMenu = MessageFormat.format("""
                
                \tСоздан новый счет: {0}
                
                \t1. Создать еще
                \t2. Операции
                \t3. Список счетов
                \t4. Главное меню""",
                currentFromAccount);
        System.out.println(pageMenu);

        int option = getOptionFromMenu(pageMenu, "Введите номер пункта");

        switch (option) {
            case 1 -> showAccountCreatingPage();
            case 2 -> showAccountOperationPage();
            case 3 -> showAccountListPage();
            default -> showMainPage();
        };
    }

    /**
     * Отображает список всех созданных счетов с возможностью выбора.
     */
    private void showAccountListPage() {
        printNewPageHeader("Список счетов");

        StringBuilder pageMenuOptions = new StringBuilder();

        int i = 1;

        for (Account account : bankService.getAccounts()) {
            String accountOption = MessageFormat.format("\n\t{0}. {1}", i, account);
            pageMenuOptions.append(accountOption);
            i++;
        }

        if (pageMenuOptions.isEmpty()) {
            String missingMessage = "\n\tСписок счетов пуст...";
            System.out.println(missingMessage);

            waitForInputToContinue("Нажмите Enter для возврата в меню счетов");

            showAccountPage();
        } else {
            String goToMainMenuOption = MessageFormat.format("\n\n\t{0}. Главное меню", i);
            pageMenuOptions.append(goToMainMenuOption);

            String pageMenu = pageMenuOptions.toString();
            System.out.println(pageMenu);

            int option = getOptionFromMenu(pageMenu, "Введите номер пункта");
            int optionsAmount = bankService.getAccounts().size() + 1;

            if (option < optionsAmount) {
                currentFromAccount = bankService.getAccounts().get(option - 1);
                showAccountOperationPage();
            } else if (option == optionsAmount) {
                showMainPage();
            }
        }
    }

    /**
     * Отображает меню операций со счетом.
     */
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
                \t8. Главное меню""";
        System.out.println(pageMenu);

        int option = getOptionFromMenu(pageMenu, "Введите номер пункта");

        switch (option) {
            case 1 -> processOperationWithOnlyFromAccount(TransactionType.DEPOSIT);
            case 2 -> processOperationWithToAccount(TransactionType.DEBIT);
            case 3 -> processOperationWithToAccount(TransactionType.TRANSFER);
            case 4 -> processOperationWithOnlyFromAccount(TransactionType.WITHDRAW);
            case 5 -> showAccountTransactionPage();
            case 6 -> showAccountListPage();
            case 7 -> showAccountPage();
            default -> showMainPage();
        };
    }

    /**
     * Отображает список всех транзакции текущего выбранного счета.
     */
    private void showAccountTransactionPage() {
        printNewAccountOperationPage();

        System.out.println();
        currentFromAccount.printTransactions();

        waitForInputToContinue("Нажмите Enter для возврата в меню операций со счетом");

        showAccountOperationPage();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. PAGE. ACCOUNT. PROCESSING
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Выводит заголовок страницы операций со счетом, содержащий:
     * - Название страницы
     * - Информацию о текущем счете
     * - Текущий баланс счета
     */
    private void printNewAccountOperationPage() {
        String title = MessageFormat.format(
                "Операции со счетом: {0}, баланс: {1}",
                currentFromAccount, currentFromAccount.getBalance());
        printNewPageHeader(title);
    }

    /**
     * Обрабатывает операции, требующие только счет-источник (пополнение, снятие).
     *
     * @param transactionType тип операции (DEPOSIT или WITHDRAW)
     */
    private void processOperationWithOnlyFromAccount(TransactionType transactionType) {
        String inputTitle = "";
        String resultTitleTemplate = "";

        if (transactionType == TransactionType.DEPOSIT) {
            inputTitle = "Введите сумму пополнения";
            resultTitleTemplate = "\nСтатус транзакции пополнения: {0}";
        } else if (transactionType == TransactionType.WITHDRAW){
            inputTitle = "Введите сумму снимаемых наличных";
            resultTitleTemplate = "\nСтатус транзакции снятия наличных: {0}";
        }

        printNewAccountOperationPage();

        double amount = getAmount(inputTitle);
        TransactionStatus result = bankService.performTransaction(transactionType, currentFromAccount, amount);

        String resultMessage = MessageFormat.format(resultTitleTemplate, result);
        System.out.println(resultMessage);

        waitForInputToContinue("Нажмите Enter для продолжения");
        showAccountOperationPage();
    }

    /**
     * Обрабатывает операции, требующие указания счета-получателя (оплата, перевод).
     *
     * @param transactionType тип операции (DEBIT или TRANSFER)
     */
    private void processOperationWithToAccount(TransactionType transactionType) {
        printNewAccountOperationPage();

        List<Account> availableAccounts = new ArrayList<>();
        StringBuilder toAccountMenu = new StringBuilder();

        int i = 1;

        for (Account toAccount : bankService.getAccounts()) {
            if (!(currentFromAccount == toAccount)) {
                availableAccounts.add(toAccount);
                String accountOption = MessageFormat.format("\n\t{0}. {1}", i, toAccount);
                toAccountMenu.append(accountOption);
                i++;
            }
        }

        if (toAccountMenu.isEmpty()) {
            String missingMessage = "\n\tСписок получателей пуст...";
            System.out.println(missingMessage);

            waitForInputToContinue("Нажмите Enter для продолжения");
        } else {
            String inputOptionTittle = "";
            String inputAmountTittle = "";
            String resultTitleTemplate = "";

            if (transactionType == TransactionType.DEBIT) {
                inputOptionTittle = "Введите номер получателя оплаты";
                inputAmountTittle = "Введите сумму оплаты";
                resultTitleTemplate = "\nСтатус транзакции оплаты: {0}";
            } else if (transactionType == TransactionType.TRANSFER) {
                inputOptionTittle = "Введите номер получателя перевода";
                inputAmountTittle = "Введите сумму перевода";
                resultTitleTemplate = "\nСтатус транзакции перевода: {0}";
            }

            String cancellationOption = MessageFormat.format("\n\n\t{0}. Отмена", i);
            toAccountMenu.append(cancellationOption);

            String pageMenu = toAccountMenu.toString();
            System.out.println(pageMenu);

            int option = getOptionFromMenu(pageMenu, inputOptionTittle);

            if (option < bankService.getAccounts().size()) {
                Account toAccount = availableAccounts.get(option - 1);
                double amount = getAmount(inputAmountTittle);

                TransactionStatus result = bankService.performTransaction(
                        transactionType, currentFromAccount, amount, toAccount);

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

    /**
     * Отображает страницу со списком всех транзакций по всем счетам.
     */
    private void showTransactionPage() {
        printNewPageHeader("Транзакции");

        if (bankService.getAccounts().isEmpty()) {
            String message = "\n\tСписок транзакций пуст...";
            System.out.println(message);
        } else {
            System.out.println();

            for (Account account : bankService.getAccounts()) {
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

    /**
     * Очищает консоль и выводит заголовок страницы с указанным названием.
     *
     * @param title заголовок страницы
     */
    private void printNewPageHeader(String title) {
        clearText();

        String header = MessageFormat.format(
                """
                ====================================================================================================
                \tБанковское приложение / {0}
                ====================================================================================================""",
                title);
        System.out.println(header);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. CONSOLE. PROCESSING
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Запрашивает у пользователя ввод суммы (положительное число) через консоль.
     *
     * @param title подсказка для ввода (например, "Введите сумму")
     * @return введенная пользователем сумма (больше 0)
     */
    private double getAmount(String title) {
        double amount = 0;

        String actionMessage = MessageFormat.format("\n> {0}: ", title);
        Scanner scanner = new Scanner(System.in);

        while (!scanner.hasNextDouble() || amount <= 0) {
            System.out.print(actionMessage);
            amount = scanner.nextDouble();
        }

        return amount;
    }

    /**
     * Запрашивает у пользователя выбор пункта меню и проверяет его корректность.
     *
     * @param menu строка с пунктами меню
     * @param title подсказка для ввода (например, "Введите номер пункта")
     * @return выбранный пользователем пункт меню
     */
    private int getOptionFromMenu(String menu, String title) {
        int option = -1;
        List<Integer> validOptions = getValuesFromMenu(menu);

        String actionMessage = MessageFormat.format("\n> {0}: ", title);
        Scanner scanner = new Scanner(System.in);

        while (!validOptions.contains(option)) {
            System.out.print(actionMessage);
            option = scanner.nextInt();
        }

        return option;
    }

    /**
     * Извлекает номера пунктов меню из строки для валидации ввода пользователя.
     *
     * @param menu строка с пунктами меню
     * @return список доступных номеров пунктов
     */
    private List<Integer> getValuesFromMenu(String menu) {
        List<Integer> values = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\d+(?=\\.)");
        Matcher matcher = pattern.matcher(menu);

        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group());
            values.add(value);
        }

        return values;
    }

    /**
     * Приостанавливает выполнение программы до нажатия Enter пользователем.
     *
     * @param title сообщение, отображаемое перед ожиданием ввода
     */
    private void waitForInputToContinue(String title) {
        String pageActionMessage = MessageFormat.format("\n> {0}...", title);
        System.out.print(pageActionMessage);

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. CONSOLE. MISC
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Очищает консоль.
     */
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