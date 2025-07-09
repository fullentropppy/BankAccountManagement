package ru.dgritsenko.bam.userinterface.console;

import ru.dgritsenko.bam.bank.Account;
import ru.dgritsenko.bam.bank.BankService;
import ru.dgritsenko.bam.userinterface.UserInterface;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Scanner;

/**
 * Класс для обработки взаимодействия с пользователем через консоль.
 * <p>Реализует пользовательский интерфейс банковского приложения.
 */
public class ConsoleUserInterface implements UserInterface {
    private BankService bankService;
    private Account currentFromAccount;

    private ConsolePage mainConsolePage;
    private ConsolePage accountConsolePage;
    private ConsolePage accountCreatingConsolePage;
    private ConsolePage accountListConsolePage;
    private ConsolePage accountOperationConsolePage;
    private ConsolePage accountTransactionConsolePage;
    private ConsolePage transactionConsolePage;

    // -----------------------------------------------------------------------------------------------------------------
    // SETTERS
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }

    public void setCurrentFromAccount(Account currentFromAccount) {
        this.currentFromAccount = currentFromAccount;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // GETTERS
    // -----------------------------------------------------------------------------------------------------------------

    public BankService getBankService() {
        return bankService;
    }

    public Account getCurrentFromAccount() {
        return currentFromAccount;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. MAIN
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Запускает главный цикл обработки пользовательского ввода.
     * Перед запуском загружает ранее сохраненные данные,
     * перед завершением сохраняет измененные данные.
     */
    @Override
    public void run() {
        loadData();
        showMainPage();
        saveData();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. PAGES PROCESSING
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Отображает главное меню приложения.
     */
    protected void showMainPage() {
        if (mainConsolePage == null) {
            mainConsolePage = new MainConsolePage(this);
        }
        mainConsolePage.show();
    }

    /**
     * Отображает меню работы со счетами.
     */
    protected void showAccountPage() {
        if (accountConsolePage == null) {
            accountConsolePage = new AccountConsolePage(this);
        }
        accountConsolePage.show();
    }

    /**
     * Отображает страницу создания нового счета.
     */
    protected void showAccountCreatingPage() {
        if (accountCreatingConsolePage == null) {
            accountCreatingConsolePage = new AccountCreatingConsolePage(this);
        }
        accountCreatingConsolePage.show();
    }

    /**
     * Отображает список всех созданных счетов с возможностью выбора.
     */
    protected void showAccountListPage() {
        if (accountListConsolePage == null) {
            accountListConsolePage = new AccountListConsolePage(this);
        }
        accountListConsolePage.show();
    }

    /**
     * Отображает меню операций со счетом.
     */
    protected void showAccountOperationPage() {
        if (accountOperationConsolePage == null) {
            accountOperationConsolePage = new AccountOperationConsolePage(this);
        }
        accountOperationConsolePage.show();
    }

    /**
     * Отображает список всех транзакции текущего выбранного счета.
     */
    protected void showAccountTransactionPage() {
        if (accountTransactionConsolePage == null) {
            accountTransactionConsolePage = new AccountTransactionConsolePage(this);
        }
        accountTransactionConsolePage.show();
    }

    /**
     * Отображает список всех транзакции по всем счетам.
     */
    protected void showTransactionPage() {
        if (transactionConsolePage == null) {
            transactionConsolePage = new TransactionConsolePage(this);
        }
        transactionConsolePage.show();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. MISC
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Очищает консоль.
     */
    protected void clearText() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException _) {}
    }

    /**
     * Загружает данные для работы.
     */
    private void loadData() {
        clearText();

        try {
            bankService.loadAccounts();
        } catch (Exception exception) {
            String errMsg = MessageFormat.format("Не удалось загрузить сохраненные данные: {0}" +
                            "\n> Нажмите Enter чтобы продолжить работу без начальных данных...",
                    exception.getMessage()
            );

            System.out.println(errMsg);
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
        }
    }

    /**
     * Сохраняет данные по результату работы.
     */
    private void saveData() {
        clearText();

        try {
            bankService.saveAccounts();
        } catch (IOException exception) {
            String errMsg = MessageFormat.format("Не удалось сохранить данные: {0}" +
                            "\n> Нажмите Enter чтобы завершить работу с потерей данных...",
                    exception.getMessage()
            );

            System.out.println(errMsg);
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
        }
    }
}