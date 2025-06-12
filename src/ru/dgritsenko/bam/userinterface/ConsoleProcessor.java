package ru.dgritsenko.bam.userinterface;

import ru.dgritsenko.bam.bank.Account;
import ru.dgritsenko.bam.test.Test;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleProcessor {
    private final ArrayList<Account> accounts = new ArrayList<>();
    private Page currentPage;

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

        String actionMessage = "> введите номер действия: ";
        System.out.print(actionMessage);

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();

        Page nextPage = switch (option) {
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
        clearText();
        printHeader();

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
                1 - Создание
                2 - Операции
                3 - Список
                4 - Главное меню
                """;
        System.out.println(pageMessage);

        String actionMessage = "> введите номер действия: ";
        System.out.print(actionMessage);

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();

        Page nextPage = switch (option) {
            case 1 -> Page.ACCOUNTS_CREATING;
            case 2 -> Page.ACCOUNTS_OPERATION;
            case 3 -> Page.ACCOUNTS_LIST;
            default -> Page.MAIN;
        };

        showPage(nextPage);
    }

    private void processAccountCreatingPage() {

    }

    private void processAccountOperationPage() {

    }

    private void processAccountsListPage() {

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

        String actionMessage = "> введите номер действия: ";
        System.out.print(actionMessage);

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();

        Page nextPage = switch (option) {
            case 1 -> Page.TRANSACTIONS_LIST;
            default -> Page.MAIN;
        };

        showPage(nextPage);
    }

    private void processTransactionsListPage() {

    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. MISC
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
        } else if (page == Page.ACCOUNTS_OPERATION) {
            processAccountOperationPage();
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
        String header = MessageFormat.format("*** Банковское приложение / {0} ***\n", currentPage);
        System.out.println(header);
    }

    private void clearText() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
                }
        } catch (IOException | InterruptedException ex) {}
    }
}