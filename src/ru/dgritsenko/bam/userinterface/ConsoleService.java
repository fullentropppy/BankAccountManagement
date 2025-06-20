package ru.dgritsenko.bam.userinterface;

import ru.dgritsenko.bam.bank.Account;
import ru.dgritsenko.bam.bank.BankService;

/**
 * Класс для обработки взаимодействия с пользователем через консоль.
 * Реализует пользовательский интерфейс банковского приложения.
 */
public class ConsoleService {
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
    public ConsoleService(BankService bankService) {
        this.bankService = bankService;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // GETTERS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Возвращает сервис для работы с банковскими операциями.
     *
     * @return сервис банка
     */
    public BankService getBankService() {
        return bankService;
    }

    /**
     * Возвращает текущий выбранный счет для операций.
     *
     * @return текущий счет
     */
    public Account getCurrentFromAccount() {
        return currentFromAccount;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // SETTERS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Устанавливает текущий обрабатываемый счет.
     *
     * @param currentFromAccount счет
     */
    public void setCurrentFromAccount(Account currentFromAccount) {
        this.currentFromAccount = currentFromAccount;
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