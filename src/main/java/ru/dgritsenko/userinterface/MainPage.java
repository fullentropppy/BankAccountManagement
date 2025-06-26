package ru.dgritsenko.userinterface;

/**
 * Класс представляет главную страницу банковского приложения.
 */
public class MainPage extends Page {
    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Создает главную страницу с указанным сервисом консоли.
     *
     * @param consoleUI сервис для работы с консолью
     */
    public MainPage(ConsoleUI consoleUI) {
        super(consoleUI);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // OVERRIDDEN
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Отображает главное меню приложения.
     */
    @Override
    public void show() {
        super.setHeader("Главное меню");

        String menu = """
                \n\t1. Счета
                \t2. Транзакции
                \t3. Выход""";
        super.setMenu(menu);

        int option = super.getOptionFromMenu("Введите номер пункта");
        switch (option) {
            case 1 -> super.consoleUI.showAccountPage();
            case 2 -> super.consoleUI.showTransactionPage();
            default -> {
                return;
            }
        };
    }
}