package ru.dgritsenko.datastorage;

import ru.dgritsenko.bank.Account;

import java.io.FileNotFoundException;
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
     */
    void saveAccounts(List<Account> accounts) throws IOException;
    /**
     * Загружает список банковских счетов.
     *
     * @return список загруженных счетов
     */
    List<Account> loadAccounts() throws IOException, ClassNotFoundException;
}