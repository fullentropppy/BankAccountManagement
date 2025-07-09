package ru.dgritsenko.bam.bank;

import ru.dgritsenko.bam.datastorage.DataStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Сервис для управления банковскими счетами и транзакциями.
 */
public class BankService {
    private final DataStorage dataStorage;
    private final List<Account> accounts;

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Создает сервис банковского приложения и загружает сохраненные данные.
     */
    public BankService(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.accounts = new ArrayList<>();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // GETTERS
    // -----------------------------------------------------------------------------------------------------------------

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. MAIN
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Загружает данные списка счетов в {@code accounts}.
     *
     * @throws IOException если произошла ошибка ввода-вывода при чтении файла
     * @throws ClassNotFoundException если класс объекта в файле не найден
     */
    public void loadAccounts() throws IOException, ClassNotFoundException {
        List<Account> loadedAccounts = dataStorage.loadAccounts();
        accounts.addAll(loadedAccounts);
    }

    /**
     * Сохраняет данные списка счетов {@code accounts}.
     *
     * @throws IOException если произошла ошибка ввода-вывода при записи файла
     */
    public void saveAccounts() throws IOException {
        dataStorage.saveAccounts(accounts);
    }

    /**
     * Создает новый счет, добавляет в список и возвращает его.
     *
     * @return созданный счет
     *
     * @throws NullPointerException если {@code holderName} равен {@code null}
     * @throws IllegalArgumentException если {@code holderName} имеет неверный формат
     */
    public Account createAccount(String holderName) {
        Account account = new Account.Builder().setHolderName(holderName).build();
        accounts.add(account);
        return account;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. GETTING DATA
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Возвращает счет по индексу из списка счетов.
     *
     * @param indexInAccounts индекс счета в {@code accounts}
     *
     * @return счет
     *
     * @throws IndexOutOfBoundsException если {@code indexInAccounts} не существует в {@code accounts}
     */
    public Account getAccount(int indexInAccounts) {
        return accounts.get(indexInAccounts);
    }

    /**
     * Возвращает количество счетов в {@code accounts}.
     *
     * @return количество счетов
     */
    public int getNumberOfAccounts() {
        return accounts.size();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. TRANSACTION PERFORMING
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Выполняет операцию, не требующую указания счета получателя.
     *
     * @param transactionType тип операции ({@code DEPOSIT} или {@code WITHDRAW})
     * @param fromAccount счет отправителя
     * @param amount сумма операции
     *
     * @return статус выполненной операции
     *
     * @throws NullPointerException если любой из обязательных параметров равен {@code null}
     * @throws IllegalArgumentException если {@code amount} <= {@code 0}
     */
    public TransactionStatus performTransaction(
            TransactionType transactionType,
            Account fromAccount,
            double amount)
    {
        return TransactionService.perform(transactionType, fromAccount, amount);
    }

    /**
     * Выполняет операцию, требующую указания счета получателя.
     *
     * @param transactionType тип операции ({@code CREDIT} или {@code TRANSFER})
     * @param fromAccount счет отправителя
     * @param amount сумма операции
     * @param toAccount счет получателя
     *
     * @return статус выполненной операции
     *
     * @throws NullPointerException если любой из обязательных параметров равен {@code null}
     * @throws IllegalArgumentException если {@code amount} <= {@code 0}
     */
    public TransactionStatus performTransaction(
            TransactionType transactionType,
            Account fromAccount,
            double amount,
            Account toAccount)
    {
        return TransactionService.perform(transactionType, fromAccount, amount, toAccount);
    }
}