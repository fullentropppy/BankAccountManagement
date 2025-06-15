package ru.dgritsenko.bam.userinterface;

import ru.dgritsenko.bam.bank.Account;
import ru.dgritsenko.bam.bank.OperationType;
import ru.dgritsenko.bam.bank.TransactionProcessor;
import ru.dgritsenko.bam.test.Test;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleProcessor {
    private final ArrayList<Account> accounts = new ArrayList<>();
    private Page currentPage;
    private Account holder;
    private Account beneficiary;
    private OperationType holderOperation;

    public void initialize() {
        showPage(Page.MAIN);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. PAGES PROCESSING
    // -----------------------------------------------------------------------------------------------------------------

    private void processMainPage() {
        String pageMessage =
                """
                Основное:
                1 - Счета
                2 - Транзакции
                3 - Выйти
                Дополнительно:
                4 - Тестирование
                """;
        System.out.println(pageMessage);

        Page nextPage = switch (getOption(ChoosingOption.DEFAULT)) {
            case 1 -> Page.ACCOUNTS;
            case 2 -> Page.TRANSACTIONS;
            case 3 -> Page.EXIT;
            case 4 -> Page.TEST;
            default -> Page.MAIN;
        };

        showPage(nextPage);
    }

    private void processExitPage() {
        System.exit(0);
    }

    private void processTestPage() {
        Test.processPredefinedSet();

        // Ожидание ввода перед закрытием
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. PAGES PROCESSING. ACCOUNTS
    // -----------------------------------------------------------------------------------------------------------------

    private void processAccountsPage() {
        String pageMessage =
                """
                1 - Создание счета
                2 - Список счетов
                3 - Главное меню
                """;
        System.out.println(pageMessage);

        Page nextPage = switch (getOption(ChoosingOption.DEFAULT)) {
            case 1 -> Page.ACCOUNTS_CREATING;
            case 2 -> Page.ACCOUNTS_LIST;
            default -> Page.MAIN;
        };

        showPage(nextPage);
    }

    private void processAccountCreatingPage() {
        String actionMessage = "> введите ФИО владельца: ";
        System.out.print(actionMessage);

        Scanner holderNameScanner = new Scanner(System.in);
        String holderName = holderNameScanner.nextLine();

        holder = new Account(holderName);
        accounts.add(holder);

        clearText();

        String pageMessage = MessageFormat.format(
                """
                Создан счет на имя {0}
                
                1 - Операции со счетом
                2 - Создать новый счет
                3 - Список счетов
                4 - Главное меню
                """,
                holder
        );
        System.out.println(pageMessage);

        Page nextPage = switch (getOption(ChoosingOption.DEFAULT)) {
            case 1 -> Page.ACCOUNTS_OPERATIONS;
            case 2 -> Page.ACCOUNTS_CREATING;
            case 3 -> Page.ACCOUNTS_LIST;
            default -> Page.MAIN;
        };

        showPage(nextPage);
    }

    private void processAccountOperationsPage() {
        String pageMessage = MessageFormat.format(
                """
                Счет {0}, баланс: {1}
                1 - Пополнить
                2 - Оплатить
                3 - Снять наличные
                4 - Перевести
                5 - Список транзакций
                6 - Главное меню
                """,
                holder, holder.getBalance()
        );
        System.out.println(pageMessage);

        int option = getOption(ChoosingOption.DEFAULT);

        Page nextPage = Page.MAIN;
        holderOperation = null;

        if (option == 1) {
            nextPage = Page.ACCOUNTS_OPERATIONS_SELECTED;
            holderOperation = OperationType.DEPOSIT;
        } else if (option == 2) {
            nextPage = Page.ACCOUNTS_OPERATIONS_SELECTED;
            holderOperation = OperationType.DEBIT;
        } else if (option == 3) {
            nextPage = Page.ACCOUNTS_OPERATIONS_SELECTED;
            holderOperation = OperationType.WITHDRAW;
        } else if (option == 4) {
            nextPage = Page.ACCOUNTS_OPERATIONS_SELECTED;
            holderOperation = OperationType.TRANSFER;
        } else if (option == 5) {
            nextPage = Page.TRANSACTIONS_LIST;
        }

        showPage(nextPage);
    }

    private void processAccountOperationsSelectedPage() {
        String pageMessage = MessageFormat.format("Счет {0}, баланс: {1}", holder, holder.getBalance());
        System.out.println(pageMessage);

        String actionMessage = "> введите сумму операции: ";
        System.out.print(actionMessage);

        Scanner scannerAmmount = new Scanner(System.in);
        Double amount = scannerAmmount.nextDouble();

        if (holderOperation == OperationType.DEBIT || holderOperation == OperationType.TRANSFER) {
            Account beneficiary = getAccountFromPrintedList();
        }

        if (holderOperation == OperationType.DEPOSIT) {
            TransactionProcessor.deposit(holder, amount);
        } else if (holderOperation == OperationType.DEBIT) {
            TransactionProcessor.debit(holder, amount, beneficiary);
        } else if (holderOperation == OperationType.WITHDRAW) {
            TransactionProcessor.withdrawal(holder, amount);
        } else if (holderOperation == OperationType.TRANSFER) {
            TransactionProcessor.transfer(holder, amount, beneficiary);
        }

        showPage(Page.ACCOUNTS_OPERATIONS);
    }

    private void processAccountsListPage() {
        holder = getAccountFromPrintedList();

        if (holder != null) {
            showPage(Page.ACCOUNTS_OPERATIONS);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. PAGES PROCESSING. TRANSACTIONS
    // -----------------------------------------------------------------------------------------------------------------

    private void processTransactionsPage() {
        String pageMessage =
                """
                1 - Список
                2 - Главное меню
                """;
        System.out.println(pageMessage);

        Page nextPage = switch (getOption(ChoosingOption.DEFAULT)) {
            case 1 -> Page.TRANSACTIONS_LIST;
            default -> Page.MAIN;
        };

        showPage(nextPage);
    }

    private void processTransactionsListPage() {

    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. PAGES PROCESSING. MISC
    // -----------------------------------------------------------------------------------------------------------------

    private void showPage(Page page) {
        currentPage = page;

        clearText();
        printHeader();

        // Вывод конкретной страницы
        if (page == Page.MAIN) {
            processMainPage();
        } else if (page == Page.ACCOUNTS) {
            processAccountsPage();
        } else if (page == Page.ACCOUNTS_CREATING) {
            processAccountCreatingPage();
        } else if (page == Page.ACCOUNTS_OPERATIONS) {
            processAccountOperationsPage();
        } else if (page == Page.ACCOUNTS_OPERATIONS_SELECTED) {
            processAccountOperationsSelectedPage();
        } else if (page == Page.ACCOUNTS_LIST) {
            processAccountsListPage();
        } else if (page == Page.TRANSACTIONS) {
            processTransactionsPage();
        } else if (page == Page.TRANSACTIONS_LIST) {
            processTransactionsListPage();
        } else if (page == Page.TEST) {
            processTestPage();
        } else if (page == Page.EXIT) {
            processExitPage();
        }
    }

    private void printHeader() {
        String header;

        if (currentPage == Page.ACCOUNTS_OPERATIONS_SELECTED) {
            header = MessageFormat.format("*** Банковское приложение / {0}: {1} ***\n", currentPage, holderOperation);
        } else {
            header = MessageFormat.format("*** Банковское приложение / {0} ***\n", currentPage);
        }
        System.out.println(header);
    }

    private void clearText() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException _) {}
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. MISC
    // -----------------------------------------------------------------------------------------------------------------

    private int getOption(ChoosingOption choosingOption) {
        System.out.print(choosingOption);

        Scanner scanner = new Scanner(System.in);

        return scanner.nextInt();
    }

    private void printAccountList(boolean addGoMainMenu) {
        if (accounts.isEmpty()) {
            return;
        }

        StringBuilder accountsListMessages = new StringBuilder();

        int i = 1;

        for (Account account : accounts) {
            String accountOption = MessageFormat.format("{0} - {1}\n", i, account);
            accountsListMessages.append(accountOption);
            i++;
        }

        if (addGoMainMenu) {
            String mainMenuOption = MessageFormat.format("{0} - Главное меню\n", i);
            accountsListMessages.append(mainMenuOption);
        }

        String accountsListMessage = accountsListMessages.toString();
        System.out.println(accountsListMessage);
    }

    private Account getAccountFromPrintedList() {
        Account chosenAccount = null;

        if (accounts.isEmpty()) {
            showPage(Page.ACCOUNTS);
            return chosenAccount;
        }

        printAccountList(true);

        int option = getOption(ChoosingOption.ACCOUNT);

        int optionsAmount = accounts.size() + 1;

        if (option == optionsAmount) {
            
        } else if (option <= accounts.size()) {
            chosenAccount = accounts.get(option - 1);
        }

        return chosenAccount;
    }
}