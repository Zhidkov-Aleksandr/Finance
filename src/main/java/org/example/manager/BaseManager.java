package org.example.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//Базовый абстрактный класс, предоставляющий общий Gson для всех менеджеров.

public abstract class BaseManager {
    protected static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();
}
