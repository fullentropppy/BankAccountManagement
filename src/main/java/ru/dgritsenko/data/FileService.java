package ru.dgritsenko.data;

import ru.dgritsenko.bank.Account;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class FileService implements DataStorage {
    private static final String DATA_DIR;
    private static final String ACCOUNTS_DIR;

    static {
        String homeDir = System.getProperty("user.home") + File.separator + "Documents";
        DATA_DIR = homeDir + File.separator + "BAM" + File.separator + "Data";
        ACCOUNTS_DIR = DATA_DIR + File.separator + "accounts.data";
    }

    @Override
    public void saveAccounts(List<Account> accounts) {
        printProgress("Сохранение данных");

        try {
            File dir = new File(DATA_DIR);
            boolean isDataMightBeSaved = true;

            if (!dir.exists()) {
                isDataMightBeSaved = dir.mkdirs();
            }

            if (isDataMightBeSaved) {
                FileOutputStream fos = new FileOutputStream(ACCOUNTS_DIR);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(accounts);
                printResult("Данные сохранены: " + DATA_DIR);
            } else {
                printError("Ошибка сохранения данных", DATA_DIR + " (Системе не удалось получить каталог для сохранения)");
            }
        } catch (Exception exception) {
            printError("Ошибка сохранения данных", exception.getMessage());
        }
    }

    @Override
    public List<Account> loadedAccounts() {
        List<Account> accounts = new ArrayList<>();
        printProgress("Загрузка данных из: " + DATA_DIR);

        try {
            Path path = Paths.get(DATA_DIR);
            if (Files.exists(path) && Files.isReadable(path)) {
                FileInputStream fis = new FileInputStream(ACCOUNTS_DIR);
                ObjectInputStream ois = new ObjectInputStream(fis);
                accounts = (ArrayList<Account>) ois.readObject();
            } else {
                printResult("Данные не найдены, новые данные будут записаны при выходе из приложения");
            }
        } catch (Exception exception) {
            printError("Ошибка загрузки данных", exception.getMessage());
        }

        return accounts;
    }

    private static void printProgress(String msg) {
        String progressMsg = MessageFormat.format("- {0}...", msg);
        System.out.println(msg);
    }

    private static void printResult(String msg) {
        String resultMsg = MessageFormat.format("- {0}", msg);
        System.out.println(resultMsg);
    }

    private static void printError(String title, String error) {
        String errorMsg = MessageFormat.format("\n! {0}: {1}", title, error);
        System.out.println(errorMsg);
    }
}