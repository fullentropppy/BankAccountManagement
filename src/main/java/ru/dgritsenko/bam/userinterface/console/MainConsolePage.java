package ru.dgritsenko.bam.userinterface.console;

/**
 * Класс представляет главную страницу банковского приложения.
 */
public class MainConsolePage extends ConsolePage {
    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Создает главную страницу с указанным сервисом консоли.
     *
     * @param consoleUserInterface сервис для работы с консолью
     */
    public MainConsolePage(ConsoleUserInterface consoleUserInterface) {
        super(consoleUserInterface);
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
            case 1 -> super.consoleUserInterface.showAccountPage();
            case 2 -> super.consoleUserInterface.showTransactionPage();
            default -> {
                return;
            }
        };
    }
}