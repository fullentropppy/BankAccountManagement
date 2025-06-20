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
        printNewAccountOperationPage();

        String menu = """
                \n\t1. Пополнить
                \t2. Оплатить
                \t3. Перевести
                \t4. Снять наличные
                
                \t5. Транзакции счета
                \t6. Список счетов
                \t7. Меню счетов
                \t8. Главное меню""";
        super.setMenu(menu);

        int option = super.getOptionFromMenu("Введите номер пункта");

        switch (option) {
            case 1 -> processOperationWithOnlyFromAccount(TransactionType.DEPOSIT);
            case 2 -> processOperationWithToAccount(TransactionType.DEBIT);
            case 3 -> processOperationWithToAccount(TransactionType.TRANSFER);
            case 4 -> processOperationWithOnlyFromAccount(TransactionType.WITHDRAW);
            case 5 -> super.consoleService.showAccountTransactionPage();
            case 6 -> super.consoleService.showAccountListPage();
            case 7 -> super.consoleService.showAccountPage();
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

        Account currentFromAccount = super.consoleService.getCurrentFromAccount();

        double amount = super.getAmount(inputTitle);
        TransactionStatus result = bankService.performTransaction(transactionType, currentFromAccount, amount);

        String resultMessage = MessageFormat.format(resultTitleTemplate, result);
        System.out.println(resultMessage);

        super.waitForInputToContinue("Нажмите Enter для продолжения");
        super.consoleService.showAccountOperationPage();
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

        Account currentFromAccount = super.consoleService.getCurrentFromAccount();

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

            super.waitForInputToContinue("Нажмите Enter для продолжения");
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
            super.setMenu(pageMenu);

            int option = super.getOptionFromMenu(inputOptionTittle);

            if (option < bankService.getAccounts().size()) {
                Account toAccount = availableAccounts.get(option - 1);
                double amount = getAmount(inputAmountTittle);

                TransactionStatus result = bankService.performTransaction(
                        transactionType, currentFromAccount, amount, toAccount);

                String resultMessage = MessageFormat.format(resultTitleTemplate, result);
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
    protected void printNewAccountOperationPage() {
        Account currentFromAccount = super.consoleService.getCurrentFromAccount();

        String title = MessageFormat.format(
                "Операции со счетом: {0}, баланс: {1}",
                currentFromAccount, currentFromAccount.getBalance());
        super.setTitle(title);
    }
}