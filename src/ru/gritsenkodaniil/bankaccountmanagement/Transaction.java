package ru.gritsenkodaniil.bankaccountmanagement;

import java.util.Date;

public class Transaction {
    private final Date date = new Date();
    private final BankAccount holder;
    private final OperationType operationType;
    private final double amount;
    private final BankAccount beneficiary;
    private OperationStatus status = OperationStatus.UNCOMMITTED;

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

    public Date getDate() {
        return date;
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
        status = OperationStatus.COMMITTED;

        // Проверка баланса на счете списания при операциях списания (списание, снятие наличных, перевод)
        if (!operationType.isAddition()) {
            double balance = holder.getBalance();
            if (balance < amount) {
                status = OperationStatus.CANCELED;
            }
        }

        // Зачисление средств на счет получателя при операциях списания (списание, перевод)
        if (status.isCommitted() && operationType.hasBeneficiary()) {
            OperationStatus creditStatus = beneficiary.credit(amount, holder);

            // Отмена транзакции при ошибке внутри операции
            if (!creditStatus.isCommitted()) {
                status = OperationStatus.CANCELED;
            }
        }

        // Добавление транзакции в список транзакций счета
        holder.addTransaction(this);
    }
}