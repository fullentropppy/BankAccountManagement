package ru.dgritsenko.bam.bank;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

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

    @Override
    public String toString() {
        return MessageFormat.format(
                "{0} от {1} ({2})",
                uuid, getDateFormatted(), status);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

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

    public UUID getUuid() {
        return uuid;
    }

    public LocalDateTime getDate() {
        return LocalDateTime.from(date);
    }

    public String getDateFormatted() {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public TransactionType getOperationType() {
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

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}