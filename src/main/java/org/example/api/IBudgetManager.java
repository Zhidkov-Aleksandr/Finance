package org.example.api;

//Интерфейс для управления бюджетами (установка, просмотр, проверка).

public interface IBudgetManager {
    void setOrUpdateBudget(String login, String category, double budget);
    void viewBudgets(String login);
    boolean checkBudget(String login, String category, double amount);
    void displayBudgetsWithRemaining(String login);
}
