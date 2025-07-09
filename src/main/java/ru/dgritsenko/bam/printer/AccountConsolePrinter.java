package ru.dgritsenko.bam.printer;

import ru.dgritsenko.bam.bank.Account;
import ru.dgritsenko.bam.bank.Transaction;

import java.text.MessageFormat;
import java.util.List;

/**
 * Класс-обработчик вывода данных класса {@link Account} в консоль.
 */
public class AccountConsolePrinter {

    /**
     * Выводит информацию о текущем балансе счета.
     *
     * @param account счет-источник данных
     */
    public static void printBalance(Account account) {
        double balance = account.getBalance();
        String msg = MessageFormat.format("Счет: {0}, баланс: {1}", account, balance);
        System.out.println(msg);
    }

    /**
     * Выводит список всех транзакций по счету.
     *
     * @param account счет-источник данных
     */
    public static void printTransactions(Account account) {
        String msg;
        List<Transaction> transactions = account.getTransactions();

        if (transactions.isEmpty()) {
            msg = MessageFormat.format("Счет: {0}, список транзакций пуст...", account);
        } else {
            StringBuilder transactionsView = new StringBuilder();

            String title = MessageFormat.format("Счет: {0}, транзакции:\n", account);
            transactionsView.append(title);

            int i = 1;

            for (Transaction transaction : transactions) {
                String transactionView = transactionView(transaction);

                String transactionInfoTemplate = (i == 1)
                        ? "\t{0} - {1}, операция: {2}, сумма {3}"
                        : "\n\t{0} - {1}, операция: {2}, сумма {3}";
                String transactionInfo = MessageFormat.format(transactionInfoTemplate,
                        i, transaction, transactionView, transaction.getAmount()
                );
                transactionsView.append(transactionInfo);
                i++;
            }
            msg = transactionsView.toString();
        }
        System.out.println(msg);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // METHODS. MISC
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Формирует строковое представление операции транзакции.
     *
     * @param transaction транзакция для обработки
     *
     * @return строковое представление операции
     */
    private static String transactionView(Transaction transaction) {
        String operationView = transaction.getTransactionType().getTitle();

        String extOperationViewTemplate = switch (transaction.getTransactionType()) {
            case CREDIT -> "{0} (от: {1})";
            case TRANSFER -> "{0} (кому: {1})";
            default -> "";
        };

        if (!extOperationViewTemplate.isBlank()) {
            operationView = MessageFormat.format(
                    extOperationViewTemplate,
                    operationView, transaction.getToAccount());
        }

        return operationView;
    }
}