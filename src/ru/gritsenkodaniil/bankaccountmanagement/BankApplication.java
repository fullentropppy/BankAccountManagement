package ru.gritsenkodaniil.bankaccountmanagement;

import java.util.HashMap;
import java.util.Map;

/**
 * Главный класс банковского приложения для управления счетами.
 * Содержит методы для инициализации счетов, выполнения операций и вывода информации.
 */
public class BankApplication {
    private static final Map<String, BankAccount> accounts = new HashMap<>();

    /**
     * Главный метод приложения.
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        initializeAccounts();
        performOperations();
        printAccountsInfo();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. INITIALIZING, PERFORMING, PRINTING
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Инициализирует счета банка.
     */
    private static void initializeAccounts() {
        addAccount("store", 50501944, "Магазин");
        addAccount("ivanovI", 94867303, "Иванов И");
        addAccount("sidorovS", 70019471, "Сидоров С");
        addAccount("petrovP", 86107026, "Петров П");
    }

    /**
     * Выполняет операции над счетами.
     */
    private static void performOperations() {
        // Создание счетов
        BankAccount store = accounts.get("store");
        BankAccount ivanovI = accounts.get("ivanovI");
        BankAccount sidorovS = accounts.get("sidorovS");
        BankAccount petrovP = accounts.get("petrovP");

        // Создание обработчика операций
        TransactionProcessor transactionProcessor = new TransactionProcessor();

        // Выполнение операций через обработчик
        transactionProcessor.deposit(ivanovI, 50000);
        transactionProcessor.debit(ivanovI, 599.25, store);
        transactionProcessor.transfer(ivanovI, 10000, sidorovS);

        transactionProcessor.withdrawal(sidorovS, 1000);
        transactionProcessor.transfer(sidorovS, 2000, petrovP);
        transactionProcessor.withdrawal(sidorovS, 8000);

        transactionProcessor.transfer(petrovP, 5000, ivanovI);
        transactionProcessor.deposit(petrovP, 5000);
        transactionProcessor.transfer(petrovP, 5000, ivanovI);
    }

    /**
     * Выводит информацию о всех счетах.
     */
    private static void printAccountsInfo() {
        for (BankAccount account : accounts.values()) {
            account.printTransactions();
            account.printBalance();
            System.out.println();
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. MISC
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Добавляет новый счет в систему.
     * @param accountKey ключ счета
     * @param accountNumber номер счета
     * @param ownerName имя владельца
     */
    private static void addAccount(String accountKey, long accountNumber, String ownerName) {
        BankAccount account = new BankAccount(accountNumber, ownerName);
        accounts.put(accountKey, account);
    }
}