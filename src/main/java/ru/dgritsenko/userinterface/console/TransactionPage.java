package ru.dgritsenko.userinterface.console;

import ru.dgritsenko.bank.Account;
import ru.dgritsenko.bank.BankService;
import ru.dgritsenko.printer.AccountPrinter;

/**
 * Класс представляет страницу просмотра всех транзакций по всем счетам.
 */
public class TransactionPage extends Page {
    private final BankService bankService;

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Создает страницу транзакций с указанным сервисом консоли.
     *
     * @param consoleUIService сервис для работы с консолью
     */
    public TransactionPage(ConsoleUIService consoleUIService) {
        super(consoleUIService);
        this.bankService = consoleUIService.getBankService();
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

        if (bankService.getNumberOfAccounts() == 0) {
            String message = "\n\tСписок транзакций пуст...";
            System.out.println(message);
        } else {
            System.out.println();

            for (Account account : bankService.getAccounts()) {
                AccountPrinter.printTransactions(account);
                System.out.println();
            }
        }

        super.waitForInputToContinue("Нажмите Enter для возврата в главное меню");
        super.consoleUIService.showMainPage();
    }
}