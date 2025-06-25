package ru.dgritsenko.userinterface;

import ru.dgritsenko.bank.Account;
import ru.dgritsenko.bank.BankService;

import java.text.MessageFormat;
import java.util.Scanner;

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
     * @param consoleService сервис для работы с консолью
     */
    public AccountCreatingPage(ConsoleService consoleService) {
        super(consoleService);
        this.bankService = consoleService.getBankService();
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

        String actionMsg = "\n> Введите на латинице фамилию и первую буку имени владельца счета " +
                "(или '-' для отмены): ";
        System.out.print(actionMsg);

        Scanner fromAccountHolderNameScanner = new Scanner(System.in);
        String fromAccountHolderName = fromAccountHolderNameScanner.nextLine();

        if (fromAccountHolderName.equals("-")) {
            super.consoleService.showAccountPage();
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
            super.consoleService.showAccountPage();
            return;
        }

        super.consoleService.setCurrentFromAccount(account);

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
            case 1 -> super.consoleService.showAccountCreatingPage();
            case 2 -> super.consoleService.showAccountOperationPage();
            case 3 -> super.consoleService.showAccountListPage();
            default -> super.consoleService.showMainPage();
        }
    }
}