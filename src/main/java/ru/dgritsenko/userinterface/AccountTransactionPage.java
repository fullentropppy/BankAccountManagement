package ru.dgritsenko.userinterface;

import ru.dgritsenko.bank.Account;
import ru.dgritsenko.printer.AccountPrinter;

import java.text.MessageFormat;

/**
 * Класс представляет страницу просмотра транзакций конкретного счета.
 */
public class AccountTransactionPage extends Page {
    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Создает страницу транзакций счета с указанным сервисом консоли.
     *
     * @param consoleService сервис для работы с консолью
     */
    public AccountTransactionPage(ConsoleService consoleService) {
        super(consoleService);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // OVERRIDDEN
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Отображает список всех транзакции текущего выбранного счета.
     */
    @Override
    public void show() {
        Account currentFromAccount = super.consoleService.getCurrentFromAccount();
        String title = MessageFormat.format("Транзакции счета {0}", currentFromAccount);
        super.setHeader(title);

        System.out.println();
        AccountPrinter.printTransactions(currentFromAccount);

        super.waitForInputToContinue("Нажмите Enter для возврата в меню операций со счетом");
        super.consoleService.showAccountOperationPage();
    }
}