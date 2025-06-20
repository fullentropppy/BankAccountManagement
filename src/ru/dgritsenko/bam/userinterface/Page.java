package ru.dgritsenko.bam.userinterface;

import ru.dgritsenko.bam.main.BankService;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Page {
    protected final ConsoleService consoleService;

    private final List<Integer> validOptions = new ArrayList<>();

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    public Page(ConsoleService consoleService) {
        this.consoleService = consoleService;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. OUTPUT
    // -----------------------------------------------------------------------------------------------------------------

    public abstract void show();

    protected void setTitle(String title) {
        clearText();
        printNewPageHeader(title);
    }

    protected void setMenu(String menu) {
        System.out.println(menu);

        // Обновление списка доступных опций
        validOptions.clear();

        Pattern pattern = Pattern.compile("\\d+(?=\\.)");
        Matcher matcher = pattern.matcher(menu);

        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group());
            validOptions.add(value);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. INPUT
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Запрашивает у пользователя ввод суммы (положительное число) через консоль.
     *
     * @param actionTitle подсказка для ввода (например, "Введите сумму")
     * @return введенная пользователем сумма (больше 0)
     */
    protected double getAmount(String actionTitle) {
        double amount = 0;

        String actionMessage = MessageFormat.format("\n> {0}: ", actionTitle);
        Scanner scanner = new Scanner(System.in);

        while (amount <= 0) {
            System.out.print(actionMessage);
            amount = scanner.nextDouble();
        }

        return amount;
    }

    /**
     * Запрашивает у пользователя выбор пункта меню и проверяет его корректность.
     *
     * @param actionTitle подсказка для ввода (например, "Введите номер пункта")
     * @return выбранный пользователем пункт меню
     */
    protected int getOptionFromMenu(String actionTitle) {
        int option = -1;

        String actionMessage = MessageFormat.format("\n> {0}: ", actionTitle);
        Scanner scanner = new Scanner(System.in);

        while (!validOptions.contains(option)) {
            System.out.print(actionMessage);
            option = scanner.nextInt();
        }

        return option;
    }

    /**
     * Приостанавливает выполнение программы до нажатия Enter пользователем.
     *
     * @param actionTitle сообщение, отображаемое перед ожиданием ввода
     */
    protected void waitForInputToContinue(String actionTitle) {
        String pageActionMessage = MessageFormat.format("\n> {0}...", actionTitle);
        System.out.print(pageActionMessage);

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. CONSOLE. PRINTING
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Очищает консоль и выводит заголовок страницы с указанным названием.
     *
     * @param title заголовок страницы
     */
    private void printNewPageHeader(String title) {
        clearText();

        String header = MessageFormat.format(
                """
                ====================================================================================================
                \tБанковское приложение / {0}
                ====================================================================================================""",
                title);
        System.out.println(header);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. CONSOLE. MISC
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Очищает консоль.
     */
    protected void clearText() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException _) {}
    }
}