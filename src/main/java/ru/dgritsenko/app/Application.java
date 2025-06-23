package ru.dgritsenko.app;

import ru.dgritsenko.bank.BankService;
import ru.dgritsenko.userinterface.ConsoleService;

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
        // Обработка банковских операций
        BankService bankService = new BankService();

        // Обработка работы через консоль
        ConsoleService consoleService = new ConsoleService(bankService);
        consoleService.run();
    }
}