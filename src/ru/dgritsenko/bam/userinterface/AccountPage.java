package ru.dgritsenko.bam.userinterface;

public class AccountPage extends Page {

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    public AccountPage(ConsoleService consoleService) {
        super(consoleService);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. INPUT / OUTPUT
    // -----------------------------------------------------------------------------------------------------------------

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
