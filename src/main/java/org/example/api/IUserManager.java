package org.example.api;

//Интерфейс для управления пользователями (загрузка, сохранение, проверка).

public interface IUserManager {
    void loadUsers();
    void saveUsers();
    boolean validCredentials(String login, String password);
    void createUser(String login, String password);
    boolean isEmpty();
    boolean containsUser(String login);
}
