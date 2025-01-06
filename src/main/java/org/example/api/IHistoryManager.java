package org.example.api;

import java.util.List;
import java.util.Scanner;

//Интерфейс для вывода истории операций.

public interface IHistoryManager {
    void operationHistoryMenu(String login, Scanner scanner);
    List<String> getAllTransactions(String login);
}
