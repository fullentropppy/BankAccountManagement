package ru.dgritsenko.bam.userinterface;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Абстрактный класс, представляющий базовую страницу интерфейса.
 * Содержит общие методы для работы с консольным вводом/выводом.
 */
public abstract class Page {
    protected final ConsoleService consoleService;

    private final List<Integer> validOptions = new ArrayList<>();

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Создает базовую страницу с указанным сервисом консоли.
     *
     * @param consoleService сервис для работы с консолью
     */
    public Page(ConsoleService consoleService) {
        this.consoleService = consoleService;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. OUTPUT
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Абстрактный метод для отображения страницы.
     */
    public abstract void show();

    /**
     * Очищает консоль и устанавливает заголовок страницы.
     *
     * @param title заголовок страницы
     */
    protected void setTitle(String title) {
        clearText();
        printNewPageHeader(title);
    }

    /**
     * Устанавливает меню страницы и парсит доступные опции.
     *
     * @param menu текст меню с опциями
     */
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

        while (true) {
            System.out.print(actionMessage);

            boolean hasNextDouble = scanner.hasNextDouble();

            if (hasNextDouble) {
                amount = scanner.nextDouble();
            }

            if (amount <= 0) {
                String errorMessage = "\n! Ошибка: введите корректную сумму (число > 0)";
                System.out.println(errorMessage);

                if (!hasNextDouble) {
                    scanner.next();
                }
            } else {
                break; // значение amount определено корректно
            }
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

        while (true) {
            System.out.print(actionMessage);

            boolean hasNextInt = scanner.hasNextInt();

            if (hasNextInt) {
                option = scanner.nextInt();
            }

            if (!validOptions.contains(option)) {
                String errorMessage = MessageFormat.format(
                        "\n! Ошибка: введите корректный номер (с {0} по {1})",
                        validOptions.getFirst(), validOptions.getLast());
                System.out.println(errorMessage);

                if (!hasNextInt) {
                    scanner.next();
                }
            } else {
                break; // значение option определено корректно
            }
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