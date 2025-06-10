package ru.gritsenkodaniil.bankaccountmanagement;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Transaction {
    private final LocalDateTime date = LocalDateTime.now();
    private final BankAccount holder;
    private final OperationType operationType;
    private final double amount;
    private final BankAccount beneficiary;
    private OperationStatus status = OperationStatus.UNCOMMITTED;

    // -----------------------------------------------------------------------------------------------------------------
    // OVERRIDDEN
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return MessageFormat.format(
                "№{0} от {1} ({2})",
                Long.toString(hashCode()), getDateFormatted(), status.getTitle());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    public Transaction(BankAccount holder, OperationType operationType, double amount, BankAccount beneficiary) {
        this.holder = holder;
        this.operationType = operationType;
        this.amount = amount;
        this.beneficiary = beneficiary; // может быть null если бенефициар не предполагается
    }

    // -----------------------------------------------------------------------------------------------------------------
    // GETTERS
    // -----------------------------------------------------------------------------------------------------------------

    public LocalDateTime getDate() {
        return LocalDateTime.from(date);
    }

    public String getDateFormatted() {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public BankAccount getHolder() {
        return holder;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public double getAmount() {
        return amount;
    }

    public BankAccount getBeneficiary() {
        return beneficiary;
    }

    public OperationStatus getStatus() {
        return status;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS
    // -----------------------------------------------------------------------------------------------------------------

    public void execute() {
        if (!checkBalance()) {
            status = OperationStatus.CANCELED;
        } else if (operationType.hasBeneficiary() && !processBeneficiary()) {
            status = OperationStatus.CANCELED;
        } else {
            status = OperationStatus.COMMITTED;
        }
        holder.addTransaction(this);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. MISC
    // -----------------------------------------------------------------------------------------------------------------

    private boolean checkBalance() {
        return operationType.isAddition() || holder.getBalance() >= amount;
    }

    private boolean processBeneficiary() {
        return beneficiary.credit(amount, holder).isCommitted();
    }
}