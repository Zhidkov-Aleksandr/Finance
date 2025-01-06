package org.example.manager;

import org.example.api.IBalanceManager;
import org.example.api.IBudgetManager;
import org.example.api.ITransactionManager;

public class BalanceManager extends BaseManager implements IBalanceManager {

    private final ITransactionManager transactionManager;
    private final IBudgetManager budgetManager;

    public BalanceManager(ITransactionManager transactionManager, IBudgetManager budgetManager) {
        this.transactionManager = transactionManager;
        this.budgetManager = budgetManager;
    }

    @Override
    public void displayBalance(String login) {
        double totalIncome = transactionManager.calculateTotal(login + "-income-transactions.json");
        double totalExpenses = transactionManager.calculateTotal(login + "-expense-transactions.json");
        double balance = totalIncome - totalExpenses;

        System.out.printf("Общий доход: %.2f\n", totalIncome);
        System.out.println("Доходы по категориям:");
        transactionManager.displayTransactionsByCategory(login + "-income-transactions.json");

        System.out.printf("Общие расходы: %.2f\n", totalExpenses);
        System.out.println("Расходы по категориям:");
        transactionManager.displayTransactionsByCategory(login + "-expense-transactions.json");

        System.out.println("Бюджет по категориям:");
        budgetManager.displayBudgetsWithRemaining(login);

        System.out.printf("Остаток баланса: %.2f\n", balance);
    }
}
