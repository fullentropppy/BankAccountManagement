package ru.dgritsenko.bam.main;

import ru.dgritsenko.bam.userinterface.ConsoleProcessor;

public class Application {
    public static void main(String[] args) {
        ConsoleProcessor consoleProcessor = new ConsoleProcessor();
        consoleProcessor.initialize();
    }
}