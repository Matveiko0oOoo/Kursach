<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Редактирование профиля</title>
    <link rel="stylesheet" href="/static/css/style.css">
</head>
<body>
<div class="container">
    <h1>Редактирование профиля</h1>

    <#if errorMessage?exists>
        <div class="alert alert-danger">${errorMessage}</div>
    </#if>

    <form action="/profile/edit/${user.id}" method="post">
        <div class="form-group">
            <label for="username">Имя пользователя</label>
            <input type="text" required class="form-control" id="username" name="name" value="${user.name}">
        </div>
        <div class="form-group">
            <label for="email">Электронная почта</label>
            <input type="email" required class="form-control" id="email" name="email" value="${user.email}">
        </div>
        <div class="form-group">
            <label for="phone">Номер телефона</label>
            <input type="tel" required class="form-control" id="phone" name="numberPhone" value="${user.numberPhone}">
        </div>
        <button type="submit" class="btn btn-dark">Сохранить изменения</button>
    </form>
    <a href="/profile" class="btn btn-secondary">Назад</a>
</div>
</body>
</html>