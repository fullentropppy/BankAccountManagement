package ru.dgritsenko.bank;

/**
 * Класс-обработчик банковских транзакций.
 * <p>Содержит статические методы для выполнения различных типов операций.
 */
public final class TransactionService {

    private TransactionService() {}

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. OPERATIONS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Выполняет операцию, не требующую указания счета получателя.
     *
     * @param transactionType тип операции ({@link TransactionType#DEPOSIT} или {@link TransactionType#WITHDRAW})
     * @param fromAccount счет отправителя
     * @param amount сумма операции
     *
     * @return статус выполненной операции
     *
     * @throws NullPointerException если любой из обязательных параметров равен {@code null}
     * @throws IllegalArgumentException если {@code amount} <= {@code 0}
     */
    public static TransactionStatus perform(
            TransactionType transactionType,
            Account fromAccount,
            double amount)
    {
        TransactionStatus status = null;

        if (transactionType == TransactionType.DEPOSIT) {
            status = deposit(fromAccount, amount);
        } else if (transactionType == TransactionType.WITHDRAW) {
            status = withdrawal(fromAccount, amount);
        }

        return status;
    }

    /**
     * Выполняет операцию, требующую указания счета получателя.
     *
     * @param transactionType тип операции ({@link TransactionType#CREDIT}или {@link TransactionType#TRANSFER})
     * @param fromAccount счет отправителя
     * @param amount сумма операции
     * @param toAccount счет получателя
     *
     * @return статус выполненной операции
     *
     * @throws NullPointerException если любой из обязательных параметров равен {@code null}
     * @throws IllegalArgumentException если {@code amount} <= {@code 0}
     */
    public static TransactionStatus perform(
            TransactionType transactionType,
            Account fromAccount,
            double amount,
            Account toAccount)
    {
        TransactionStatus status = null;

        if (transactionType == TransactionType.CREDIT) {
            status = credit(fromAccount, amount, toAccount);
        } else if (transactionType == TransactionType.TRANSFER) {
            status = transfer(fromAccount, amount, toAccount);
        }

        return status;
    }

    /**
     * Выполняет операцию пополнения счета.
     *
     * @param fromAccount счет для пополнения
     * @param amount сумма пополнения
     *
     * @return статус операции
     *
     * @throws NullPointerException если {@code fromAccount} равен {@code null}
     * @throws IllegalArgumentException если {@code amount} <= {@code 0}
     */
    public static TransactionStatus deposit(Account fromAccount, double amount) {
        return processIncreasing(fromAccount, TransactionType.DEPOSIT, amount, null);
    }

    /**
     * Выполняет операцию зачисления средств на счет.
     *
     * @param fromAccount счет отправителя
     * @param amount сумма перевода
     * @param toAccount счет получателя
     *
     * @return статус операции
     *
     * @throws NullPointerException если любой из обязательных параметров равен {@code null}
     * @throws IllegalArgumentException если {@code amount} <= {@code 0}
     */
    public static TransactionStatus credit(Account fromAccount, double amount, Account toAccount) {
        return processIncreasing(fromAccount, TransactionType.CREDIT, amount, toAccount);
    }

    /**
     * Выполняет операцию снятия наличных со счета.
     *
     * @param fromAccount счет для снятия
     * @param amount сумма снятия
     *
     * @return статус операции
     *
     * @throws NullPointerException если {@code fromAccount} равен {@code null}
     * @throws IllegalArgumentException если {@code amount} <= {@code 0}
     */
    public static TransactionStatus withdrawal(Account fromAccount, double amount) {
        return processReducing(fromAccount, TransactionType.WITHDRAW, amount, null);
    }

    /**
     * Выполняет операцию перевода средств между счетами.
     *
     * @param fromAccount счет отправителя
     * @param amount сумма перевода
     * @param toAccount счет получателя
     *
     * @return статус операции
     *
     * @throws NullPointerException если любой из обязательных параметров равен {@code null}
     * @throws IllegalArgumentException если {@code amount} <= {@code 0}
     */
    public static TransactionStatus transfer(Account fromAccount, double amount, Account toAccount) {
        return processReducing(fromAccount, TransactionType.TRANSFER, amount, toAccount);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. MISC
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Обрабатывает транзакции, увеличивающие баланс счета.
     * Автоматически подтверждает транзакцию и добавляет её в историю.
     *
     * @param fromAccount счет, на который зачисляются средства
     * @param transactionType тип операции ({@link TransactionType#DEPOSIT})
     * @param amount сумма
     * @param toAccount счет-источник (для {@link TransactionType#CREDIT}) или {@code null} (для {@link TransactionType#DEPOSIT})
     *
     * @return статус транзакции
     *
     * @throws NullPointerException если любой из обязательных параметров равен {@code null}
     * @throws IllegalArgumentException если {@code amount} <= {@code 0}
     * @throws NullPointerException если для данного типа операции требуется {@code toAccount}, но он равен {@code null}
     */
    private static TransactionStatus processIncreasing(
            Account fromAccount,
            TransactionType transactionType,
            double amount,
            Account toAccount)
    {
        Transaction transaction = new Transaction.Builder()
                .setFromAccount(fromAccount)
                .setTransactionType(transactionType)
                .setAmount(amount)
                .setToAccount(toAccount)
                .setStatus(TransactionStatus.COMMITTED)
                .build();

        fromAccount.addTransaction(transaction);

        return transaction.getStatus();
    }

    /**
     * Обрабатывает транзакции, уменьшающие баланс счета.
     * Проверяет достаточность средств. Если средств недостаточно, транзакция отменяется.
     *
     * @param fromAccount счет, с которого списываются средства
     * @param transactionType тип операции ({@link TransactionType#TRANSFER} или {@code {@link TransactionType#WITHDRAW}})
     * @param amount сумма
     * @param toAccount счет-получатель (для {@link TransactionType#TRANSFER}) или null (для {@link TransactionType#WITHDRAW})
     *
     * @return статус транзакции
     *
     * @throws NullPointerException если любой из обязательных параметров равен {@code null}
     * @throws IllegalArgumentException если {@code amount} <= {@code 0}
     * @throws NullPointerException если для данного типа операции требуется {@code toAccount}, но он равен {@code null}
     */
    private static TransactionStatus processReducing(
            Account fromAccount,
            TransactionType transactionType,
            double amount,
            Account toAccount)
    {
        TransactionStatus status;

        if (fromAccount.getBalance() >= amount) {
            if (transactionType.hasToAccount()) {
                status = credit(toAccount, amount, fromAccount);
            } else {
                status = TransactionStatus.COMMITTED;
            }
        } else {
            status = TransactionStatus.CANCELED;
        }

        Transaction transaction = new Transaction.Builder()
                .setFromAccount(fromAccount)
                .setTransactionType(transactionType)
                .setAmount(amount)
                .setToAccount(toAccount)
                .setStatus(status)
                .build();
        
        fromAccount.addTransaction(transaction);

        return transaction.getStatus();
    }
}