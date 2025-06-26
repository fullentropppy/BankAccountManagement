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
 *  <p>
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
     * Очищает консоль и устанавливает шапку страницы.
     *
     * @param title заголовок страницы
     */
    protected void setHeader(String title) {
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

    /**
     * Выводит форматированную ошибку.
     *
     * @param error текст ошибки
     */
    protected void printError(String error) {
        printError(error, null);
    }

    /**
     * Выводит форматированную ошибку
     * и при необходимости приостанавливает выполнение программы до нажатия Enter пользователем.
     *
     * @param error текст ошибки
     * @param waitingTitle сообщение, отображаемое при приостановлении выполнения,
     *                     если {@code null}, то приостановления выполнения не будет.
     */
    protected void printError(String error, String waitingTitle) {
        String errorMsg = MessageFormat.format("\n! Ошибка: {0}", error);
        System.out.println(errorMsg);

        if (waitingTitle != null && !waitingTitle.isBlank()) {
            waitForInputToContinue(waitingTitle);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. INPUT
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Запрашивает у пользователя ввод суммы (положительное число) через консоль.
     *
     * @param actionTitle подсказка для ввода
     *
     * @return введенная пользователем сумма
     */
    protected double getAmount(String actionTitle) {
        double amount = 0;

        String actionMsg = MessageFormat.format("\n> {0}: ", actionTitle);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(actionMsg);

            boolean hasNextDouble = scanner.hasNextDouble();

            if (hasNextDouble) {
                amount = scanner.nextDouble();
            }

            if (amount <= 0) {
                System.out.println("\n! Ошибка: введите корректную сумму (число > 0)");

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
     * @param actionTitle подсказка для ввода
     *
     * @return выбранный пользователем пункт меню
     */
    protected int getOptionFromMenu(String actionTitle) {
        int option = -1;

        String actionMsg = MessageFormat.format("\n> {0}: ", actionTitle);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(actionMsg);

            boolean hasNextInt = scanner.hasNextInt();

            if (hasNextInt) {
                option = scanner.nextInt();
            }

            if (!validOptions.contains(option)) {
                String error = MessageFormat.format(
                        "Введите корректный номер (с {0} по {1})",
                        validOptions.getFirst(), validOptions.getLast()
                );
                printError(error);

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
        String actionMsg = MessageFormat.format("\n> {0}...", actionTitle);
        System.out.print(actionMsg);

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