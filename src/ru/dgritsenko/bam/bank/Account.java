package ru.dgritsenko.bam.bank;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Random;

public class Account {
    private final long accountNumber;
    private String holderName;
    private final ArrayList<Transaction> transactions;

    // Кэширование баланса
    private double cachedBalance;
    private boolean balanceIsValid;

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

    public Account(String holderName) {
        Random random = new Random();

        this.transactions = new ArrayList<>();
        this.accountNumber = random.nextInt(999999999);
        this.holderName = holderName;
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
        transactions.add(transaction);

        // При обновлении списка транзакций требуется пересчет кэша баланса
        balanceIsValid = false;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. GETTING DATA
    // -----------------------------------------------------------------------------------------------------------------

    public double getBalance() {
        double balance = 0;

        if (balanceIsValid) {
            // Если данные актуальны, используется кэшированный баланс
            balance = cachedBalance;
        } else {
            // Если данные неактуальны, баланс пересчитывается
            for (Transaction transaction : transactions) {
                // Учитываются только подтвержденные транзакции
                if (transaction.getStatus().isCommitted()) {
                    double transactionAmount = transaction.getAmount();
                    balance += transaction.getOperationType().isAddition() ? transactionAmount : -transactionAmount;
                }
            }
            cachedBalance = balance;
            balanceIsValid = true;
        }

        return balance;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. PRINT
    // -----------------------------------------------------------------------------------------------------------------

    public void printBalance() {
        double balance = getBalance();
        String message = MessageFormat.format("Счет: {0}, баланс: {1}", this, balance);
        System.out.println(message);
    }

    public void printTransactions() {
        String message;

        if (transactions.isEmpty()) {
            message = MessageFormat.format("Счет: {0}, список транзакций пуст...", this);
        } else {
            StringBuilder messages = new StringBuilder();

            String title = MessageFormat.format("Счет: {0}, транзакции:\n", this);
            messages.append(title);

            int i = 1;

            for (Transaction transaction : transactions) {
                String operationView = transactionOperationView(transaction);

                String transactionInfoTemplate = (i == 1)
                        ? "\t{0} - {1}, операция: {2}, сумма {3}"
                        : "\n\t{0} - {1}, операция: {2}, сумма {3}";
                String transactionInfo = MessageFormat.format(transactionInfoTemplate,
                        i, transaction, operationView, transaction.getAmount()
                );
                messages.append(transactionInfo);
                i++;
            }

            message = messages.toString();
        }

        System.out.println(message);
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
                    operationView, transaction.getToAccount());
        }

        return operationView;
    }
}