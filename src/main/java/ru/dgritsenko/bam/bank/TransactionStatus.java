package ru.dgritsenko.bam.bank;

/**
 * Перечисление, представляющее возможные статусы транзакции.
 */
public enum TransactionStatus {
    UNCOMMITTED("Не подтверждена"),
    COMMITTED("Подтверждена"),
    CANCELED("Отменена");

    private final String title;

    // -----------------------------------------------------------------------------------------------------------------
    // OVERRIDDEN
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Возвращает строковое представление статуса транзакции.
     *
     * @return строковое представление статуса транзакции
     */
    @Override
    public String toString() {
        return getTitle();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Конструктор перечисления.
     *
     * @param title название статуса транзакции
     */
    TransactionStatus(String title) {
        this.title = title;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // GETTERS
    // -----------------------------------------------------------------------------------------------------------------

    public String getTitle() {
        return title;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. GETTING DATA
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Проверяет, является ли текущий статус статусом подтверждения.
     *
     * @return {@code true}, если текущий статус {@link TransactionStatus#COMMITTED}
     */
    public boolean isCommitted() {
        return this == COMMITTED;
    }
}