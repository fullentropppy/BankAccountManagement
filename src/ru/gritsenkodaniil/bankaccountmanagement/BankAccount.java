package ru.gritsenkodaniil.bankaccountmanagement;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Класс, представляющий банковский счет.
 * Содержит информацию о владельце, номере счета, балансе и списке транзакций.
 */
public class BankAccount {
    private final long accountNumber;
    private String holderName;
    private final ArrayList<Transaction> transactions = new ArrayList<>();

    // Кэширование баланса
    private double cachedBalance;
    boolean balanceIsValid;

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
        transactions.add(transaction);

        // При обновлении списка транзакций требуется пересчет кэша баланса
        balanceIsValid = false;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. GETTING DATA
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Возвращает текущий баланс счета.
     * @return текущий баланс
     */
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