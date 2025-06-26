package ru.dgritsenko.userinterface.console;

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
     * @param consoleUIService сервис для работы с консолью
     */
    public AccountPage(ConsoleUIService consoleUIService) {
        super(consoleUIService);
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
            case 1 -> super.consoleUIService.showAccountCreatingPage();
            case 2 -> super.consoleUIService.showAccountListPage();
            default -> super.consoleUIService.showMainPage();
        };
    }
}