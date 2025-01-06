package org.example.manager;

import com.google.gson.reflect.TypeToken;
import org.example.api.IUserManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

//Класс, отвечающий за работу с пользователями (загрузка/сохранение из файла, проверка логина/пароля).

public class UserManager extends BaseManager implements IUserManager {

    private static final String USER_FILE = "users.json";

    private final Map<String, String> users = new HashMap<>();

    public UserManager() {
        // При создании менеджера сразу загружаем пользователей из файла
        loadUsers();
    }

    @Override
    public void loadUsers() {
        Path path = Paths.get(USER_FILE);
        if (Files.exists(path)) {
            try {
                String json = Files.readString(path);
                if (!json.isEmpty()) {
                    Map<String, String> loadedUsers = GSON.fromJson(json, new TypeToken<Map<String, String>>() {}.getType());
                    users.putAll(loadedUsers);
                }
            } catch (IOException e) {
                System.err.println("Ошибка при загрузке пользователей: " + e.getMessage());
            }
        }
    }

    @Override
    public void saveUsers() {
        Path path = Paths.get(USER_FILE);
        try {
            Files.writeString(path, GSON.toJson(users));
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении пользователей: " + e.getMessage());
        }
    }

    @Override
    public boolean validCredentials(String login, String password) {
        return users.containsKey(login) && users.get(login).equals(password);
    }

    @Override
    public void createUser(String login, String password) {
        users.put(login, password);
        saveUsers();
    }

    @Override
    public boolean isEmpty() {
        return users.isEmpty();
    }

    @Override
    public boolean containsUser(String login) {
        return users.containsKey(login);
    }
}
