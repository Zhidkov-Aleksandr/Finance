package org.example;

import org.example.api.*;
import org.example.manager.*;

import java.util.Scanner;

public class WalletApp  {

    private final IUserManager userManager;
    private final ITransactionManager transactionManager;
    private final IBudgetManager budgetManager;
    private final IBalanceManager balanceManager;
    private final IHistoryManager historyManager;

    public WalletApp() {
        // Инициализация менеджеров
        this.transactionManager = new TransactionManager();
        this.budgetManager = new BudgetManager(transactionManager);
        this.balanceManager = new BalanceManager(transactionManager, budgetManager);
        this.userManager = new UserManager();
        this.historyManager = new HistoryManager(transactionManager);
    }

    public static void main(String[] args) {
        WalletApp app = new WalletApp();
        app.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Добро пожаловать в систему управления финансами!");

            if (userManager.isEmpty()) {
                System.out.println("Пока нет пользователей.");
                System.out.print("Введите логин нового пользователя: ");
                String login = scanner.nextLine();
                System.out.print("Введите пароль: ");
                String password = scanner.nextLine();

                userManager.createUser(login, password);
                System.out.println("Пользователь успешно создан.");
            } else {
                System.out.println("1. Авторизация");
                System.out.println("2. Создание нового пользователя");
                System.out.println("3. Выход");
                String choice = scanner.nextLine();

                if (choice.equals("1")) {
                    System.out.print("Введите логин: ");
                    String login = scanner.nextLine();
                    System.out.print("Введите пароль: ");
                    String password = scanner.nextLine();

                    if (userManager.validCredentials(login, password)) {
                        System.out.println("Авторизация успешна.");
                        userMenu(login, scanner);
                    } else {
                        System.out.println("Неверный логин или пароль.");
                    }
                } else if (choice.equals("2")) {
                    System.out.print("Введите логин нового пользователя: ");
                    String login = scanner.nextLine();
                    System.out.print("Введите пароль: ");
                    String password = scanner.nextLine();

                    userManager.createUser(login, password);
                    System.out.println("Пользователь успешно создан.");
                } else if (choice.equals("3")) {
                    System.out.println("Выход из программы.");
                    System.exit(0);
                }
            }
        }
    }

    private void userMenu(String login, Scanner scanner) {
        while (true) {
            System.out.println("Выберите действие:");
            System.out.println("1. Посмотреть баланс");
            System.out.println("2. Выполнить операцию");
            System.out.println("3. Выход");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                balanceManager.displayBalance(login);
            } else if (choice.equals("2")) {
                operationMenu(login, scanner);
            } else if (choice.equals("3")) {
                break;
            }
        }
    }

    private void operationMenu(String login, Scanner scanner) {
        while (true) {
            System.out.println("Выберите тип операции:");
            System.out.println("1. Ввести доход");
            System.out.println("2. Ввести расход");
            System.out.println("3. Бюджеты");
            System.out.println("4. История операций");
            System.out.println("5. Назад");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                incomeMenu(login, scanner);
            } else if (choice.equals("2")) {
                expenseMenu(login, scanner);
            } else if (choice.equals("3")) {
                budgetMenu(login, scanner);
            } else if (choice.equals("4")) {
                historyManager.operationHistoryMenu(login, scanner);
            } else if (choice.equals("5")) {
                break;
            }
        }
    }

    private void budgetMenu(String login, Scanner scanner) {
        while (true) {
            System.out.println("Управление бюджетами:");
            System.out.println("1. Установить или изменить бюджет");
            System.out.println("2. Просмотреть бюджеты");
            System.out.println("3. Назад");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                setOrUpdateBudget(login, scanner);
            } else if (choice.equals("2")) {
                budgetManager.viewBudgets(login);
            } else if (choice.equals("3")) {
                break;
            }
        }
    }

    private void setOrUpdateBudget(String login, Scanner scanner) {
        System.out.print("Введите категорию для бюджета: ");
        String category = scanner.nextLine();
        double budget = 0;

        while (true) {
            System.out.print("Введите сумму бюджета: ");
            String budgetStr = scanner.nextLine();
            try {
                budget = Double.parseDouble(budgetStr);
                if (budget <= 0) {
                    System.out.println("Сумма должна быть больше нуля. Попробуйте снова.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Допускается только ввод числа. Попробуйте снова.");
            }
        }

        budgetManager.setOrUpdateBudget(login, category, budget);
    }

    private void incomeMenu(String login, Scanner scanner) {
        double amount;
        while (true) {
            System.out.print("Введите сумму дохода: ");
            String amountStr = scanner.nextLine();
            try {
                amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    System.out.println("Сумма должна быть больше нуля. Попробуйте снова.");
                    continue;
                }
                // Логируем транзакцию
                transactionManager.logTransaction(login, requestCategory(scanner, "дохода"), amountStr, true);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Допускается только ввод числа. Попробуйте снова.");
            }
        }
    }

    private void expenseMenu(String login, Scanner scanner) {
        while (true) {
            System.out.print("Введите сумму расхода: ");
            String amountStr = scanner.nextLine();
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    System.out.println("Сумма должна быть больше нуля. Попробуйте снова.");
                    continue;
                }
                String category = requestCategory(scanner, "расхода");

                // Проверяем бюджет
                if (!budgetManager.checkBudget(login, category, amount)) {
                    System.out.println("Операция расхода превышает установленный бюджет!");
                    return;
                }

                // Логируем транзакцию
                transactionManager.logTransaction(login, category, amountStr, false);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Допускается только ввод числа. Попробуйте снова.");
            }
        }
    }

    private String requestCategory(Scanner scanner, String operationName) {
        System.out.print("Введите категорию " + operationName + ": ");
        return scanner.nextLine();
    }
}
