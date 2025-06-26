package ru.dgritsenko.userinterface.console;

import ru.dgritsenko.bank.Account;
import ru.dgritsenko.bank.BankService;
import ru.dgritsenko.userinterface.UserInterface;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Scanner;

/**
 * Класс для обработки взаимодействия с пользователем через консоль.
 * <p>Реализует пользовательский интерфейс банковского приложения.
 */
public class ConsoleUIService implements UserInterface {
    private final BankService bankService;
    private Account currentFromAccount;

    private Page mainPage;
    private Page accountPage;
    private Page accountCreatingPage;
    private Page accountListPage;
    private Page accountOperationPage;
    private Page accountTransactionPage;
    private Page transactionPage;

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Создаёт обработчик консольного ввода с указанным сервисом банка.
     *
     * @param bankService сервис для работы с банковскими операциями
     */
    public ConsoleUIService(BankService bankService) {
        this.bankService = bankService;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // SETTERS
    // -----------------------------------------------------------------------------------------------------------------

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
        Scanner scanner = new Scanner(System.in);

        try {
            bankService.loadAccounts();
        } catch (Exception exception) {
            String errMsg = MessageFormat.format("Не удалось загрузить сохраненные данные: {0}" +
                    "\nНажмите Enter чтобы продолжить работу без начальных данных...",
                    exception.getMessage()
            );
            System.out.println(errMsg);
            scanner.nextLine();
        }

        showMainPage();

        try {
            bankService.saveAccounts();
        } catch (IOException exception) {
            String errMsg = MessageFormat.format("Не удалось сохранить данные: {0}" +
                    "\nНажмите Enter чтобы завершить работу с потерей данных...",
                    exception.getMessage()
            );
            System.out.println(errMsg);
            scanner.nextLine();
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. PAGES PROCESSING
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Отображает главное меню приложения.
     */
    protected void showMainPage() {
        if (mainPage == null) {
            mainPage = new MainPage(this);
        }
        mainPage.show();
    }

    /**
     * Отображает меню работы со счетами.
     */
    protected void showAccountPage() {
        if (accountPage == null) {
            accountPage = new AccountPage(this);
        }
        accountPage.show();
    }

    /**
     * Отображает страницу создания нового счета.
     */
    protected void showAccountCreatingPage() {
        if (accountCreatingPage == null) {
            accountCreatingPage = new AccountCreatingPage(this);
        }
        accountCreatingPage.show();
    }

    /**
     * Отображает список всех созданных счетов с возможностью выбора.
     */
    protected void showAccountListPage() {
        if (accountListPage == null) {
            accountListPage = new AccountListPage(this);
        }
        accountListPage.show();
    }

    /**
     * Отображает меню операций со счетом.
     */
    protected void showAccountOperationPage() {
        if (accountOperationPage == null) {
            accountOperationPage = new AccountOperationPage(this);
        }
        accountOperationPage.show();
    }

    /**
     * Отображает список всех транзакции текущего выбранного счета.
     */
    protected void showAccountTransactionPage() {
        if (accountTransactionPage == null) {
            accountTransactionPage = new AccountTransactionPage(this);
        }
        accountTransactionPage.show();
    }

    /**
     * Отображает список всех транзакции по всем счетам.
     */
    protected void showTransactionPage() {
        if (transactionPage == null) {
            transactionPage = new TransactionPage(this);
        }
        transactionPage.show();
    }
}