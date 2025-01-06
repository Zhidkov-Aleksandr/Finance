package org.example.api;

import java.util.List;

// Интерфейс для работы с транзакциями (логирование, получение, подсчёт).

public interface ITransactionManager {
    void logTransaction(String login, String category, String amountStr, boolean isIncome);
    List<String> getAllTransactions(String login);
    double calculateTotal(String fileName);
    double calculateTotalForCategory(String fileName, String category);
    void displayTransactionsByCategory(String fileName);
}
