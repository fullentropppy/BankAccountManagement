package ru.dgritsenko.bam.userinterface;

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
     * @param consoleService сервис для работы с консолью
     */
    public AccountPage(ConsoleService consoleService) {
        super(consoleService);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // OVERRIDDEN
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Отображает меню работы со счетами.
     */
    @Override
    public void show() {
        super.setTitle("Меню счетов");
        String menu = """
                \n\t1. Создать
                \t2. Список
                \t3. Главное меню""";
        super.setMenu(menu);

        int option = super.getOptionFromMenu("Введите номер пункта");

        switch (option) {
            case 1 -> super.consoleService.showAccountCreatingPage();
            case 2 -> super.consoleService.showAccountListPage();
            default -> super.consoleService.showMainPage();
        };
    }
}