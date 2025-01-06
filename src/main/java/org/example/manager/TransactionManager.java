package org.example.manager;

import com.google.gson.reflect.TypeToken;
import org.example.api.ITransactionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

//Класс, отвечающий за логику работы с доходными и расходными транзакциями.

public class TransactionManager extends BaseManager implements ITransactionManager {

    private int transactionCounter = 1;

    @Override
    public void logTransaction(String login, String category, String amountStr, boolean isIncome) {
        String transactionFile = login + (isIncome ? "-income-transactions.json" : "-expense-transactions.json");
        String timestamp = new SimpleDateFormat("HH:mm dd.MM.yyyy").format(new Date());

        Map<String, String> transactionEntry = new HashMap<>();
        transactionEntry.put("number", String.valueOf(transactionCounter));
        transactionEntry.put("amount", amountStr);
        transactionEntry.put("date", timestamp);
        transactionEntry.put("name", category);

        transactionCounter++;

        try {
            Files.writeString(
                    Paths.get(transactionFile),
                    GSON.toJson(transactionEntry) + System.lineSeparator(),
                    java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.APPEND
            );
            System.out.println("Операция успешно выполнена.");
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении операции: " + e.getMessage());
        }
    }

    @Override
    public List<String> getAllTransactions(String login) {
        List<String> transactions = new ArrayList<>();
        try {
            String incomeFile = login + "-income-transactions.json";
            if (Files.exists(Paths.get(incomeFile))) {
                transactions.addAll(Files.readAllLines(Paths.get(incomeFile)));
            }
            String expenseFile = login + "-expense-transactions.json";
            if (Files.exists(Paths.get(expenseFile))) {
                transactions.addAll(Files.readAllLines(Paths.get(expenseFile)));
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении операций: " + e.getMessage());
        }
        return transactions;
    }

    @Override
    public double calculateTotal(String fileName) {
        double total = 0;
        if (Files.exists(Paths.get(fileName))) {
            try {
                String content = Files.readString(Paths.get(fileName)).trim();
                if (!content.isEmpty()) {
                    List<String> lines = Arrays.asList(content.split(System.lineSeparator()));
                    for (String line : lines) {
                        if (!line.trim().isEmpty()) {
                            Map<String, String> entry = GSON.fromJson(line, new TypeToken<Map<String, String>>() {}.getType());
                            total += Double.parseDouble(entry.get("amount"));
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Ошибка при загрузке операций: " + e.getMessage());
            }
        }
        return total;
    }

    @Override
    public double calculateTotalForCategory(String fileName, String category) {
        double total = 0;
        if (Files.exists(Paths.get(fileName))) {
            try {
                String content = Files.readString(Paths.get(fileName));
                if (!content.isEmpty()) {
                    List<String> lines = Arrays.asList(content.split(System.lineSeparator()));
                    for (String line : lines) {
                        if (!line.trim().isEmpty()) {
                            Map<String, String> entry = GSON.fromJson(line, new TypeToken<Map<String, String>>() {}.getType());
                            if (entry.get("name").equals(category)) {
                                total += Double.parseDouble(entry.get("amount"));
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Ошибка при подсчёте расходов: " + e.getMessage());
            }
        }
        return total;
    }

    @Override
    public void displayTransactionsByCategory(String fileName) {
        if (Files.exists(Paths.get(fileName))) {
            try {
                String content = Files.readString(Paths.get(fileName));
                if (!content.isEmpty()) {
                    Map<String, Double> categoryTotals = new HashMap<>();
                    List<String> lines = Arrays.asList(content.split(System.lineSeparator()));
                    for (String line : lines) {
                        if (!line.trim().isEmpty()) {
                            Map<String, String> entry = GSON.fromJson(line, new TypeToken<Map<String, String>>() {}.getType());
                            String category = entry.get("name");
                            double amount = Double.parseDouble(entry.get("amount"));
                            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + amount);
                        }
                    }
                    for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
                        System.out.printf("%s: %.2f\n", entry.getKey(), entry.getValue());
                    }
                }
            } catch (IOException e) {
                System.err.println("Ошибка при загрузке операций: " + e.getMessage());
            }
        }
    }
}
