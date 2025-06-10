package ru.gritsenkodaniil.bankaccountmanagement;

public class BankApplication {

    public static void main(String[] args) {
        // Создание счетов
        BankAccount store = new BankAccount(50501944, "Магазин");
        BankAccount ivanovI = new BankAccount(94867303, "Иванов И");
        BankAccount sidorovS = new BankAccount(70019471, "Сидоров С");
        BankAccount petrovP = new BankAccount(86107026, "Петров П");

        // Выполнение операций
        ivanovI.deposit(50000);
        ivanovI.debit(599.25, store);
        ivanovI.transfer(10000, sidorovS);

        sidorovS.withdrawal(1000);
        sidorovS.transfer(2000, petrovP);
        sidorovS.withdrawal(8000);

        petrovP.transfer(5000, ivanovI);
        petrovP.deposit(5000);
        petrovP.transfer(5000, ivanovI);

        // Вывод инфо
        store.printTransactions();
        store.printBalance();

        printDivider();

        ivanovI.printTransactions();
        ivanovI.printBalance();

        printDivider();

        sidorovS.printTransactions();
        sidorovS.printBalance();

        printDivider();

        petrovP.printTransactions();
        petrovP.printBalance();
    }

    public static void printDivider() {
        System.out.println();
    }
}