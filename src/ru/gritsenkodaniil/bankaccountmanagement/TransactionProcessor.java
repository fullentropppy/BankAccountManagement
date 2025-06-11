package ru.gritsenkodaniil.bankaccountmanagement;

/**
 * Класс, обрабатывающий банковские операции (пополнение, списание, переводы).
 * Создает транзакции, проверяет условия их выполнения и обновляет состояние счетов.
 */
public class TransactionProcessor {

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. OPERATIONS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Выполняет операцию пополнения счета.
     * @param holder владелец счета
     * @param amount сумма пополнения
     * @return статус выполнения операции
     */
    public OperationStatus deposit(BankAccount holder, double amount) {
        return processIncreasing(holder, OperationType.DEPOSIT, amount, null);
    }

    /**
     * Выполняет операцию зачисления средств от отправителя.
     * @param holder владелец счета
     * @param amount сумма
     * @param beneficiary отправитель средств
     * @return статус выполнения операции
     */
    public OperationStatus credit(BankAccount holder, double amount, BankAccount beneficiary) {
        return processIncreasing(holder, OperationType.CREDIT, amount, beneficiary);
    }

    /**
     * Выполняет операцию списания средств в пользу бенефициара.
     * @param holder владелец счета
     * @param amount сумма
     * @param beneficiary бенефициар
     * @return статус выполнения операции
     */
    public OperationStatus debit(BankAccount holder, double amount, BankAccount beneficiary ) {
        return processReducing(holder, OperationType.DEBIT, amount, beneficiary);
    }

    /**
     * Выполняет операцию снятия наличных.
     * @param holder владелец счета
     * @param amount сумма
     * @return статус выполнения операции
     */
    public OperationStatus withdrawal(BankAccount holder, double amount) {
        return processReducing(holder, OperationType.WITHDRAW, amount, null);
    }

    /**
     * Выполняет операцию перевода средств.
     * @param holder владелец счета
     * @param amount сумма
     * @param receiver получатель
     * @return статус выполнения операции
     */
    public OperationStatus transfer(BankAccount holder, double amount, BankAccount receiver) {
        return processReducing(holder, OperationType.TRANSFER, amount, receiver);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. MISC
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Обрабатывает операцию, увеличивающую баланс счета (пополнение, зачисление).
     * @param holder владелец счета
     * @param operationType тип операции (DEPOSIT или CREDIT)
     * @param amount сумма операции
     * @return статус выполнения операции
     */
    private OperationStatus processIncreasing(BankAccount holder, OperationType operationType, double amount, BankAccount beneficiary) {
        // Обработка транзакции
        Transaction transaction = new Transaction(holder, operationType, amount, beneficiary);
        transaction.setStatus(OperationStatus.COMMITTED); // Проверки не требуются, транзакция успешна

        // Добавление транзакции в список владельца
        holder.addTransaction(transaction);

        return transaction.getStatus();
    }

    /**
     * Обрабатывает операцию, уменьшающую баланс счета (списание, перевод, снятие).
     * @param holder владелец счета
     * @param operationType тип операции (DEBIT, WITHDRAW или TRANSFER)
     * @param amount сумма операции
     * @param beneficiary бенефициар
     * @return статус выполнения операции
     */
    private OperationStatus processReducing(BankAccount holder, OperationType operationType, double amount, BankAccount beneficiary) {
        OperationStatus status;

        // Обработка транзакции
        Transaction transaction = new Transaction(holder, operationType, amount, beneficiary);

        if (holder.getBalance() >= amount) {
            if (operationType.hasBeneficiary()) {
                status = credit(beneficiary, amount, holder);
            } else {
                status = OperationStatus.COMMITTED;
            }
        } else {
            status = OperationStatus.CANCELED;
        }

        transaction.setStatus(status);

        // Добавление транзакции в список владельца
        holder.addTransaction(transaction);

        return transaction.getStatus();
    }
}