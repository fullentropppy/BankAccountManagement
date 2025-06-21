package ru.dgritsenko.bam.userinterface;

import ru.dgritsenko.bam.bank.Account;
import ru.dgritsenko.bam.bank.BankService;

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

        String actionMessage = "\n> Введите ФИО владельца нового счета: ";
        System.out.print(actionMessage);

        Scanner fromAccountHolderNameScanner = new Scanner(System.in);
        String fromAccountHolderName = fromAccountHolderNameScanner.nextLine();

        Account account = bankService.createAccount(fromAccountHolderName);
        super.consoleService.setCurrentFromAccount(account);

        String menu = MessageFormat.format("""
                
                \tСоздан новый счет: {0}
                
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
        };
    }
}