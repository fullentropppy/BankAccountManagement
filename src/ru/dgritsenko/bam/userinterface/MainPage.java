package ru.dgritsenko.bam.userinterface;

public class MainPage extends Page {

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    public MainPage(ConsoleService consoleService) {
        super(consoleService);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. INPUT / OUTPUT
    // -----------------------------------------------------------------------------------------------------------------

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
            case 1 -> consoleService.showAccountPage();
            case 2 -> consoleService.showTransactionPage();
            default -> System.exit(0);
        };
    }
}
