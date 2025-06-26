package ru.dgritsenko.userinterface;

/**
 * Класс представляет страницу со списком всех банковских счетов.
 */
public class AccountPage extends Page {
    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Создает страницу меню счетов с указанным сервисом консоли.
     *
     * @param consoleUI сервис для работы с консолью
     */
    public AccountPage(ConsoleUI consoleUI) {
        super(consoleUI);
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
            case 1 -> super.consoleUI.showAccountCreatingPage();
            case 2 -> super.consoleUI.showAccountListPage();
            default -> super.consoleUI.showMainPage();
        };
    }
}