package ru.dgritsenko.bam.main;

import ru.dgritsenko.bam.userinterface.ConsoleProcessor;

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
        ConsoleProcessor consoleProcessor = new ConsoleProcessor();
        consoleProcessor.run();
    }
}