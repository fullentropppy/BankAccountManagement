package ru.dgritsenko.bam.datastorage;

import ru.dgritsenko.bam.bank.Account;

import java.io.IOException;
import java.util.List;

/**
 * Интерфейс для хранения и загрузки данных.
 * <p>
 * Определяет контракт для классов, реализующих механизмы сохранения и загрузки
 * данных. Реализации могут использовать различные способы
 * хранения данных (файлы, базы данных и т.д.).
 */
public interface DataStorage {
    /**
     * Сохраняет список банковских счетов.
     *
     * @param accounts список счетов для сохранения
     *
     * @throws IOException если произошла ошибка ввода-вывода при записи файла
     */
    void saveAccounts(List<Account> accounts) throws IOException;
    /**
     * Загружает список банковских счетов.
     *
     * @return список загруженных счетов
     *
     * @throws IOException если произошла ошибка ввода-вывода при чтении файла
     * @throws ClassNotFoundException если класс объекта в файле не найден
     */
    List<Account> loadAccounts() throws IOException, ClassNotFoundException;
}