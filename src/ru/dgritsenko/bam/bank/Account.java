package ru.dgritsenko.bam.bank;

import java.lang.annotation.IncompleteAnnotationException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс, представляющий банковский счет.
 * Содержит информацию о владельце счета, номере счета, списке транзакций и балансе.
 * Поддерживает операции по работе с балансом и транзакциями.
 */
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

    /**
     * Возвращает строковое представление счета в формате: "Имя владельца (№НомерСчета)".
     *
     * @return строковое представление счета
     */
    @Override
    public String toString() {
        return MessageFormat.format("{0} (№{1})", holderName, Long.toString(accountNumber));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Конструктор создает новый счет с указанным именем владельца.
     * Номер счета генерируется случайным образом.
     *
     * @param holderName ФИО владельца счета
     */
    public Account(String holderName) {
        if (!holderNameIsCorrect()) {

        }

        this.accountNumber = getGeneratedAccountNumber();
        this.transactions = new ArrayList<>();

        setHolderName(holderName);
    }

    /**
     * Конструктор создает новый счет с указанным номером счета и именем владельца.
     *
     * @param accountNumber номер счета
     * @param holderName ФИО владельца счета
     */
    public Account(long accountNumber, String holderName) {
        if (!accountNumberIsCorrect()) {

        } else if (!holderNameIsCorrect()) {

        }

        this.accountNumber = accountNumber;
        this.transactions = new ArrayList<>();

        setHolderName(holderName);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // GETTERS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Возвращает номер счета.
     *
     * @return номер счета
     */
    public long getAccountNumber() {
        return accountNumber;
    }

    /**
     * Возвращает имя владельца счета.
     *
     * @return имя владельца
     */
    public String getHolderName() {
        return holderName;
    }

    /**
     * Возвращает копию списка транзакций по счету.
     *
     * @return список транзакций
     */
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // SETTERS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Устанавливает новое имя владельца счета.
     *
     * @param holderName новое имя владельца
     */
    public void setHolderName(String holderName) {
        if (!holderNameIsCorrect()) {

        }

        this.holderName = holderName;
        formatHolderName();
    }

    /**
     * Добавляет новую транзакцию в список транзакций счета.
     *
     * @param transaction транзакция для добавления
     */
    public void addTransaction(Transaction transaction) {
        if (transaction == null) {
            
        } else if (transactions.contains(transaction)) {
            
        }

        transactions.add(transaction);

        // При обновлении списка транзакций требуется пересчет кэша баланса
        balanceIsValid = false;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. CHECKS
    // -----------------------------------------------------------------------------------------------------------------

    public boolean accountNumberIsCorrect() {
        return accountNumber >= 100_000_000 && accountNumber < 1_000_000_000;
    }

    public boolean holderNameIsCorrect() {
        Pattern pattern = Pattern.compile("^[A-z][A-z]+(['-][A-z][A-z]+)* [A-z]$");
        Matcher matcher = pattern.matcher(holderName.strip());

        return matcher.matches();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. GETTING DATA
    // -----------------------------------------------------------------------------------------------------------------

    public long getGeneratedAccountNumber() {
        Random random = new Random();

        return 100_000_000 + random.nextLong(900_000_001);
    }

    /**
     * Вычисляет текущий баланс счета на основе подтвержденных транзакций.
     * Использует кэширование для оптимизации.
     *
     * @return текущий баланс счета
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
                    balance += transaction.getTransactionType().isAddition() ? transactionAmount : -transactionAmount;
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
     * Выводит на экран информацию о балансе счета.
     */
    public void printBalance() {
        double balance = getBalance();
        String message = MessageFormat.format("Счет: {0}, баланс: {1}", this, balance);
        System.out.println(message);
    }

    /**
     * Выводит на экран список всех транзакций по счету.
     */
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

    private void formatHolderName() {
        String strippedLowerCaseName = holderName.strip().toLowerCase();
        int nameLength = strippedLowerCaseName.length();

        holderName = strippedLowerCaseName.substring(0, 1).toUpperCase()
                + strippedLowerCaseName.substring(1, nameLength - 2)
                + " "
                + strippedLowerCaseName.substring(nameLength - 1).toUpperCase();
    }

    /**
     * Форматирует строковое представление операции транзакции в зависимости от её типа.
     * Добавляет дополнительную информацию для операций CREDIT, DEBIT и TRANSFER.
     *
     * @param transaction транзакция для форматирования
     * @return строковое представление операции
     */
    private String transactionOperationView(Transaction transaction) {
        String operationView = transaction.getTransactionType().getTitle();

        String extOperationViewTemplate = switch (transaction.getTransactionType()) {
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