package ru.dgritsenko.userinterface;

import ru.dgritsenko.bank.Account;
import ru.dgritsenko.bank.BankService;

import java.text.MessageFormat;

/**
 * Класс представляет страницу со списком всех банковских счетов.
 */
public class AccountListPage extends Page {
    private final BankService bankService;

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Создает страницу списка счетов с указанным сервисом консоли.
     *
     * @param consoleUI сервис для работы с консолью
     */
    public AccountListPage(ConsoleUI consoleUI) {
        super(consoleUI);
        this.bankService = consoleUI.getBankService();
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

        int i = 1;
        for (Account account : bankService.getAccounts()) {
            String accountOption = MessageFormat.format("\n\t{0}. {1}", i, account);
            pageMenuOptions.append(accountOption);
            i++;
        }

        if (pageMenuOptions.isEmpty()) {
            System.out.println("\n\tСписок счетов пуст...");
            super.waitForInputToContinue("Нажмите Enter для возврата в меню счетов");
            super.consoleUI.showAccountPage();
        } else {
            String goToMainMenuOption = MessageFormat.format("\n\n\t{0}. Меню счетов", i);
            pageMenuOptions.append(goToMainMenuOption);

            String menu = pageMenuOptions.toString();
            super.setMenu(menu);

            int option = super.getOptionFromMenu("Введите номер счета");
            int optionsAmount = bankService.getNumberOfAccounts() + 1;

            if (option < optionsAmount) {
                Account currentFromAccount = bankService.getAccount(option - 1);
                super.consoleUI.setCurrentFromAccount(currentFromAccount);
                super.consoleUI.showAccountOperationPage();
            } else if (option == optionsAmount) {
                super.consoleUI.showAccountPage();
            }
        }
    }
}