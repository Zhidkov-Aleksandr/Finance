package org.example.manager;

import com.google.gson.reflect.TypeToken;
import org.example.api.IBudgetManager;
import org.example.api.ITransactionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

//Класс, отвечающий за логику работы с бюджетами пользователя.

public class BudgetManager extends BaseManager implements IBudgetManager {

    private final ITransactionManager transactionManager;

    public BudgetManager(ITransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public void setOrUpdateBudget(String login, String category, double budget) {
        String budgetFile = login + "-budgets.json";
        Map<String, Double> budgets = new HashMap<>();

        try {
            if (Files.exists(Paths.get(budgetFile))) {
                String content = Files.readString(Paths.get(budgetFile));
                if (!content.isEmpty()) {
                    budgets = GSON.fromJson(content, new TypeToken<Map<String, Double>>() {}.getType());
                }
            }
            budgets.put(category, budget);
            Files.writeString(Paths.get(budgetFile), GSON.toJson(budgets));
            System.out.println("Бюджет успешно установлен/изменён.");
        } catch (IOException e) {
            System.err.println("Ошибка при установке бюджета: " + e.getMessage());
        }
    }

    @Override
    public void viewBudgets(String login) {
        String budgetFile = login + "-budgets.json";
        if (Files.exists(Paths.get(budgetFile))) {
            try {
                String content = Files.readString(Paths.get(budgetFile));
                if (!content.isEmpty()) {
                    Map<String, Double> budgets = GSON.fromJson(content, new TypeToken<Map<String, Double>>() {}.getType());
                    System.out.println("Бюджеты по категориям:");
                    for (Map.Entry<String, Double> entry : budgets.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue());
                    }
                } else {
                    System.out.println("Нет установленных бюджетов.");
                }
            } catch (IOException e) {
                System.err.println("Ошибка при загрузке бюджетов: " + e.getMessage());
            }
        } else {
            System.out.println("Нет установленных бюджетов.");
        }
    }

    @Override
    public boolean checkBudget(String login, String category, double amount) {
        String budgetFile = login + "-budgets.json";
        if (Files.exists(Paths.get(budgetFile))) {
            try {
                String content = Files.readString(Paths.get(budgetFile));
                if (!content.isEmpty()) {
                    Map<String, Double> budgets = GSON.fromJson(content, new TypeToken<Map<String, Double>>() {}.getType());
                    if (budgets.containsKey(category)) {
                        double budget = budgets.get(category);
                        double totalExpenses = transactionManager.calculateTotalForCategory(login + "-expense-transactions.json", category);
                        if (totalExpenses + amount > budget) {
                            return false;
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Ошибка при проверке бюджета: " + e.getMessage());
            }
        }
        return true;
    }

    @Override
    public void displayBudgetsWithRemaining(String login) {
        String budgetFile = login + "-budgets.json";
        if (Files.exists(Paths.get(budgetFile))) {
            try {
                String content = Files.readString(Paths.get(budgetFile));
                if (!content.isEmpty()) {
                    Map<String, Double> budgets = GSON.fromJson(content, new TypeToken<Map<String, Double>>() {}.getType());
                    for (Map.Entry<String, Double> entry : budgets.entrySet()) {
                        String category = entry.getKey();
                        double budget = entry.getValue();
                        double totalExpenses = transactionManager.calculateTotalForCategory(login + "-expense-transactions.json", category);
                        double remaining = budget - totalExpenses;
                        System.out.printf("%s: %.2f, Оставшийся бюджет: %.2f\n", category, budget, remaining);
                    }
                }
            } catch (IOException e) {
                System.err.println("Ошибка при загрузке бюджетов: " + e.getMessage());
            }
        }
    }
}
