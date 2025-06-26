package ru.dgritsenko.userinterface;

import ru.dgritsenko.bank.Account;
import ru.dgritsenko.bank.TransactionStatus;
import ru.dgritsenko.bank.TransactionType;
import ru.dgritsenko.bank.BankService;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс представляет страницу операций с конкретным банковским счетом.
 */
public class AccountOperationPage extends Page {
    private final BankService bankService;

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Создает страницу операций со счетом с указанным сервисом консоли.
     *
     * @param consoleService сервис для работы с консолью
     */
    public AccountOperationPage(ConsoleService consoleService) {
        super(consoleService);
        this.bankService = consoleService.getBankService();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // OVERRIDDEN
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Отображает меню операций со счетом.
     */
    @Override
    public void show() {
        printNewPageHeader();

        String menu = """
                \n\t1. Пополнить
                \t2. Перевести
                \t3. Снять наличные
                
                \t4. Транзакции счета
                \t5. Список счетов
                \t6. Меню счетов
                \t7. Главное меню""";
        super.setMenu(menu);

        int option = super.getOptionFromMenu("Введите номер пункта");
        switch (option) {
            case 1 -> processOperationWithOnlyFromAccount(TransactionType.DEPOSIT);
            case 2 -> processOperationWithToAccount(TransactionType.TRANSFER);
            case 3 -> processOperationWithOnlyFromAccount(TransactionType.WITHDRAW);
            case 4 -> super.consoleService.showAccountTransactionPage();
            case 5 -> super.consoleService.showAccountListPage();
            case 6 -> super.consoleService.showAccountPage();
            default -> super.consoleService.showMainPage();
        };
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. OPERATION PROCESSING
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Обрабатывает операции, требующие только счет-источник.
     *
     * @param transactionType тип операции ({@code DEPOSIT} или {@code WITHDRAW})
     */
    private void processOperationWithOnlyFromAccount(TransactionType transactionType) {
        printNewPageHeader();
        printOperationHeader(transactionType);
        processOperation(transactionType, null);
        super.consoleService.showAccountOperationPage();
    }

    /**
     * Обрабатывает операции, требующие указания счета-получателя.
     *
     * @param transactionType тип операции ({@code TRANSFER})
     */
    private void processOperationWithToAccount(TransactionType transactionType) {
        printNewPageHeader();
        printOperationHeader(transactionType);

        Account toAccount = getToAccount();
        if (toAccount != null) {
            processOperation(transactionType, toAccount);
        }

        super.consoleService.showAccountOperationPage();
    }

    private void processOperation(TransactionType transactionType, Account toAccount) {
        double amount = getAmount("Введите сумму операции", true);
        if (amount > 0) {
            Account currentFromAccount = super.consoleService.getCurrentFromAccount();
            TransactionStatus result;

            if (toAccount == null) {
                result = bankService.performTransaction(transactionType, currentFromAccount, amount);
            } else {
                result = bankService.performTransaction(transactionType, currentFromAccount, amount, toAccount);
            }

            String resultMsg = MessageFormat.format("\nСтатус транзакции: {0}", result);
            System.out.println(resultMsg);
            super.waitForInputToContinue("Нажмите Enter для продолжения");
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. MISC
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Выводит заголовок страницы операций со счетом.
     */
    private void printNewPageHeader() {
        Account currentFromAccount = super.consoleService.getCurrentFromAccount();

        String title = MessageFormat.format(
                "Операции со счетом: {0}, баланс: {1}",
                currentFromAccount, currentFromAccount.getBalance()
        );

        super.setHeader(title);
    }

    /**
     * Выводит заголовок выполняемой операции со счетом.
     *
     * @param transactionType тип выполняемой операции
     */
    private void printOperationHeader(TransactionType transactionType) {
        String operationHeader = MessageFormat.format("\n\tВыполняемая операция: {0}", transactionType);
        System.out.println(operationHeader);
    }

    /**
     * Выводит меню с выбором счета получателя.
     *
     * @return выбранный счет, иначе {@code null}
     */
    private Account getToAccount() {
        Account toAccount = null;

        Account currentFromAccount = super.consoleService.getCurrentFromAccount();
        List<Account> availableAccounts = new ArrayList<>();
        StringBuilder toAccountOptions = new StringBuilder();

        int i = 1;
        for (Account account : bankService.getAccounts()) {
            // Пропуск текущего счета
            if (!(currentFromAccount == account)) {
                availableAccounts.add(account);
                String accountOption = MessageFormat.format("\n\t{0}. {1}", i, account);
                toAccountOptions.append(accountOption);
                i++;
            }
        }

        if (toAccountOptions.isEmpty()) {
            System.out.println("\n\tСписок получателей пуст...");
            super.waitForInputToContinue("Нажмите Enter для продолжения");
        } else {
            String cancellationOption = MessageFormat.format("\n\n\t{0}. Отмена", i);
            toAccountOptions.append(cancellationOption);

            String pageMenu = toAccountOptions.toString();
            super.setMenu(pageMenu);

            int option = super.getOptionFromMenu("Введите номер получателя");
            if (option > 0 && option < bankService.getNumberOfAccounts()) {
                toAccount = availableAccounts.get(option - 1);
            }
        }

        return toAccount;
    }
}