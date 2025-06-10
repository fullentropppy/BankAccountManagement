package ru.gritsenkodaniil.bankaccountmanagement;

import java.util.HashMap;
import java.util.Map;

public class BankApplication {
    private static Map<String, BankAccount> accounts = new HashMap<>();

    public static void main(String[] args) {
        // Создание счетов
        initializeAccounts();

        // Выполнение операций
        performOperations();

        // Вывод инфо
        printAccountsInfo();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. INITIALIZING, PERFORMING, PRINTING
    // -----------------------------------------------------------------------------------------------------------------

    private static void initializeAccounts() {
        addAccount("store", 50501944, "Магазин");
        addAccount("ivanovI", 94867303, "Иванов И");
        addAccount("sidorovS", 70019471, "Сидоров С");
        addAccount("petrovP", 86107026, "Петров П");
    }

    private static void performOperations() {
        BankAccount store = accounts.get("store");
        BankAccount ivanovI = accounts.get("ivanovI");
        BankAccount sidorovS = accounts.get("sidorovS");
        BankAccount petrovP = accounts.get("petrovP");

        ivanovI.deposit(50000);
        ivanovI.debit(599.25, store);
        ivanovI.transfer(10000, sidorovS);

        sidorovS.withdrawal(1000);
        sidorovS.transfer(2000, petrovP);
        sidorovS.withdrawal(8000);

        petrovP.transfer(5000, ivanovI);
        petrovP.deposit(5000);
        petrovP.transfer(5000, ivanovI);
    }

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

    private static void addAccount(String accountKey, long accountNumber, String ownerName) {
        BankAccount account = new BankAccount(accountNumber, ownerName);
        accounts.put(accountKey, account);
    }
}