package ru.dgritsenko.bam.bank;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Класс, представляющий банковскую транзакцию.
 * Содержит информацию о типе, сумме, участниках, статусе и дате транзакции.
 */
public class Transaction {
    private final UUID uuid;
    private final LocalDateTime date;
    private final Account fromAccount;
    private final TransactionType transactionType;
    private final double amount;
    private final Account toAccount;
    private TransactionStatus status;

    // -----------------------------------------------------------------------------------------------------------------
    // OVERRIDDEN
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Возвращает строковое представление транзакции в формате:
     * "UUID от Дата (Статус)".
     *
     * @return строковое представление транзакции
     */
    @Override
    public String toString() {
        return MessageFormat.format(
                "{0} от {1} ({2})",
                uuid, getDateFormatted(), status);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Конструктор создает новую транзакцию с указанными параметрами.
     *
     * @param fromAccount счет отправителя
     * @param transactionType тип транзакции
     * @param amount сумма транзакции
     * @param toAccount счет получателя (может быть null)
     */
    public Transaction(Account fromAccount, TransactionType transactionType, double amount, Account toAccount) {
        this.uuid = UUID.randomUUID();
        this.date = LocalDateTime.now();
        this.fromAccount = fromAccount;
        this.transactionType = transactionType;
        this.amount = amount;
        this.toAccount = toAccount; // может быть null если бенефициар не предполагается
        this.status = TransactionStatus.UNCOMMITTED;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // GETTERS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Возвращает уникальный идентификатор транзакции.
     *
     * @return UUID транзакции
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Возвращает дату и время транзакции.
     *
     * @return дата транзакции
     */
    public LocalDateTime getDate() {
        return LocalDateTime.from(date);
    }

    /**
     * Возвращает отформатированную строку с датой транзакции.
     *
     * @return строка с датой в формате "yyyy-MM-dd HH:mm:ss"
     */
    public String getDateFormatted() {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Возвращает счет отправителя средств.
     *
     * @return счет отправителя
     */
    public Account getFromAccount() {
        return fromAccount;
    }

    /**
     * Возвращает тип операции транзакции.
     *
     * @return тип транзакции
     */
    public TransactionType getOperationType() {
        return transactionType;
    }

    /**
     * Возвращает сумму транзакции.
     *
     * @return сумма транзакции
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Возвращает счет получателя средств.
     *
     * @return счет получателя (может быть null)
     */
    public Account getToAccount() {
        return toAccount;
    }

    /**
     * Возвращает текущий статус транзакции.
     *
     * @return статус транзакции
     */
    public TransactionStatus getStatus() {
        return status;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // SETTERS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Устанавливает новый статус транзакции.
     *
     * @param status новый статус
     */
    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}