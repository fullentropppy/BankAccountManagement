package ru.dgritsenko.datastorage;

import ru.dgritsenko.bank.Account;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;

/**
 * Реализация интерфейса {@link DataStorage} для хранения данных в файловой системе.
 * <p>Предоставляет методы для сохранения и загрузки банковских счетов в/из файла.
 * Файл данных хранится в директории {@code Documents/BAM/Data/accounts.data}.
 */
public class FileService implements DataStorage {
    private static final String ACCOUNTS_PATH;

    static {
        String sep = File.separator;
        String homeDir = System.getProperty("user.home") + sep + "Documents";
        String dataDir = homeDir + sep + "BAM" + sep + "Data";
        ACCOUNTS_PATH = dataDir + sep + "accounts.data";
    }

    // -----------------------------------------------------------------------------------------------------------------
    // OVERRIDDEN
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Загружает список банковских счетов из файла.
     *
     * @return список загруженных счетов
     *
     * @throws IOException если произошла ошибка ввода-вывода при чтении файла
     * @throws ClassNotFoundException если класс объекта в файле не найден
     */
    @Override
    public List<Account> loadAccounts() throws IOException, ClassNotFoundException {
        return (List<Account>) loadObject(ACCOUNTS_PATH);
    }

    /**
     * Сохраняет список банковских счетов в файл.
     *
     * @param accounts список счетов для сохранения
     *
     * @throws IOException если произошла ошибка ввода-вывода при записи файла
     */
    @Override
    public void saveAccounts(List<Account> accounts) throws IOException {
        saveObject(accounts, ACCOUNTS_PATH);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. MISC
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Загружает объект из указанного файла.
     *
     * @param fullPath полный путь к файлу
     *
     * @return загруженный объект
     *
     * @throws IOException если произошла ошибка ввода-вывода при чтении файла
     * @throws ClassNotFoundException если класс объекта в файле не найден
     */
    public Object loadObject(String fullPath) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fullPath);
        ObjectInputStream ois = new ObjectInputStream(fis);
        return ois.readObject();
    }

    /**
     * Сохраняет объект в указанный файл.
     * <p>Создает все необходимые директории, если они не существуют.
     *
     * @param object объект для сохранения
     *
     * @param fullPath полный путь к файлу
     * @throws IOException если произошла ошибка ввода-вывода при записи файла
     *                     или если не удалось создать директории
     */
    public void saveObject(Object object, String fullPath) throws IOException {
        Path path = Paths.get(fullPath);
        Path parentPath = path.getParent();
        String dir = (parentPath != null) ? parentPath.toString() : "";

        boolean isDataMightBeSaved = true;
        File checkableDir = new File(dir);

        if (!checkableDir.exists()) {
            isDataMightBeSaved = checkableDir.mkdirs();
        }

        if (isDataMightBeSaved) {
            FileOutputStream fos = new FileOutputStream(fullPath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
        } else {
            String errMsg = MessageFormat.format("Не удалось создать каталог для записи данных: {0}", dir);
            throw new IOException(errMsg);
        }
    }
}