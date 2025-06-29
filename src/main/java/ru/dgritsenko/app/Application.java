package ru.dgritsenko.app;

import ru.dgritsenko.bank.BankService;
import ru.dgritsenko.datastorage.DataStorage;
import ru.dgritsenko.datastorage.FileService;
import ru.dgritsenko.userinterface.console.ConsoleUIService;
import ru.dgritsenko.userinterface.UserInterface;
import ru.dgritsenko.userinterface.graphical.GraphicalUIService;

import java.util.*;

/**
 * Главный класс приложения, содержащий точку входа.
 */
public class Application {
    private static boolean USE_GUI;

    static {
        USE_GUI = false;
    }

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки.
     *             <p>Возможные параметры:
     *             <ul>
     *              <li>{@code -gui} использовать графический интерфейс.
     *              Если параметр отсутствует, будет использован консольный интерфейс.
     *             </ul>
     */
    public static void main(String[] args) {
        setParams(args);

        // Сервис работы с данными
        DataStorage dataStorage = new FileService();

        // Сервис работы с банковскими операциями
        BankService bankService = new BankService(dataStorage);

        // Сервис для взаимодействия с пользователем
        UserInterface userInterface = null;
        if (USE_GUI) {
            userInterface = new GraphicalUIService();
        } else {
            userInterface = new ConsoleUIService();
        }
        userInterface.setBankService(bankService);
        userInterface.run();
    }

    /**
     * Устанавливает параметры приложения из аргументов командной строки.
     *
     * @param appArgs аргументы командной строки.
     */
    private static void setParams(String[] appArgs) {
        List<String> appArgsArray = new ArrayList<>(Arrays.asList(appArgs));

        if (appArgsArray.contains("-gui")) {
            USE_GUI = true;
        }
    }
}