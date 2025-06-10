package ru.gritsenkodaniil.bankaccountmanagement;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс, представляющий банковскую транзакцию.
 * Содержит информацию о дате, типе операции, сумме, участниках и статусе.
 */
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

    /**
     * Возвращает строковое представление транзакции.
     * @return строка в формате "№ХэшКода от Дата (Статус)"
     */
    @Override
    public String toString() {
        return MessageFormat.format(
                "№{0} от {1} ({2})",
                Long.toString(hashCode()), getDateFormatted(), status.getTitle());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Создает новую транзакцию.
     * @param holder владелец счета
     * @param operationType тип операции
     * @param amount сумма
     * @param beneficiary бенефициар (может быть null)
     */
    public Transaction(BankAccount holder, OperationType operationType, double amount, BankAccount beneficiary) {
        this.holder = holder;
        this.operationType = operationType;
        this.amount = amount;
        this.beneficiary = beneficiary; // может быть null если бенефициар не предполагается
    }

    // -----------------------------------------------------------------------------------------------------------------
    // GETTERS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Возвращает дату транзакции.
     * @return дата транзакции
     */
    public LocalDateTime getDate() {
        return LocalDateTime.from(date);
    }

    /**
     * Возвращает отформатированную дату транзакции.
     * @return строка с датой в формате "yyyy-MM-dd HH:mm:ss"
     */
    public String getDateFormatted() {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Возвращает владельца счета.
     * @return владелец счета
     */
    public BankAccount getHolder() {
        return holder;
    }

    /**
     * Возвращает тип операции.
     * @return тип операции
     */
    public OperationType getOperationType() {
        return operationType;
    }

    /**
     * Возвращает сумму транзакции.
     * @return сумма
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Возвращает бенефициара.
     * @return бенефициар (может быть null)
     */
    public BankAccount getBeneficiary() {
        return beneficiary;
    }

    /**
     * Возвращает статус транзакции.
     * @return статус
     */
    public OperationStatus getStatus() {
        return status;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Выполняет транзакцию.
     * Проверяет баланс и обрабатывает бенефициара при необходимости.
     */
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

    /**
     * Проверяет достаточность средств на счете.
     * @return true если средств достаточно или операция на пополнение, иначе false
     */
    private boolean checkBalance() {
        return operationType.isAddition() || holder.getBalance() >= amount;
    }

    /**
     * Обрабатывает операцию с бенефициаром.
     * @return true если операция успешна, иначе false
     */
    private boolean processBeneficiary() {
        return beneficiary.credit(amount, holder).isCommitted();
    }
}