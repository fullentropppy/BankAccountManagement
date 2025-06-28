package ru.dgritsenko.app;

import ru.dgritsenko.bank.BankService;
import ru.dgritsenko.datastorage.DataStorage;
import ru.dgritsenko.datastorage.FileService;
import ru.dgritsenko.userinterface.console.ConsoleUIService;
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
        UserInterface userInterface = new ConsoleUIService(bankService);
        userInterface.run();
    }
}