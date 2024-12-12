<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Авторизация</h1>
    <hr>

    <!-- Всплывающее уведомление -->
    <#if error?exists && error>
        <div id="error-message" class="alert alert-danger" style="background-color: #f8d7da; color: #721c24; border-color: #f5c6cb; position: fixed; bottom: 20px; left: 50%; transform: translateX(-50%);" role="alert">
            Неверный логин или пароль.
        </div>
    </#if>

    <form action="/login" method="post">
        <div class="form-group">
            <label for="email">Электронная почта</label>
            <input type="email" class="form-control <#if error?exists && error>is-invalid</#if>" id="email" name="username">
            <div id="email" class="invalid-feedback">
                Неверная электронная почта.
            </div>
        </div>
        <div class="form-group">
            <label for="password">Пароль</label>
            <input type="password" class="form-control mb-3 <#if error?exists && error>is-invalid</#if>" id="password" name="password">
            <div id="password" class="invalid-feedback">
                Неверный пароль.
            </div>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        <button type="submit" style="width: 100%" class="btn btn-success">Войти</button>
    </form>
    <hr>
    <h2>Нет аккаунта BUYSELL? <a href="/registration">Зарегистрируйся!</a></h2>

    <script>
        window.onload = function() {
            var errorMessage = document.getElementById("error-message");
            if (errorMessage) {
                setTimeout(function() {
                    errorMessage.style.display = "none";
                }, 3000); // Скрыть через 3 секунды
            }
        };
    </script>
</@t.template>