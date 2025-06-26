package ru.dgritsenko.bank;

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
 * <p>Содержит информацию о владельце счета, номере счета, списке транзакций и балансе.
 * <p>Создание объекта выполняется через {@link Builder}.
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
     * Служебный конструктор для создания объекта через {@link Builder}.
     *
     * @param builder статический вложенный класс-источник данных для заполнения
     */
    private Account(Builder builder) {
        this.accountNumber = builder.accountNumber;
        this.holderName = builder.holderName;
        this.transactions = builder.transactions;
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

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // SETTERS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Устанавливает имя владельца счета.
     *
     * @param holderName имя владельца счета (фамилия и первая буква имени на латинице)
     *
     * @throws NullPointerException если {@code holderName} равен {@code null}
     * @throws IllegalArgumentException если {@code holderName} имеет неверный формат
     */
    public void setHolderName(String holderName) {
        this.holderName = validHolderName(holderName);
    }

    /**
     * Добавляет транзакцию в список транзакций счета.
     *
     * @param transaction транзакция для добавления
     *
     * @throws NullPointerException если {@code transaction} равен {@code null}
     * @throws IllegalArgumentException если {@code transaction} уже существует в списке
     */
    public void addTransaction(Transaction transaction) {
        // Проверка на null
        Objects.requireNonNull(transaction, "Транзакция не должна быть null");

        // Проверка на уникальность транзакции
        if (transactions.contains(transaction)) {
            String errMsg = MessageFormat.format(
                    "Транзакция не уникальна \"{0}\": " +
                            "транзакция уже есть в списке транзакций счета \"{1}\"",
                    transaction.getUuid(), this
            );
            throw new IllegalArgumentException(errMsg);
        }

        transactions.add(transaction);

        // При обновлении списка транзакций требуется пересчет кэша баланса
        balanceIsValid = false;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. GETTING OTHER DATA
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Возвращает текущий баланс счета, вычисляя его на основе подтвержденных транзакций.
     * <p>
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
    // METHODS. STATIC VALIDATION
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Проверяет номера счета на корректность.
     *
     * @param accountNumber номер счета для проверки
     *
     * @return {@code true} если номер корректен (в диапазоне 100000000-999999999)
     */
    private static boolean isAccountNumberCorrect(long accountNumber) {
        return accountNumber >= 100_000_000 && accountNumber < 1_000_000_000;
    }

    /**
     * Проверяет номер счета на корректность и возвращает его, если он корректен.
     *
     * @param accountNumber номер счета для проверки
     *
     * @return корректный номер счета
     *
     * @throws IllegalArgumentException если {@code accountNumber} не соответствует формату
     */
    private static long validAccountNumber(long accountNumber) {
        // Проверка на формат
        if (!isAccountNumberCorrect(accountNumber)) {
            String errMsg = MessageFormat.format(
                    "Некорректный номер счета \"{0}\": " +
                            "Номер счета должен быть в диапазоне от 100 000 000 до 999 999 999",
                    accountNumber
            );
            throw new IllegalArgumentException(errMsg);
        }

        return accountNumber;
    }

    /**
     * Проверяет имя владельца номера счета на корректность.
     *
     * @param holderName имя для проверки
     *
     * @return {@code true} если имя соответствует формату (фамилия и первая буква имени на латинице)
     */
    private static boolean isHolderNameCorrect(String holderName) {
        boolean isCorrect;

        if (holderName == null) {
            isCorrect = false;
        } else {
            Pattern pattern = Pattern.compile("^[A-z][A-z]+ [A-z]$");
            Matcher matcher = pattern.matcher(holderName.strip());
            isCorrect = matcher.matches();
        }

        return isCorrect;
    }

    /**
     * Проверяет имя владельца на корректность и возвращает отформатированное имя, если оно корректно.
     *
     * @param holderName имя владельца для проверки
     *
     * @return корректное отформатированное имя владельца
     *
     * @throws NullPointerException если {@code holderName} равно {@code null}
     *
     * @throws IllegalArgumentException если {@code holderName} не соответствует формату
     */
    private static String validHolderName(String holderName) {
        // Проверка на null
        Objects.requireNonNull(holderName, "Имя владельца не должно быть null");

        // Проверка на формат
        if (!isHolderNameCorrect(holderName)) {
            String errMsg = MessageFormat.format(
                    "Некорректное имя владельца \"{0}\": " +
                    "имя владельца должно состоять из фамилии и первой буквы имени на латинице",
                    holderName
            );
            throw new IllegalArgumentException(errMsg);
        }

        return getFormattedHolderName(holderName);
    }

    /**
     * Проверяет список транзакций на корректность.
     *
     * @param transactions список транзакций для проверки
     *
     * @return корректный список транзакций
     *
     * @throws NullPointerException если одно из значений {@code transactions} равно {@code null}
     *
     * @throws IllegalArgumentException если в {@code transactions} есть неуникальные значения
     */
    private static List<Transaction> validTransactions(List<Transaction> transactions) {
        int currentIndex = 0;

        for (Transaction transaction : transactions) {
            // Проверка на null
            Objects.requireNonNull(transaction, "В списке транзакций не должно быть null");

            // Проверка на уникальность транзакций в списке
            int lastTransactionIndex = transactions.lastIndexOf(transaction);

            if(currentIndex != lastTransactionIndex) {
                throw new IllegalArgumentException("Некорректный список транзакций: список транзакций содержит дубли");
            }
        }

        return transactions;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. STATIC WORK WITH FIELD DATA
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Генерирует случайный номер счета в диапазоне 100000000-999999999.
     *
     * @return сгенерированный номер счета
     */
    public static long getGeneratedAccountNumber() {
        return 100_000_000 + ThreadLocalRandom.current().nextLong(900_000_001);
    }

    /**
     * Возвращает отформатированное имя владельца:
     * первая буква фамилии и имени в верхнем регистре, остальные в нижнем.
     *
     * @param holderName исходное имя владельца
     *
     * @return отформатированное имя владельца
     */
    public static String getFormattedHolderName(String holderName) {
        String strippedLowerCaseName = holderName.strip().toLowerCase();
        int nameLength = strippedLowerCaseName.length();

        return strippedLowerCaseName.substring(0, 1).toUpperCase()
                + strippedLowerCaseName.substring(1, nameLength - 2)
                + " "
                + strippedLowerCaseName.substring(nameLength - 1).toUpperCase();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // BUILDER NESTED CLASS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Вложенный статичный класс, представляющий построитель родительского класса {@link Account}.
     * <p>Содержит поля идентичные полям родительского класса.
     * Каждое поле имеет set-метод для установки значения.
     */
    public static class Builder {
        private long accountNumber;
        private String holderName;
        private List<Transaction> transactions;

        // -------------------------------------------------------------------------------------------------------------
        // BUILDER. CONSTRUCTORS
        // -------------------------------------------------------------------------------------------------------------

        /**
         * Создает построитель для последующего создания основного класса {@link Account}.
         */
        public Builder() {
            super();
        }

        // -------------------------------------------------------------------------------------------------------------
        // BUILDER. SETTERS
        // -------------------------------------------------------------------------------------------------------------

        public Builder setAccountNumber(long accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public Builder setHolderName(String holderName) {
            this.holderName = holderName;
            return this;
        }

        public Builder setTransactions(List<Transaction> transactions) {
            this.transactions = transactions;
            return this;
        }

        public Builder addTransaction(Transaction transaction) {
            transactions.add(transaction);
            return this;
        }

        // -------------------------------------------------------------------------------------------------------------
        // BUILDER. BUILDING
        // -------------------------------------------------------------------------------------------------------------

        /**
         * Валидирует значения полей и создает экземпляр основного класса {@link Account}.
         *
         * @return новая транзакция
         */
        public Account build() {
            validate();
            return new Account(this);
        }

        /**
         * Создает без валидации экземпляр основного класса {@link Account}.
         *
         * @return новая транзакция
         */
        public Account buildWithoutValidations() {
            return new Account(this);
        }

        // -------------------------------------------------------------------------------------------------------------
        // BUILDER. MISC
        // -------------------------------------------------------------------------------------------------------------

        /**
         * Валидирует результат заполнения полей построителя.
         */
        private void validate() {
            // Проверка на null с установкой значений по умолчанию при необходимости
            accountNumber = accountNumber == 0
                    ? getGeneratedAccountNumber()
                    : validAccountNumber(accountNumber);

            transactions = transactions == null
                    ? new ArrayList<>()
                    : validTransactions(transactions);

            // Расширенные проверки
            holderName = validHolderName(holderName);
        }
    }
}