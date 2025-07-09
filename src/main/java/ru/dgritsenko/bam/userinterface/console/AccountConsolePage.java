package ru.dgritsenko.bam.userinterface.console;

/**
 * Класс представляет страницу со списком всех банковских счетов.
 */
public class AccountConsolePage extends ConsolePage {
    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Создает страницу меню счетов с указанным сервисом консоли.
     *
     * @param consoleUserInterface сервис для работы с консолью
     */
    public AccountConsolePage(ConsoleUserInterface consoleUserInterface) {
        super(consoleUserInterface);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // OVERRIDDEN
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Отображает меню работы со счетами.
     */
    @Override
    public void show() {
        super.setHeader("Меню счетов");

        String menu = """
                \n\t1. Создать
                \t2. Список
                
                \t3. Главное меню""";
        super.setMenu(menu);

        int option = super.getOptionFromMenu("Введите номер пункта");
        switch (option) {
            case 1 -> super.consoleUserInterface.showAccountCreatingPage();
            case 2 -> super.consoleUserInterface.showAccountListPage();
            default -> super.consoleUserInterface.showMainPage();
        };
    }
}