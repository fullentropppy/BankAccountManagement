package ru.gritsenkodaniil.bankaccountmanagement;

import java.text.MessageFormat;
import java.util.ArrayList;

public class BankAccount {
    private final long accountNumber;
    private String holderName;
    private final ArrayList<Transaction> transactions = new ArrayList<>();

    // -----------------------------------------------------------------------------------------------------------------
    // OVERRIDDEN
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return MessageFormat.format("{0} (№{1})", holderName, Long.toString(accountNumber));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    public BankAccount(long accountNumber, String ownerName) {
        this.accountNumber = accountNumber;
        this.holderName = ownerName;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // GETTERS
    // -----------------------------------------------------------------------------------------------------------------

    public long getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public ArrayList<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // SETTERS
    // -----------------------------------------------------------------------------------------------------------------

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. OPERATIONS
    // -----------------------------------------------------------------------------------------------------------------

    private OperationStatus executeTransaction(OperationType operationType, double amount, BankAccount beneficiary) {
        Transaction transaction = new Transaction(this, operationType, amount, beneficiary);
        transaction.execute();
        return transaction.getStatus();
    }

    public OperationStatus deposit(double amount) {
        return executeTransaction(OperationType.DEPOSIT, amount, null);
    }

    public OperationStatus credit(double amount, BankAccount sender) {
        return executeTransaction(OperationType.CREDIT, amount, sender);
    }

    public OperationStatus debit(double amount, BankAccount beneficiary ) {
        return executeTransaction(OperationType.DEBIT, amount, beneficiary);
    }

    public OperationStatus withdrawal(double amount) {
        return executeTransaction(OperationType.WITHDRAW, amount, null);
    }

    public OperationStatus transfer(double amount, BankAccount receiver) {
        return executeTransaction(OperationType.TRANSFER, amount, receiver);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. GETTING DATA
    // -----------------------------------------------------------------------------------------------------------------

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

    public void printBalance() {
        double balance = getBalance();
        String message = MessageFormat.format("Владелец: {0}, баланс: {1}", this, balance);
        System.out.println(message);
    }

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