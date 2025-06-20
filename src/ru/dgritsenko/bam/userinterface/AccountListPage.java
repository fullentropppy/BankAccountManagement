package ru.dgritsenko.bam.userinterface;

import ru.dgritsenko.bam.bank.Account;
import ru.dgritsenko.bam.bank.BankService;

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
     * @param consoleService сервис для работы с консолью
     */
    public AccountListPage(ConsoleService consoleService) {
        super(consoleService);
        this.bankService = consoleService.getBankService();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // OVERRIDDEN
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Отображает список всех созданных счетов с возможностью выбора.
     */
    @Override
    public void show() {
        super.setTitle("Список счетов");

        StringBuilder pageMenuOptions = new StringBuilder();

        int i = 1;

        for (Account account : bankService.getAccounts()) {
            String accountOption = MessageFormat.format("\n\t{0}. {1}", i, account);
            pageMenuOptions.append(accountOption);
            i++;
        }

        if (pageMenuOptions.isEmpty()) {
            String missingMessage = "\n\tСписок счетов пуст...";
            System.out.println(missingMessage);

            super.waitForInputToContinue("Нажмите Enter для возврата в меню счетов");
            super.consoleService.showAccountPage();
        } else {
            String goToMainMenuOption = MessageFormat.format("\n\n\t{0}. Главное меню", i);
            pageMenuOptions.append(goToMainMenuOption);

            String pageMenu = pageMenuOptions.toString();
            super.setMenu(pageMenu);

            int option = super.getOptionFromMenu("Введите номер пункта");
            int optionsAmount = bankService.getAccounts().size() + 1;

            if (option < optionsAmount) {
                Account currentFromAccount = bankService.getAccounts().get(option - 1);
                super.consoleService.setCurrentFromAccount(currentFromAccount);
                super.consoleService.showAccountOperationPage();
            } else if (option == optionsAmount) {
                super.consoleService.showMainPage();
            }
        }
    }
}