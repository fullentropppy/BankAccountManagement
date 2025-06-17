package ru.dgritsenko.bam.bank;

public class TransactionProcessor {
    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. OPERATIONS
    // -----------------------------------------------------------------------------------------------------------------

    public static TransactionStatus deposit(Account fromAccount, double amount) {
        return processIncreasing(fromAccount, TransactionType.DEPOSIT, amount, null);
    }

    public static TransactionStatus credit(Account fromAccount, double amount, Account toAccount) {
        return processIncreasing(fromAccount, TransactionType.CREDIT, amount, toAccount);
    }

    public static TransactionStatus debit(Account fromAccount, double amount, Account toAccount ) {
        return processReducing(fromAccount, TransactionType.DEBIT, amount, toAccount);
    }

    public static TransactionStatus withdrawal(Account fromAccount, double amount) {
        return processReducing(fromAccount, TransactionType.WITHDRAW, amount, null);
    }

    public static TransactionStatus transfer(Account fromAccount, double amount, Account toAccount) {
        return processReducing(fromAccount, TransactionType.TRANSFER, amount, toAccount);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. MISC
    // -----------------------------------------------------------------------------------------------------------------

    private static TransactionStatus processIncreasing(
            Account fromAccount,
            TransactionType transactionType,
            double amount,
            Account toAccount) {

        // Обработка транзакции
        Transaction transaction = new Transaction(fromAccount, transactionType, amount, toAccount);
        transaction.setStatus(TransactionStatus.COMMITTED); // Проверки не требуются, транзакция успешна

        // Добавление транзакции в список владельца
        fromAccount.addTransaction(transaction);

        return transaction.getStatus();
    }

    private static TransactionStatus processReducing(
            Account fromAccount,
            TransactionType transactionType,
            double amount,
            Account toAccount) {

        TransactionStatus status;

        // Обработка транзакции
        Transaction transaction = new Transaction(fromAccount, transactionType, amount, toAccount);

        if (fromAccount.getBalance() >= amount) {
            if (transactionType.hasToAccount()) {
                status = credit(toAccount, amount, fromAccount);
            } else {
                status = TransactionStatus.COMMITTED;
            }
        } else {
            status = TransactionStatus.CANCELED;
        }

        transaction.setStatus(status);

        // Добавление транзакции в список владельца
        fromAccount.addTransaction(transaction);

        return transaction.getStatus();
    }
}