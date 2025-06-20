package ru.dgritsenko.bam.userinterface;

import ru.dgritsenko.bam.bank.Account;
import ru.dgritsenko.bam.main.BankService;

import java.text.MessageFormat;

public class AccountTransactionPage extends Page {

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    public AccountTransactionPage(ConsoleService consoleService) {
        super(consoleService);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. INPUT / OUTPUT
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public void show() {
        Account currentFromAccount = super.consoleService.getCurrentFromAccount();
        String title = MessageFormat.format("Транзакции счета {0}", currentFromAccount);
        super.setTitle(title);

        System.out.println();
        currentFromAccount.printTransactions();

        super.waitForInputToContinue("Нажмите Enter для возврата в меню операций со счетом");

        super.consoleService.showAccountOperationPage();
    }
}
