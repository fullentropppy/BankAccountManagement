package ru.dgritsenko.bam.userinterface.console;

import ru.dgritsenko.bam.bank.Account;
import ru.dgritsenko.bam.bank.BankService;

import java.text.MessageFormat;

/**
 * Класс представляет страницу со списком всех банковских счетов.
 */
public class AccountListConsolePage extends ConsolePage {
    private final BankService bankService;

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Создает страницу списка счетов с указанным сервисом консоли.
     *
     * @param consoleUserInterface сервис для работы с консолью
     */
    public AccountListConsolePage(ConsoleUserInterface consoleUserInterface) {
        super(consoleUserInterface);
        this.bankService = consoleUserInterface.getBankService();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // OVERRIDDEN
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Отображает список всех созданных счетов с возможностью выбора.
     */
    @Override
    public void show() {
        super.setHeader("Список счетов");

        StringBuilder pageMenuOptions = new StringBuilder();

        int i = 2;
        for (Account account : bankService.getAccounts()) {
            String accountOption = MessageFormat.format("\n\t{0}. {1}", i, account);
            pageMenuOptions.append(accountOption);
            i++;
        }

        if (pageMenuOptions.isEmpty()) {
            System.out.println("\n\tСписок счетов пуст...");
            super.waitForInputToContinue("Нажмите Enter для возврата в меню счетов");
            super.consoleUserInterface.showAccountPage();
        } else {
            pageMenuOptions.insert(0, "\n\t1. Меню счетов\n");

            String menu = pageMenuOptions.toString();
            super.setMenu(menu);

            int option = super.getOptionFromMenu("Введите номер счета");
            int optionsAmount = bankService.getNumberOfAccounts() + 1;

            if (option == 1) {
                super.consoleUserInterface.showAccountPage();
            } else {
                Account currentFromAccount = bankService.getAccount(option - 2);
                super.consoleUserInterface.setCurrentFromAccount(currentFromAccount);
                super.consoleUserInterface.showAccountOperationPage();
            }
        }
    }
}