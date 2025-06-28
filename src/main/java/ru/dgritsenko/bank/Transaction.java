package ru.dgritsenko.bank;

import java.io.Serial;
import java.io.Serializable;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

/**
 * Класс, представляющий банковскую транзакцию.
 * <p>Содержит информацию о типе, сумме, участниках, статусе и дате транзакции.
 * <p>Создание объекта выполняется через {@link Builder}.
 */
public class Transaction implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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
     * Возвращает строковое представление транзакции в формате: "UUID от Дата (Статус)".
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
     * Служебный конструктор для создания объекта через {@link Transaction.Builder}.
     *
     * @param builder статический вложенный класс-источник данных для заполнения
     */
    private Transaction(Builder builder) {
        this.uuid = builder.uuid;
        this.date = builder.date;
        this.fromAccount = builder.fromAccount;
        this.transactionType = builder.transactionType;
        this.amount = builder.amount;
        this.toAccount = builder.toAccount;
        this.status = builder.status;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // GETTERS
    // -----------------------------------------------------------------------------------------------------------------

    public UUID getUuid() {
        return uuid;
    }

    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Возвращает отформатированную строку с датой транзакции формате:
     * "yyyy-MM-dd HH:mm:ss".
     *
     * @return строка с форматированной датой
     */
    public String getDateFormatted() {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public Account getToAccount() {
        return toAccount;
    }

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
     *
     * @throws NullPointerException если {@code status} равен {@code null}
     */
    public void setStatus(TransactionStatus status) {
        this.status = Objects.requireNonNull(status, "Статус не должен быть null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. STATIC VALIDATION
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Проверяет сумму транзакции на корректность.
     *
     * @param amount сумма транзакции для проверки
     *
     * @return корректная сумма транзакции
     *
     * @throws IllegalArgumentException если {@code amount} <= {@code 0}
     */
    private static double validAmount(double amount) {
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
     *
     * @param toAccount счет получателя для проверки
     *
     * @return счет получателя, если он корректен
     *
     * @throws NullPointerException если для данного типа транзакции требуется {@code toAccount}, но он равен {@code null}
     */
    private static Account validToAccount(Account toAccount, TransactionType transactionType) {
        if (transactionType.hasToAccount() && toAccount == null) {
            String errMsg = MessageFormat.format(
                    "Счет получателя не должен быть null при типе транзакции \"{0}\"",
                    transactionType
            );
            throw new NullPointerException(errMsg);
        }

        return toAccount;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // BUILDER NESTED CLASS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Вложенный статичный класс, представляющий построитель родительского класса {@link Transaction}.
     * <p>Содержит поля идентичные полям родительского класса.
     * Каждое поле имеет set-метод для установки значения.
     */
    public static class Builder {
        private UUID uuid;
        private LocalDateTime date;
        private Account fromAccount;
        private TransactionType transactionType;
        private double amount;
        private Account toAccount;
        private TransactionStatus status;

        // -------------------------------------------------------------------------------------------------------------
        // BUILDER. CONSTRUCTORS
        // -------------------------------------------------------------------------------------------------------------

        /**
         * Создает построитель для последующего создания основного класса {@link Transaction}.
         */
        public Builder() {
            super();
        }

        public Builder setUUID(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder setDate(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public Builder setFromAccount(Account fromAccount) {
            this.fromAccount = fromAccount;
            return this;
        }

        public Builder setTransactionType(TransactionType transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public Builder setAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public Builder setToAccount(Account toAccount) {
            this.toAccount = toAccount;
            return this;
        }

        public Builder setStatus(TransactionStatus status) {
            this.status = status;
            return this;
        }

        // -------------------------------------------------------------------------------------------------------------
        // BUILDER. BUILDING
        // -------------------------------------------------------------------------------------------------------------

        /**
         * Валидирует значения полей и создает экземпляр основного класса {@link Transaction}.
         *
         * @return новая транзакция
         */
        public Transaction build() {
            validate();
            return new Transaction(this);
        }

        /**
         * Создает без валидации экземпляр основного класса {@link Transaction}.
         *
         * @return новая транзакция
         */
        public Transaction buildWithoutValidations() {
            return new Transaction(this);
        }

        // -------------------------------------------------------------------------------------------------------------
        // BUILDER. MISC
        // -------------------------------------------------------------------------------------------------------------

        /**
         * Валидирует результат заполнения полей построителя.
         */
        private void validate() {
            // Проверки на null
            fromAccount = Objects.requireNonNull(fromAccount, "Счет не должен быть null");
            transactionType = Objects.requireNonNull(transactionType, "Тип транзакции не должен быть null");

            // Проверка на null с установкой значений по умолчанию при необходимости
            uuid = uuid == null
                    ? UUID.randomUUID()
                    : Objects.requireNonNull(uuid, "UUID не должен быть null");

            date = date == null
                    ? LocalDateTime.now()
                    : Objects.requireNonNull(date, "Дата не должна быть null");

            status = status == null
                    ? TransactionStatus.UNCOMMITTED
                    : Objects.requireNonNull(status, "Статус не должен быть null");

            // Расширенные проверки
            amount = validAmount(amount);
            toAccount = validToAccount(toAccount, transactionType);
        }
    }
}