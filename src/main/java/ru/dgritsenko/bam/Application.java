package ru.dgritsenko.bam;

import ru.dgritsenko.bam.bank.BankService;
import ru.dgritsenko.bam.datastorage.DataStorage;
import ru.dgritsenko.bam.datastorage.FileService;
import ru.dgritsenko.bam.userinterface.console.ConsoleUserInterface;
import ru.dgritsenko.bam.userinterface.UserInterface;

/**
 * Главный класс приложения, содержащий точку входа.
 */
public class Application {
    /**
     * Точка входа в приложение.
     */
    public static void main(String[] args) {
        // Сервис работы с данными
        DataStorage dataStorage = new FileService();

        // Сервис работы с банковскими операциями
        BankService bankService = new BankService(dataStorage);

        // Сервис для взаимодействия с пользователем
        UserInterface userInterface = new ConsoleUserInterface();
        userInterface.setBankService(bankService);
        userInterface.run();
    }
}