package ru.gritsenkodaniil.bankaccountmanagement;

import java.util.Date;

public class Transaction {
    private final Date date;
    private final BankAccount holder;
    private final OperationType operationType;
    private final double amount;
    private final BankAccount beneficiary;

    private OperationStatus status;
    private String message;

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    public Transaction(BankAccount holder, OperationType operationType, double amount, BankAccount beneficiary) {
        this.date = new Date();
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

    public String getMessage() {
        return message;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS
    // -----------------------------------------------------------------------------------------------------------------

    public void execute() {
        status = OperationStatus.COMMITTED;
        message = "";

        // Проверка баланса на счете списания при операциях списания (списание, снятие наличных, перевод)
        if (!operationType.isAddition()) {
            double balance = holder.getBalance();
            if (balance < amount) {
                status = OperationStatus.CANCELED;
                message = "Недостаточно средств";
            }
        }

        // Зачисление средств на счет получателя при операциях списания (списание, перевод)
        if (status == OperationStatus.COMMITTED && operationType.hasBeneficiary()) {
            OperationStatus creditStatus = beneficiary.credit(amount, holder);

            // Отмена транзакции при ошибке внутри операции
            if (creditStatus == OperationStatus.CANCELED) {
                status = OperationStatus.CANCELED;
                message = "Не удалось зачислить средства получателю";
            }
        }

        // Добавление транзакции в список транзакций счета
        holder.addTransaction(this);
    }
}