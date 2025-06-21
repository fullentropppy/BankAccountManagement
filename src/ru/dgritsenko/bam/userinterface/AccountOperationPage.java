package ru.dgritsenko.bam.userinterface;

import ru.dgritsenko.bam.bank.Account;
import ru.dgritsenko.bam.bank.TransactionStatus;
import ru.dgritsenko.bam.bank.TransactionType;
import ru.dgritsenko.bam.bank.BankService;

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
     * Обрабатывает операции, требующие только счет-источник (пополнение, снятие).
     *
     * @param transactionType тип операции (DEPOSIT или WITHDRAW)
     */
    private void processOperationWithOnlyFromAccount(TransactionType transactionType) {
        printNewPageHeader();
        printOperationHeader(transactionType);

        Account currentFromAccount = super.consoleService.getCurrentFromAccount();
        double amount = super.getAmount("Введите сумму операции");
        TransactionStatus result = bankService.performTransaction(transactionType, currentFromAccount, amount);

        String resultMessage = MessageFormat.format("\nСтатус транзакции: {0}", result);
        System.out.println(resultMessage);

        super.waitForInputToContinue("Нажмите Enter для продолжения");
        super.consoleService.showAccountOperationPage();
    }

    /**
     * Обрабатывает операции, требующие указания счета-получателя (оплата, перевод).
     *
     * @param transactionType тип операции (TRANSFER)
     */
    private void processOperationWithToAccount(TransactionType transactionType) {
        printNewPageHeader();
        printOperationHeader(transactionType);

        List<Account> availableAccounts = new ArrayList<>();
        StringBuilder toAccountOptions = new StringBuilder();

        Account currentFromAccount = super.consoleService.getCurrentFromAccount();

        int i = 1;

        for (Account toAccount : bankService.getAccounts()) {
            if (!(currentFromAccount == toAccount)) {
                availableAccounts.add(toAccount);
                String accountOption = MessageFormat.format("\n\t{0}. {1}", i, toAccount);
                toAccountOptions.append(accountOption);
                i++;
            }
        }

        if (toAccountOptions.isEmpty()) {
            String missingMessage = "\n\tСписок получателей пуст...";
            System.out.println(missingMessage);

            super.waitForInputToContinue("Нажмите Enter для продолжения");
        } else {
            String cancellationOption = MessageFormat.format("\n\n\t{0}. Отмена", i);
            toAccountOptions.append(cancellationOption);

            String pageMenu = toAccountOptions.toString();
            super.setMenu(pageMenu);

            int option = super.getOptionFromMenu("Введите номер получателя");

            if (option < bankService.getAccounts().size()) {
                Account toAccount = availableAccounts.get(option - 1);
                double amount = getAmount("Введите сумму операции");

                TransactionStatus result = bankService.performTransaction(
                        transactionType, currentFromAccount, amount, toAccount);

                String resultMessage = MessageFormat.format("\nСтатус транзакции: {0}", result);
                System.out.println(resultMessage);

                super.waitForInputToContinue("Нажмите Enter для продолжения");
            }
        }

        super.consoleService.showAccountOperationPage();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. MISC
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Выводит заголовок страницы операций со счетом, содержащий:
     * - Название страницы
     * - Информацию о текущем счете
     * - Текущий баланс счета
     */
    private void printNewPageHeader() {
        Account currentFromAccount = super.consoleService.getCurrentFromAccount();

        String title = MessageFormat.format(
                "Операции со счетом: {0}, баланс: {1}",
                currentFromAccount, currentFromAccount.getBalance());
        super.setHeader(title);
    }

    private void printOperationHeader(TransactionType transactionType) {
        String operationHeader = MessageFormat.format("\n\tВыполняемая операция: {0}", transactionType);
        System.out.println(operationHeader);
    }

}