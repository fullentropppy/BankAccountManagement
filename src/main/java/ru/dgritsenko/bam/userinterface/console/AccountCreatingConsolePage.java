package ru.dgritsenko.bam.userinterface.console;

import ru.dgritsenko.bam.bank.Account;
import ru.dgritsenko.bam.bank.BankService;

import java.text.MessageFormat;

/**
 * Класс представляет страницу создания нового банковского счета.
 */
public class AccountCreatingConsolePage extends ConsolePage {
    private final BankService bankService;

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Создает страницу создания счета с указанным сервисом консоли.
     *
     * @param consoleUserInterface сервис для работы с консолью
     */
    public AccountCreatingConsolePage(ConsoleUserInterface consoleUserInterface) {
        super(consoleUserInterface);
        this.bankService = consoleUserInterface.getBankService();
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
            super.consoleUserInterface.showAccountPage();
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
            super.consoleUserInterface.showAccountPage();
            return;
        }

        super.consoleUserInterface.setCurrentFromAccount(account);

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
            case 1 -> super.consoleUserInterface.showAccountCreatingPage();
            case 2 -> super.consoleUserInterface.showAccountOperationPage();
            case 3 -> super.consoleUserInterface.showAccountListPage();
            default -> super.consoleUserInterface.showMainPage();
        }
    }
}