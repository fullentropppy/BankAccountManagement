package ru.dgritsenko.bam.bank;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
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
    private final List<Transaction> transactions;

    // Кэширование баланса
    private double cachedBalance;
    private boolean balanceIsValid;

    // -----------------------------------------------------------------------------------------------------------------
    // OVERRIDDEN
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Возвращает строковое представление счета в формате: "Имя владельца (№НомерСчета)".
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
     * Создает новый счет с автоматически сгенерированным номером.
     * @param holderName имя владельца счета (фамилия и первая буква имени на латинице)
     * @throws NullPointerException если holderName равен null
     * @throws IllegalArgumentException если holderName имеет неверный формат
     */
    public Account(String holderName) throws NullPointerException, IllegalArgumentException {
        // Присвоение значений с проверкой
        setHolderName(holderName);

        this.accountNumber = getGeneratedAccountNumber();
        this.holderName = holderName;
        this.transactions = new ArrayList<>();
    }

    /**
     * Создает новый счет с указанным номером.
     * @param accountNumber номер счета (должен быть в диапазоне 100000000-999999999)
     * @param holderName имя владельца счета (фамилия и первая буква имени на латинице)
     * @throws NullPointerException если holderName равен null
     * @throws IllegalArgumentException если accountNumber или holderName имеют неверный формат
     */
    public Account(long accountNumber, String holderName) throws NullPointerException, IllegalArgumentException {
        // Проверка параметров
        checkAccountNumber(accountNumber);

        // Присвоение значений с проверкой
        setHolderName(holderName);

        this.accountNumber = accountNumber;
        this.transactions = new ArrayList<>();
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
     * @return имя владельца счета
     */
    public String getHolderName() {
        return holderName;
    }

    /**
     * Возвращает неизменяемый список транзакций по счету.
     * @return неизменяемый список транзакций
     */
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    /**
     * Возвращает текущий баланс счета, вычисляя его на основе подтвержденных транзакций.
     * Использует кэширование для оптимизации.
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
    // SETTERS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Устанавливает имя владельца счета.
     * @param holderName имя владельца счета (фамилия и первая буква имени на латинице)
     * @throws NullPointerException если holderName равен null
     * @throws IllegalArgumentException если holderName имеет неверный формат
     */
    public void setHolderName(String holderName) throws NullPointerException, IllegalArgumentException {
        checkHolderName(holderName);
        formatHolderName();
    }

    /**
     * Добавляет транзакцию в список транзакций счета.
     * @param transaction транзакция для добавления
     * @throws NullPointerException если transaction равен null
     * @throws IllegalArgumentException если транзакция уже существует в списке
     */
    public void addTransaction(Transaction transaction) {
        // Проверка на null
        Objects.requireNonNull(transaction, "Транзакция не должна быть null");

        // Проверка на уникальность транзакции
        if (transactions.contains(transaction)) {
            String exceptionMessageTemplate =
                    "Транзакция не уникальна \"{0}\": " +
                    "транзакция уже есть в списке транзакций счета \"{1}\"";
            String exceptionMessage = MessageFormat.format(exceptionMessageTemplate, transaction.getUuid(), this);
            throw new IllegalArgumentException(exceptionMessage);
        }

        transactions.add(transaction);

        // При обновлении списка транзакций требуется пересчет кэша баланса
        balanceIsValid = false;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. CHECKS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Проверяет корректность номера счета.
     * @param accountNumber номер счета для проверки
     * @return true если номер корректен (в диапазоне 100000000-999999999)
     */
    public boolean accountNumberIsCorrect(long accountNumber) {
        return accountNumber >= 100_000_000 && accountNumber < 1_000_000_000;
    }

    /**
     * Проверяет корректность имени владельца счета.
     * @param holderName имя для проверки
     * @return true если имя соответствует формату (фамилия и первая буква имени на латинице)
     */
    public boolean holderNameIsCorrect(String holderName) {
        Pattern pattern = Pattern.compile("^[A-z][A-z]+ [A-z]$");
        Matcher matcher = pattern.matcher(holderName.strip());

        return matcher.matches();
    }

    /**
     * Проверяет номер счета и генерирует исключение, если он некорректен.
     * @param accountNumber номер счета для проверки
     * @throws IllegalArgumentException если номер счета не в диапазоне 100000000-999999999
     */
    private void checkAccountNumber(long accountNumber) {
        // Проверка на формат
        if (!accountNumberIsCorrect(accountNumber)) {
            String exceptionMessageTemplate =
                    "Некорректный номер счета \"{0}\": " +
                    "Номер счета должен быть в диапазоне от 100 000 000 до 999 999 999";
            String exceptionMessage = MessageFormat.format(exceptionMessageTemplate, accountNumber);
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    /**
     * Проверяет имя владельца и генерирует исключение, если оно некорректно.
     * @param holderName имя для проверки
     * @throws NullPointerException если holderName равен null
     * @throws IllegalArgumentException если имя не соответствует формату
     */
    private void checkHolderName(String holderName) {
        // Проверка на null
        Objects.requireNonNull(holderName, "Имя владельца не должно быть null");

        // Проверка на формат
        if (!holderNameIsCorrect(holderName)) {
            String exceptionMessageTemplate =
                    "Некорректное имя владельца \"{0}\": " +
                    "имя владельца должно состоять из фамилии и первой буквы имени на латинице";
            String exceptionMessage = MessageFormat.format(exceptionMessageTemplate, holderName);
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. WORKING WITH FIELDS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Генерирует случайный номер счета в диапазоне 100000000-999999999.
     * @return сгенерированный номер счета
     */
    private long getGeneratedAccountNumber() {
        return 100_000_000 + ThreadLocalRandom.current().nextLong(900_000_001);
    }

    /**
     * Форматирует имя владельца счета: первая буква фамилии и имени в верхнем регистре,
     * остальные буквы фамилии в нижнем регистре.
     */
    private void formatHolderName() {
        String strippedLowerCaseName = holderName.strip().toLowerCase();
        int nameLength = strippedLowerCaseName.length();

        holderName = strippedLowerCaseName.substring(0, 1).toUpperCase()
                + strippedLowerCaseName.substring(1, nameLength - 2)
                + " "
                + strippedLowerCaseName.substring(nameLength - 1).toUpperCase();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. PRINT
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Выводит информацию о текущем балансе счета.
     */
    public void printBalance() {
        double balance = getBalance();
        String message = MessageFormat.format("Счет: {0}, баланс: {1}", this, balance);
        System.out.println(message);
    }

    /**
     * Выводит в консоль список всех транзакций по счету.
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

    /**
     * Формирует строковое представление операции транзакции.
     * @param transaction транзакция для обработки
     * @return строковое представление операции
     */
    private String transactionOperationView(Transaction transaction) {
        String operationView = transaction.getTransactionType().getTitle();

        String extOperationViewTemplate = switch (transaction.getTransactionType()) {
            case CREDIT -> "{0} (от: {1})";
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