package ru.dgritsenko.bam.userinterface;

import ru.dgritsenko.bam.bank.Account;
import ru.dgritsenko.bam.main.BankService;

public class TransactionPage extends Page {
    private final BankService bankService;

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    public TransactionPage(ConsoleService consoleService) {
        super(consoleService);
        this.bankService = consoleService.getBankService();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. INPUT / OUTPUT
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public void show() {
        super.setTitle("Транзакции");

        if (bankService.getAccounts().isEmpty()) {
            String message = "\n\tСписок транзакций пуст...";
            System.out.println(message);
        } else {
            System.out.println();

            for (Account account : bankService.getAccounts()) {
                account.printTransactions();
                System.out.println();
            }
        }

        super.waitForInputToContinue("Нажмите Enter для возврата в главное меню");

        super.consoleService.showMainPage();
    }
}
