package ru.dgritsenko.bam.main;

import ru.dgritsenko.bam.userinterface.ConsoleService;

/**
 * Главный класс приложения, содержащий точку входа.
 */
public class Application {
    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        BankService bankService = new BankService();
        ConsoleService consoleService = new ConsoleService(bankService);
        consoleService.run();
    }
}