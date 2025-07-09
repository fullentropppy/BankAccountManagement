package ru.dgritsenko.bam.userinterface;

import ru.dgritsenko.bam.bank.BankService;

/**
 * Интерфейс пользовательского интерфейса банковского приложения.
 * <p>Определяет основной метод для запуска взаимодействия с пользователем.
 * Реализации могут предоставлять различные способы взаимодействия
 * (консольный, графический интерфейс и т.д.).
 */
public interface UserInterface {
    /**
     * Запускает пользовательский интерфейс приложения.
     */
    void run();

    /**
     * Устанавливает значение поля bankService, если необходимо.
     */
    void setBankService(BankService bankService);
}