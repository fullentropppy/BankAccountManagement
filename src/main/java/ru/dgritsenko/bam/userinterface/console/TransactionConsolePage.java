package ru.dgritsenko.bam.userinterface.console;

import ru.dgritsenko.bam.bank.Account;
import ru.dgritsenko.bam.bank.BankService;
import ru.dgritsenko.bam.printer.AccountConsolePrinter;

/**
 * Класс представляет страницу просмотра всех транзакций по всем счетам.
 */
public class TransactionConsolePage extends ConsolePage {
    private final BankService bankService;

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Создает страницу транзакций с указанным сервисом консоли.
     *
     * @param consoleUserInterface сервис для работы с консолью
     */
    public TransactionConsolePage(ConsoleUserInterface consoleUserInterface) {
        super(consoleUserInterface);
        this.bankService = consoleUserInterface.getBankService();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. INPUT / OUTPUT
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Отображает список всех транзакции по всем счетам.
     */
    @Override
    public void show() {
        super.setHeader("Транзакции");
        System.out.println();

        boolean transactionsExist = false;

        for (Account account : bankService.getAccounts()) {
            if (!account.getTransactions().isEmpty()) {
                transactionsExist = true;
                AccountConsolePrinter.printTransactions(account);
                System.out.println();
            }
        }

        if (!transactionsExist) {
            String message = "\tСписок транзакций пуст...";
            System.out.println(message);
        }

        super.waitForInputToContinue("Нажмите Enter для возврата в главное меню");
        super.consoleUserInterface.showMainPage();
    }
}