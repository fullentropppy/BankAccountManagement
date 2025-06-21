package ru.dgritsenko.bam.bank;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для управления банковскими счетами и транзакциями.
 */
public class BankService {
    private final List<Account> accounts = new ArrayList<>();

    /**
     * Создаёт новый банковский счет с указанным владельцем.
     *
     * @param holderName ФИО владельца счёта (не может быть пустым).
     * @return Созданный объект
     */
    public Account createAccount(String holderName) {
        Account account = new Account(holderName);
        accounts.add(account);
        return account;
    }

    /**
     * Возвращает список всех счетов.
     *
     * @return список счетов.
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * Выполняет операцию, не требующую указания счета получателя.
     *
     * @param transactionType тип операции (DEPOSIT или WITHDRAW)
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
     * @param transactionType тип операции (CREDIT или TRANSFER)
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