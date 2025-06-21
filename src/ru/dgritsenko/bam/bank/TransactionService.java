package ru.dgritsenko.bam.bank;

/**
 * Класс-обработчик банковских транзакций.
 * Содержит статические методы для выполнения различных типов операций.
 */
public class TransactionService {
    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. OPERATIONS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Выполняет операцию, не требующую указания счета получателя.
     *
     * @param transactionType тип операции (DEPOSIT или WITHDRAW)
     * @param fromAccount счет отправителя
     * @param amount сумма операции
     * @return статус выполненной операции
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
     * @param transactionType тип операции (CREDIT или TRANSFER)
     * @param fromAccount счет отправителя
     * @param amount сумма операции
     * @param toAccount счет получателя
     * @return статус выполненной операции
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
     * @return статус операции
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
     * @return статус операции
     */
    public static TransactionStatus credit(Account fromAccount, double amount, Account toAccount) {
        return processIncreasing(fromAccount, TransactionType.CREDIT, amount, toAccount);
    }

    /**
     * Выполняет операцию снятия наличных со счета.
     *
     * @param fromAccount счет для снятия
     * @param amount сумма снятия
     * @return статус операции
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
     * @return статус операции
     */
    public static TransactionStatus transfer(Account fromAccount, double amount, Account toAccount) {
        return processReducing(fromAccount, TransactionType.TRANSFER, amount, toAccount);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. MISC
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Обрабатывает транзакции, увеличивающие баланс счета (пополнение, зачисление).
     * Автоматически подтверждает транзакцию (COMMITTED) и добавляет её в историю.
     *
     * @param fromAccount счет, на который зачисляются средства
     * @param transactionType тип операции (DEPOSIT или CREDIT)
     * @param amount сумма
     * @param toAccount счет-источник (для CREDIT) или null (для DEPOSIT)
     * @return статус транзакции (всегда COMMITTED)
     */
    private static TransactionStatus processIncreasing(
            Account fromAccount,
            TransactionType transactionType,
            double amount,
            Account toAccount) {

        Transaction transaction = new Transaction(fromAccount, transactionType, amount, toAccount);
        transaction.setStatus(TransactionStatus.COMMITTED); // Проверки не требуются, транзакция успешна
        fromAccount.addTransaction(transaction);

        return transaction.getStatus();
    }

    /**
     * Обрабатывает транзакции, уменьшающие баланс счета (списание, перевод, снятие).
     * Проверяет достаточность средств. Если средств недостаточно, транзакция отменяется (CANCELED).
     * Для операций с получателем (TRANSFER) также создает встречную транзакцию.
     *
     * @param fromAccount счет, с которого списываются средства
     * @param transactionType тип операции (TRANSFER или WITHDRAW)
     * @param amount сумма
     * @param toAccount счет-получатель (для TRANSFER) или null (для WITHDRAW)
     * @return статус транзакции (COMMITTED или CANCELED)
     */
    private static TransactionStatus processReducing(
            Account fromAccount,
            TransactionType transactionType,
            double amount,
            Account toAccount) {

        TransactionStatus status;
        Transaction transaction = new Transaction(fromAccount, transactionType, amount, toAccount);

        if (fromAccount.getBalance() >= amount) {
            if (transactionType.hasToAccount()) {
                status = credit(toAccount, amount, fromAccount);
            } else {
                status = TransactionStatus.COMMITTED;
            }
        } else {
            status = TransactionStatus.CANCELED;
        }

        transaction.setStatus(status);
        fromAccount.addTransaction(transaction);

        return transaction.getStatus();
    }
}