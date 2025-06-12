package ru.dgritsenko.bam;

import ru.dgritsenko.bam.userinterface.ConsoleProcessor;

/**
 * Главный класс банковского приложения для управления счетами.
 * Содержит методы для инициализации счетов, выполнения операций и вывода информации.
 */
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