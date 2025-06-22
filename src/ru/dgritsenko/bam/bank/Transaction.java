package ru.dgritsenko.bam.bank;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
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
        return MessageFormat.format("{0} от {1} ({2})", uuid, getDateFormatted(), status);
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
     * @throws NullPointerException если любой из обязательных параметров равен null
     * @throws IllegalArgumentException если amount <= 0
     * @throws NullPointerException если для данного типа транзакции требуется toAccount, но он равен null
     */
    public Transaction(
            Account fromAccount,
            TransactionType transactionType,
            double amount,
            Account toAccount) throws NullPointerException, IllegalArgumentException
    {
        this(
                UUID.randomUUID(),
                LocalDateTime.now(),
                fromAccount,
                transactionType,
                amount,
                toAccount,
                TransactionStatus.COMMITTED
        );
    }

    /**
     * Конструктор создает новую транзакцию с указанными параметрами.
     *
     * @param uuid уникальный идентификатор транзакции
     * @param date дата и время транзакции
     * @param fromAccount счет отправителя
     * @param transactionType тип транзакции
     * @param amount сумма транзакции
     * @param toAccount счет получателя (может быть null)
     * @param status статус транзакции
     * @throws NullPointerException если любой из обязательных параметров равен null
     * @throws IllegalArgumentException если amount <= 0
     * @throws NullPointerException если для данного типа транзакции требуется toAccount, но он равен null
     */
    public Transaction(
            UUID uuid,
            LocalDateTime date,
            Account fromAccount,
            TransactionType transactionType,
            double amount,
            Account toAccount,
            TransactionStatus status) throws NullPointerException, IllegalArgumentException
    {
        // Установка значений с проверкой на null
        this.uuid = Objects.requireNonNull(uuid, "UUID не должен быть null");
        this.date = Objects.requireNonNull(date, "Дата не должна быть null");
        this.fromAccount = Objects.requireNonNull(fromAccount, "Счет не должен быть null");
        this.transactionType = Objects.requireNonNull(transactionType, "Тип транзакции не должен быть null");
        this.status = Objects.requireNonNull(status, "Статус не должен быть null");

        // Установка значений с расширенной проверкой
        this.amount = validAmount(amount);
        this.toAccount = validToAccount(toAccount);
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
        return date;
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
    public TransactionType getTransactionType() {
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
     * @param status новый статус
     * @throws NullPointerException если status равен null
     */
    public void setStatus(TransactionStatus status) throws NullPointerException {
        this.status = Objects.requireNonNull(status, "Статус не должен быть null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. CHECKS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Проверяет сумму транзакции на валидность.
     * @param amount сумма транзакции для проверки
     * @return валидная сумма транзакции
     * @throws IllegalArgumentException если amount <= 0
     */
    private double validAmount(double amount) {
        if (amount <= 0) {
            String errMsg = MessageFormat.format(
                    "Некорректная сумма транзакции \"{0}\": " +
                    "сумма транзакции должна быть больше нуля",
                    amount
            );
            throw new IllegalArgumentException(errMsg);
        }

        return amount;
    }

    /**
     * Проверяет счет получателя на соответствие типу транзакции.
     * @param toAccount счет получателя для проверки
     * @return счет получателя, если он корректен
     * @throws NullPointerException если для типа транзакции требуется счет получателя, но он не указан
     */
    private Account validToAccount(Account toAccount) {
        if (transactionType.hasToAccount() && toAccount == null) {
            String errMsg = MessageFormat.format(
                    "Счет получателя не должен быть null при типе транзакции \"{0}\"",
                    transactionType
            );
            throw new NullPointerException(errMsg);
        }

        return toAccount;
    }
}