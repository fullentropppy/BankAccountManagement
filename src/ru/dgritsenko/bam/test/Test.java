package ru.dgritsenko.bam.test;

import ru.dgritsenko.bam.bank.Account;
import ru.dgritsenko.bam.bank.TransactionProcessor;

import java.util.HashMap;
import java.util.Map;

public class Test {
    private static final Map<String, Account> accounts = new HashMap<>();

    public static void main(String[] args) {
        processPredefinedSet();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. PROCESSING
    // -----------------------------------------------------------------------------------------------------------------

    public static void processPredefinedSet() {
        initializeAccounts();
        performOperations();
        printAccountsInfo();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. INITIALIZING, PERFORMING, PRINTING
    // -----------------------------------------------------------------------------------------------------------------

    private static void initializeAccounts() {
        addAccount("store", "Магазин");
        addAccount("ivanovI", "Иванов И");
        addAccount("sidorovS", "Сидоров С");
        addAccount("petrovP", "Петров П");
    }

    private static void performOperations() {
        // Создание счетов
        Account store = accounts.get("store");
        Account ivanov = accounts.get("ivanov");
        Account sidorov = accounts.get("sidorov");
        Account petrov = accounts.get("petrov");

        // Выполнение операций через обработчик
        TransactionProcessor.deposit(ivanov, 50000);
        TransactionProcessor.debit(ivanov, 599.25, store);
        TransactionProcessor.transfer(ivanov, 10000, sidorov);

        TransactionProcessor.withdrawal(sidorov, 1000);
        TransactionProcessor.transfer(sidorov, 2000, petrov);
        TransactionProcessor.withdrawal(sidorov, 8000);

        TransactionProcessor.transfer(petrov, 5000, ivanov);
        TransactionProcessor.deposit(petrov, 5000);
        TransactionProcessor.transfer(petrov, 5000, ivanov);
    }

    private static void printAccountsInfo() {
        for (Account account : accounts.values()) {
            account.printTransactions();
            account.printBalance();
            System.out.println();
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. MISC
    // -----------------------------------------------------------------------------------------------------------------

    private static void addAccount(String accountKey, String holderName) {
        Account account = new Account(holderName);
        accounts.put(accountKey, account);
    }
}