package ru.dgritsenko.bam.userinterface;

import ru.dgritsenko.bam.bank.Account;
import ru.dgritsenko.bam.bank.OperationStatus;
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
                \tБанковское приложение / Главное меню
                ====================================================================================================
                """;
        System.out.println(mainPageHeader);

        String pageMenu = """
                \t1. Счета
                \t2. Транзакции
                \t3. Выход
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
                \tБанковское приложение / Меню счетов
                ====================================================================================================
                """;
        System.out.println(mainPageHeader);

        String pageMenu = """
                \t1. Создать
                \t2. Список
                \t3. Главное меню
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
                \tБанковское приложение / Создание счета
                ====================================================================================================
                """;
        System.out.print(mainPageHeader);

        String pageActionMessage = "\n> Введите ФИО владельца нового счета: ";
        System.out.print(pageActionMessage);

        Scanner holderNameScanner = new Scanner(System.in);
        String holderName = holderNameScanner.nextLine();

        Account account = new Account(holderName);
        accounts.add(account);

        String pageMenu = MessageFormat.format("""
                
                \tСоздан новый счет: {0}
                
                \t1. Создать еще
                \t2. Операции
                \t3. Список счетов
                \t4. Меню счетов
                \t5. Главное меню
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
            showAccountListPage();
        } else if (option == 5) {
            showMainPage();
        } else {
            // обработка ошибки ввода
        }
    }

    private void showAccountListPage() {
        clearText();

        String mainPageHeader = """
                ====================================================================================================
                \tБанковское приложение / Список счетов
                ====================================================================================================
                """;
        System.out.println(mainPageHeader);

        StringBuilder pageMenu = new StringBuilder();

        int i = 1;

        for (Account account : accounts) {
            String accountOptionTemplate = (i == 1) ? "\t{0}. {1}" : "\n\t{0}. {1}";
            String accountOption = MessageFormat.format(accountOptionTemplate, i, account);
            pageMenu.append(accountOption);
            i++;
        }

        if (pageMenu.isEmpty()) {
            String missingMessage = "\tСписок счетов пуст...";
            System.out.println(missingMessage);

            String pageActionMessage = "\n> Нажмите Enter для возврата в меню счетов...";
            System.out.print(pageActionMessage);

            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();

            showAccountPage();
        } else {
            String goToMainMenuOption = MessageFormat.format("\n\n\t{0}. Главное меню", i);
            pageMenu.append(goToMainMenuOption);

            String pageText = pageMenu.toString();
            System.out.println(pageText);

            String pageActionMessage = "\n> Введите номер пункта: ";
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
                \tБанковское приложение / Операции со счетом: {0}, баланс: {1}
                ====================================================================================================
                """,
                account, account.getBalance());
        System.out.println(mainPageHeader);

        String pageMenu = """
                \t1. Пополнить
                \t2. Оплатить
                \t3. Перевести
                \t4. Снять наличные
                \t5. Список счетов
                \t6. Меню счетов
                \t7. Главное меню
                """;
        System.out.println(pageMenu);

        String pageActionMessage = "> Введите номер пункта: ";
        System.out.print(pageActionMessage);

        Scanner optionScanner = new Scanner(System.in);
        int option = optionScanner.nextInt();

        if (option == 1) {
            pageActionMessage = "\n> Введите сумму пополнения: ";
            System.out.print(pageActionMessage);

            Scanner amountScanner = new Scanner(System.in);
            double amount = amountScanner.nextInt();

            OperationStatus result = TransactionProcessor.deposit(account, amount);

            String resultMessage = MessageFormat.format("\nСтатус транзакции пополнения: {0}", result);
            System.out.println(resultMessage);

            pageActionMessage = "\n> Нажмите Enter для продолжения...";
            System.out.print(pageActionMessage);

            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();

            showAccountOperationPage(account);
        } else if (option == 2) {
            StringBuilder beneficiaryMenu = new StringBuilder();

            int i = 1;

            for (Account beneficiary : accounts) {
                if (!(account == beneficiary)) {
                    String accountOptionTemplate = (i == 1) ? "\n\t{0}. {1}" : "\n\t{0}. {1}";
                    String accountOption = MessageFormat.format(accountOptionTemplate, i, beneficiary);
                    beneficiaryMenu.append(accountOption);
                    i++;
                }
            }

            if (beneficiaryMenu.isEmpty()) {
                String missingMessage = "\n\tСписок получателей пуст...";
                System.out.println(missingMessage);

                pageActionMessage = "\n> Нажмите Enter для продолжения...";
                System.out.print(pageActionMessage);

                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();

                showAccountOperationPage(account);
            } else {
                String canselationOption = MessageFormat.format("\n\n\t{0}. Отмена", i);
                beneficiaryMenu.append(canselationOption);

                String pageText = beneficiaryMenu.toString();
                System.out.println(pageText);

                pageActionMessage = "\n> Введите номер получателя: ";
                System.out.print(pageActionMessage);

                Scanner scanner = new Scanner(System.in);
                option = scanner.nextInt();

                int optionsAmount = accounts.size();

                if (option > 0 && option < optionsAmount) {
                    Account beneficiary = accounts.get(option - 1);

                    pageActionMessage = "\n> Введите сумму оплаты: ";
                    System.out.print(pageActionMessage);

                    Scanner amountScanner = new Scanner(System.in);
                    double amount = amountScanner.nextInt();

                    OperationStatus result = TransactionProcessor.debit(account, amount, beneficiary);

                    String resultMessage = MessageFormat.format("\nСтатус транзакции оплаты: {0}", result);
                    System.out.println(resultMessage);

                    pageActionMessage = "\n> Нажмите Enter для продолжения...";
                    System.out.print(pageActionMessage);

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
                    String accountOptionTemplate = (i == 1) ? "\n\t{0}. {1}" : "\n\t{0}. {1}";
                    String accountOption = MessageFormat.format(accountOptionTemplate, i, beneficiary);
                    beneficiaryMenu.append(accountOption);
                    i++;
                }
            }

            if (beneficiaryMenu.isEmpty()) {
                String missingMessage = "\n\tСписок получателей пуст...";
                System.out.println(missingMessage);

                pageActionMessage = "\n> Нажмите Enter для продолжения...";
                System.out.print(pageActionMessage);

                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();

                showAccountOperationPage(account);
            } else {
                String canselationOption = MessageFormat.format("\n\n\t{0}. Отмена", i);
                beneficiaryMenu.append(canselationOption);

                String pageText = beneficiaryMenu.toString();
                System.out.println(pageText);

                pageActionMessage = "\n> Введите номер получателя: ";
                System.out.print(pageActionMessage);

                Scanner scanner = new Scanner(System.in);
                option = scanner.nextInt();

                int optionsAmount = accounts.size();

                if (option > 0 && option < optionsAmount) {
                    Account beneficiary = accounts.get(option - 1); // Если пропущен текущий счет, индекс сдвигается, поправить!

                    pageActionMessage = "\n> Введите сумму перевода: ";
                    System.out.print(pageActionMessage);

                    Scanner amountScanner = new Scanner(System.in);
                    double amount = amountScanner.nextInt();

                    OperationStatus result = TransactionProcessor.debit(account, amount, beneficiary);

                    String resultMessage = MessageFormat.format("\nСтатус транзакции переводы: {0}", result);
                    System.out.println(resultMessage);

                    pageActionMessage = "\n> Нажмите Enter для продолжения...";
                    System.out.print(pageActionMessage);

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
            pageActionMessage = "\n> Введите сумму наличных: ";
            System.out.print(pageActionMessage);

            Scanner amountScanner = new Scanner(System.in);
            double amount = amountScanner.nextInt();

            OperationStatus result = TransactionProcessor.withdrawal(account, amount);

            String resultMessage = MessageFormat.format("\nСтатус транзакции снятия наличных: {0}", result);
            System.out.println(resultMessage);

            pageActionMessage = "\n> Нажмите Enter для продолжения...";
            System.out.print(pageActionMessage);

            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();

            showAccountOperationPage(account);
        } else if (option == 5) {
            showAccountListPage();
        } else if (option == 6) {
            showAccountPage();
        } else if (option == 7) {
            showMainPage();
        } else {
            // обработка ошибки ввода
        }
    }

    private void showTransactionPage() {
        clearText();

        String mainPageHeader = """
                ====================================================================================================
                \tБанковское приложение / Транзакции
                ====================================================================================================
                """;
        System.out.println(mainPageHeader);

        StringBuilder accountsData = new StringBuilder();

        if (accounts.isEmpty()) {
            String message = "\tСписок транзакций пуст...\n";
            System.out.println(message);
        } else {
            for (Account account : accounts) {
                account.printTransactions();
                System.out.println();
            }
        }

        String pageActionMessage = "> Нажмите Enter для возврата в главное меню...";
        System.out.print(pageActionMessage);

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