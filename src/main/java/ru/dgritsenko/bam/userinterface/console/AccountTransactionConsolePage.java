package ru.dgritsenko.bam.userinterface.console;

import ru.dgritsenko.bam.bank.Account;
import ru.dgritsenko.bam.printer.AccountConsolePrinter;

import java.text.MessageFormat;

/**
 * Класс представляет страницу просмотра транзакций конкретного счета.
 */
public class AccountTransactionConsolePage extends ConsolePage {
    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Создает страницу транзакций счета с указанным сервисом консоли.
     *
     * @param consoleUserInterface сервис для работы с консолью
     */
    public AccountTransactionConsolePage(ConsoleUserInterface consoleUserInterface) {
        super(consoleUserInterface);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // OVERRIDDEN
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Отображает список всех транзакции текущего выбранного счета.
     */
    @Override
    public void show() {
        Account currentFromAccount = super.consoleUserInterface.getCurrentFromAccount();
        String title = MessageFormat.format("Транзакции счета {0}", currentFromAccount);
        super.setHeader(title);

        System.out.println();
        AccountConsolePrinter.printTransactions(currentFromAccount);

        super.waitForInputToContinue("Нажмите Enter для возврата в меню операций со счетом");
        super.consoleUserInterface.showAccountOperationPage();
    }
}