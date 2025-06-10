package ru.gritsenkodaniil.bankaccountmanagement;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Класс, представляющий банковский счет.
 * Содержит информацию о владельце, номере счета и списке транзакций.
 */
public class BankAccount {
    private final long accountNumber;
    private String holderName;
    private final ArrayList<Transaction> transactions = new ArrayList<>();

    // -----------------------------------------------------------------------------------------------------------------
    // OVERRIDDEN
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Возвращает строковое представление счета.
     * @return строка в формате "Имя владельца (№НомерСчета)"
     */
    @Override
    public String toString() {
        return MessageFormat.format("{0} (№{1})", holderName, Long.toString(accountNumber));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Создает новый банковский счет.
     * @param accountNumber номер счета
     * @param ownerName имя владельца
     */
    public BankAccount(long accountNumber, String ownerName) {
        this.accountNumber = accountNumber;
        this.holderName = ownerName;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // GETTERS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Возвращает номер счета.
     * @return номер счета
     */
    public long getAccountNumber() {
        return accountNumber;
    }

    /**
     * Возвращает имя владельца счета.
     * @return имя владельца
     */
    public String getHolderName() {
        return holderName;
    }

    /**
     * Возвращает копию списка транзакций.
     * @return список транзакций
     */
    public ArrayList<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // SETTERS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Устанавливает имя владельца счета.
     * @param holderName новое имя владельца
     */
    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    /**
     * Добавляет транзакцию в список.
     * @param transaction транзакция для добавления
     */
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. OPERATIONS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Выполняет транзакцию.
     * @param operationType тип операции
     * @param amount сумма
     * @param beneficiary бенефициар (может быть null)
     * @return статус выполнения операции
     */
    private OperationStatus executeTransaction(OperationType operationType, double amount, BankAccount beneficiary) {
        Transaction transaction = new Transaction(this, operationType, amount, beneficiary);
        transaction.execute();
        return transaction.getStatus();
    }

    /**
     * Выполняет операцию пополнения счета.
     * @param amount сумма пополнения
     * @return статус выполнения операции
     */
    public OperationStatus deposit(double amount) {
        return executeTransaction(OperationType.DEPOSIT, amount, null);
    }

    /**
     * Выполняет операцию зачисления средств от отправителя.
     * @param amount сумма
     * @param sender отправитель средств
     * @return статус выполнения операции
     */
    public OperationStatus credit(double amount, BankAccount sender) {
        return executeTransaction(OperationType.CREDIT, amount, sender);
    }

    /**
     * Выполняет операцию списания средств в пользу бенефициара.
     * @param amount сумма
     * @param beneficiary бенефициар
     * @return статус выполнения операции
     */
    public OperationStatus debit(double amount, BankAccount beneficiary ) {
        return executeTransaction(OperationType.DEBIT, amount, beneficiary);
    }

    /**
     * Выполняет операцию снятия наличных.
     * @param amount сумма
     * @return статус выполнения операции
     */
    public OperationStatus withdrawal(double amount) {
        return executeTransaction(OperationType.WITHDRAW, amount, null);
    }

    /**
     * Выполняет операцию перевода средств.
     * @param amount сумма
     * @param receiver получатель
     * @return статус выполнения операции
     */
    public OperationStatus transfer(double amount, BankAccount receiver) {
        return executeTransaction(OperationType.TRANSFER, amount, receiver);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. GETTING DATA
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Рассчитывает текущий баланс счета.
     * @return текущий баланс
     */
    public double getBalance() {
        double balance = 0;

        for (Transaction transaction : transactions) {
            // Учитываются только подтвержденные транзакции
            if (transaction.getStatus().isCommitted()) {
                if (transaction.getOperationType().isAddition()) {
                    balance += transaction.getAmount();
                } else {
                    balance -= transaction.getAmount();
                }
            }
        }

        return balance;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. PRINT
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Выводит текущий баланс счета.
     */
    public void printBalance() {
        double balance = getBalance();
        String message = MessageFormat.format("Владелец: {0}, баланс: {1}", this, balance);
        System.out.println(message);
    }

    /**
     * Выводит список всех транзакций по счету.
     */
    public void printTransactions() {
        String title = MessageFormat.format("Владелец: {0}, транзакции: ", this);
        System.out.println(title);

        int i = 1;

        for (Transaction transaction : transactions) {
            //String successView = transaction.getStatus().getTitle();
            String operationView = transactionOperationView(transaction);

            String transactionInfo = MessageFormat.format(
                    "   {0} - {1}, операция: {2}, сумма {3}",
                    i, transaction, operationView, transaction.getAmount()
            );
            System.out.println(transactionInfo);
            i++;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. MISC
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Формирует строковое представление операции транзакции.
     * @param transaction транзакция
     * @return строковое представление операции
     */
    private String transactionOperationView(Transaction transaction) {
        String operationView = transaction.getOperationType().getTitle();

        String extOperationViewTemplate = switch (transaction.getOperationType()) {
            case CREDIT -> "{0} (от: {1})";
            case DEBIT -> "{0} (получатель: {1})";
            case TRANSFER -> "{0} (кому: {1})";
            default -> "";
        };

        if (!extOperationViewTemplate.isBlank()) {
            operationView = MessageFormat.format(
                    extOperationViewTemplate,
                    operationView, transaction.getBeneficiary());
        }

        return operationView;
    }
}