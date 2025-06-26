package ru.dgritsenko.userinterface;

import ru.dgritsenko.bank.Account;
import ru.dgritsenko.bank.BankService;

import java.text.MessageFormat;

/**
 * Класс представляет страницу создания нового банковского счета.
 */
public class AccountCreatingPage extends Page {
    private final BankService bankService;

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Создает страницу создания счета с указанным сервисом консоли.
     *
     * @param consoleUI сервис для работы с консолью
     */
    public AccountCreatingPage(ConsoleUI consoleUI) {
        super(consoleUI);
        this.bankService = consoleUI.getBankService();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // OVERRIDDEN
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Отображает страницу создания нового счета.
     */
    @Override
    public void show() {
        super.setHeader("Создание счета");

        String actionMsg = "Введите на латинице фамилию и первую буку имени владельца счета";
        String fromAccountHolderName = super.getString(actionMsg, "-");

        if (fromAccountHolderName.equals("-")) {
            super.consoleUI.showAccountPage();
        } else {
            createAccountAndShowResult(fromAccountHolderName);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // MISC
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Выполняет попытку создания нового счета и выводит результат в случае успеха.
     *
     * @param fromAccountHolderName имя владельца создаваемого счета
     */
    private void createAccountAndShowResult(String fromAccountHolderName) {
        Account account;

        try {
            account = bankService.createAccount(fromAccountHolderName);
        } catch (Exception exception) {
            super.printError(exception.getMessage(), "Нажмите Enter для возврата на страницу счетов");
            super.consoleUI.showAccountPage();
            return;
        }

        super.consoleUI.setCurrentFromAccount(account);

        String menu = MessageFormat.format("""
                \n\tСоздан новый счет: {0}
               
                \t1. Создать еще
                \t2. Операции
                \t3. Список счетов
                \t4. Главное меню""",
                account);
        super.setMenu(menu);

        int option = super.getOptionFromMenu("Введите номер пункта");
        switch (option) {
            case 1 -> super.consoleUI.showAccountCreatingPage();
            case 2 -> super.consoleUI.showAccountOperationPage();
            case 3 -> super.consoleUI.showAccountListPage();
            default -> super.consoleUI.showMainPage();
        }
    }
}