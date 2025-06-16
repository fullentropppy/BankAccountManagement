package ru.dgritsenko.bam.main;

import ru.dgritsenko.bam.userinterfacenew.ConsoleProcessor;

public class Application {
    /**
     * Главный метод приложения.
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        ConsoleProcessor consoleProcessor = new ConsoleProcessor();
        consoleProcessor.initialize();
    }
}