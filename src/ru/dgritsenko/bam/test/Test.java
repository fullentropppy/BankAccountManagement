package ru.dgritsenko.bam.test;

import ru.dgritsenko.bam.bank.Account;
import ru.dgritsenko.bam.bank.TransactionService;

import java.util.HashMap;
import java.util.Map;

/**
 * Тестовый класс для проверки функциональности банковского приложения.
 * Содержит предопределенный набор операций для демонстрации работы системы.
 */
public class Test {
    private static final Map<String, Account> accounts = new HashMap<>();

    /**
     * Точка входа для тестового сценария.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        processPredefinedSet();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. PROCESSING
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Выполняет предопределенный набор операций и выводит результаты.
     */
    public static void processPredefinedSet() {
        initializeAccounts();
        performOperations();
        printAccountsInfo();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. INITIALIZING, PERFORMING, PRINTING
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Инициализирует тестовые счета:
     * - "Магазин" (ключ "store")
     * - "Иванов И" (ключ "ivanovI")
     * - "Сидоров С" (ключ "sidorovS")
     * - "Петров П" (ключ "petrovP")
     */
    private static void initializeAccounts() {
        addAccount("store", "Магазин");
        addAccount("ivanovI", "Иванов И");
        addAccount("sidorovS", "Сидоров С");
        addAccount("petrovP", "Петров П");
    }

    /**
     * Выполняет тестовый набор операций:
     * 1. Пополнение счета Иванова.
     * 2. Оплата в магазин.
     * 3. Перевод от Иванова Сидорову.
     * 4. Снятие наличных Сидоровым.
     * 5. Перевод от Сидорова Петрову.
     * 6. Возврат средств от Петрова Иванову.
     */
    private static void performOperations() {
        // Создание счетов
        Account store = accounts.get("store");
        Account ivanov = accounts.get("ivanov");
        Account sidorov = accounts.get("sidorov");
        Account petrov = accounts.get("petrov");

        // Выполнение операций через обработчик
        TransactionService.deposit(ivanov, 50000);
        TransactionService.debit(ivanov, 599.25, store);
        TransactionService.transfer(ivanov, 10000, sidorov);

        TransactionService.withdrawal(sidorov, 1000);
        TransactionService.transfer(sidorov, 2000, petrov);
        TransactionService.withdrawal(sidorov, 8000);

        TransactionService.transfer(petrov, 5000, ivanov);
        TransactionService.deposit(petrov, 5000);
        TransactionService.transfer(petrov, 5000, ivanov);
    }

    /**
     * Выводит на экран информацию о всех счетах: список транзакций и текущий баланс для каждого счета.
     * Формат вывода для каждого счета:
     * - Список транзакций (если есть)
     * - Текущий баланс
     * - Пустая строка-разделитель
     */
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

    /**
     * Создает новый счет и добавляет его в тестовую коллекцию.
     *
     * @param accountKey ключ для доступа к счету
     * @param holderName имя владельца счета
     */
    private static void addAccount(String accountKey, String holderName) {
        Account account = new Account(holderName);
        accounts.put(accountKey, account);
    }
}