package ru.dgritsenko.userinterface.console;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Абстрактный класс, представляющий базовую страницу интерфейса.
 * <p>Содержит общие методы для работы с консольным вводом/выводом.
 */
public abstract class Page {
    protected final ConsoleUIService consoleUIService;
    private final List<Integer> validOptions = new ArrayList<>();

    // -----------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Создает базовую страницу с указанным сервисом консоли.
     *
     * @param consoleUIService сервис для работы с консолью
     */
    public Page(ConsoleUIService consoleUIService) {
        this.consoleUIService = consoleUIService;
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
        consoleUIService.clearText();
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
     * @param waitingTitle подсказка при приостановлении выполнения,
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
     * Запрашивает у пользователя ввод строки.
     *
     * @param actionTitle подсказка для ввода
     * @param cancellationOption дополнительная подсказка для отмены операции (на ввод не влияет)
     *
     * @return введеная строка
     */
    protected String getString(String actionTitle, String cancellationOption) {
        String actionMsg = getFormattedActionTitle(actionTitle, cancellationOption);
        System.out.print(actionMsg);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    /**
     * Запрашивает у пользователя ввод суммы.
     *
     * @param actionTitle подсказка для ввода
     * @param isCancellationAvail добавление в подсказку информации о возможности отмены операции.
     *                            Если {@code true} то предлагается опция отмены и введенная сумма может быть {@code 0}.
     *                            Иначе введенная сумма должна быть > {@code 0}
     *
     *
     * @return введенная сумма
     */
    protected double getAmount(String actionTitle, boolean isCancellationAvail) {
        double amount = -1;

        String cancellationOption = isCancellationAvail ? "0" : null;
        String actionMsg = getFormattedActionTitle(actionTitle, cancellationOption);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(actionMsg);

            boolean hasNextDouble = scanner.hasNextDouble();
            if (hasNextDouble) {
                amount = scanner.nextDouble();
            }

            if (!isCancellationAvail || amount < 0) {
                System.out.println("\n! Ошибка: введите корректную сумму");

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
     * @return выбранный номер пункта меню
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
     * @param actionTitle подсказка для ввода
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
        consoleUIService.clearText();

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
     * Возвращает форматированную строку с предложением ввода.
     *
     * @param actionTitle подсказка для ввода
     * @param actionToCancel представление опции для отмены операции.
     *                       Если {@code null} то информация об опции отмены не будет добавлена
     *
     * @return строка с предложением ввода
     */
    private String getFormattedActionTitle(String actionTitle, String actionToCancel) {
        String cancellationHint;

        if (actionToCancel == null) {
            cancellationHint = "";
        } else {
            cancellationHint = MessageFormat.format(" (или ''{0}'' для отмены)", actionToCancel);
        }

        return MessageFormat.format("\n> {0}{1}: ", actionTitle, cancellationHint);
    }
}