package ru.dgritsenko.bam.userinterface;

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
     * @param consoleService сервис для работы с консолью
     */
    public MainPage(ConsoleService consoleService) {
        super(consoleService);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // OVERRIDDEN
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Отображает главное меню приложения.
     */
    @Override
    public void show() {
        super.setTitle("Главное меню");
        String menu = """
                \n\t1. Счета
                \t2. Транзакции
                \t3. Выход""";
        super.setMenu(menu);

        int option = super.getOptionFromMenu("Введите номер пункта");

        switch (option) {
            case 1 -> super.consoleService.showAccountPage();
            case 2 -> super.consoleService.showTransactionPage();
            default -> System.exit(0);
        };
    }
}