# Finance
Простое консольное Java-приложение для управления личными финансами:

Регистрация и авторизация пользователей
Учёт доходов и расходов
Настройка и проверка бюджетов по категориям
Просмотр истории операций
Отображение баланса и остатков по бюджетам

Возможности
Регистрация пользователей: хранение логинов/паролей в JSON-файле
Авторизация: проверка логина и пароля при входе
Учёт доходов: добавление транзакции дохода, сохранение в JSON-файл
Учёт расходов: добавление транзакции расхода, проверка соответствующего бюджета
Настройка бюджетов: установка бюджетов по категориям, сохранение в JSON-файл
История операций: просмотр всех операций (доходов и расходов) в консоли
Баланс: отображение суммарного дохода, расхода и остатка средств

Пример работы
markdown
Копировать код
Добро пожаловать в систему управления финансами!
1. Авторизация
2. Создание нового пользователя
3. Выход
> 1
Введите логин: testUser
Введите пароль: qwerty
Авторизация успешна.

Выберите действие:
1. Посмотреть баланс
2. Выполнить операцию
3. Выход
> 2

Выберите тип операции:
1. Ввести доход
2. Ввести расход
3. Бюджеты
4. История операций
5. Назад
> 1
Введите сумму дохода: 1000
Введите категорию дохода: Зарплата
Операция успешно выполнена.
...
