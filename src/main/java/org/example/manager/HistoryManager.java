package org.example.manager;

import org.example.api.IHistoryManager;
import org.example.api.ITransactionManager;

import java.util.List;
import java.util.Scanner;

//Класс, отвечающий за вывод истории операций в консольном меню.

public class HistoryManager extends BaseManager implements IHistoryManager {

    private final ITransactionManager transactionManager;

    public HistoryManager(ITransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public void operationHistoryMenu(String login, Scanner scanner) {
        System.out.println("История операций:");
        List<String> transactions = getAllTransactions(login);
        if (transactions.isEmpty()) {
            System.out.println("Операции отсутствуют.");
            return;
        }
        for (String transaction : transactions) {
            System.out.println(transaction);
        }
        System.out.print("Введите 0 для возврата в предыдущее меню: ");
        double number = inputDouble(scanner, "");
        if (number == 0) {
            System.out.println("Возвращение в предыдущее меню...");
        } else {
            System.out.println("Некорректный ввод. Доступна только операция возврата.");
        }
    }

    @Override
    public List<String> getAllTransactions(String login) {
        return transactionManager.getAllTransactions(login);
    }

    /**
     * Утилитный метод для считывания double с консоли.
     */
    private double inputDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                double value = Double.parseDouble(input);
                if (value >= 0) {
                    return value;
                } else {
                    System.out.println("Ошибка: введите положительное число или 0 для возврата.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите допустимое число.");
            }
        }
    }
}
