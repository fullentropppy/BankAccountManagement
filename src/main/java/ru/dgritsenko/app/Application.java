package ru.dgritsenko.app;

import ru.dgritsenko.bank.BankService;
import ru.dgritsenko.data.DataStorage;
import ru.dgritsenko.data.FileService;
import ru.dgritsenko.userinterface.ConsoleUI;
import ru.dgritsenko.userinterface.UserInterface;

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
        // Сервис работы с данными
        DataStorage dataStorage = new FileService();

        // Сервис работы с банковскими операциями
        BankService bankService = new BankService(dataStorage);

        // Сервис для взаимодействия с пользователем
        UserInterface userInterface = new ConsoleUI(bankService);
        userInterface.run();
    }
}