package ru.dgritsenko.bam.bank;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для управления банковскими счетами и транзакциями.
 */
public class BankService {
    private final List<Account> accounts = new ArrayList<>();

    public Account createAccount(String holderName) {
        Account account = new Account(holderName);
        accounts.add(account);
        return account;
    }

    /**
     * Возвращает копию списка всех счетов.
     *
     * @return список счетов.
     */
    public List<Account> getAccounts() {
        return List.copyOf(accounts);
    }

    /**
     * Выполняет операцию, не требующую указания счета получателя.
     *
     * @param transactionType тип операции ({@code DEPOSIT} или {@code WITHDRAW})
     * @param fromAccount счет отправителя
     * @param amount сумма операции
     * @return статус выполненной операции
     */
    public TransactionStatus performTransaction(
            TransactionType transactionType,
            Account fromAccount,
            double amount)
    {
        return TransactionService.perform(transactionType, fromAccount, amount);
    }

    /**
     * Выполняет операцию, требующую указания счета получателя.
     *
     * @param transactionType тип операции ({@code CREDIT} или {@code TRANSFER})
     * @param fromAccount счет отправителя
     * @param amount сумма операции
     * @param toAccount счет получателя
     * @return статус выполненной операции
     */
    public TransactionStatus performTransaction(
            TransactionType transactionType,
            Account fromAccount,
            double amount,
            Account toAccount)
    {
        return TransactionService.perform(transactionType, fromAccount, amount, toAccount);
    }
}