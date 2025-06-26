package ru.dgritsenko.data;

import ru.dgritsenko.bank.Account;

import java.util.List;

public interface DataStorage {
    void saveAccounts(List<Account> accounts);
    List<Account> loadedAccounts();
}