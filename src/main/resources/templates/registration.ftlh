<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Регистрация</h1>
    <hr>
    <!-- Всплывающее уведомление -->
    <#if errorMessage??>
        <div id="error-message" class="alert alert-danger" style="background-color: #f8d7da; color: #721c24; border-color: #f5c6cb; position: fixed; bottom: 20px; left: 50%; transform: translateX(-50%);" role="alert">
            ${errorMessage}
        </div>
    </#if>

    <form action="/registration" method="post" onsubmit="return validatePassword()">
        <div class="form-group">
            <label for="name">Имя пользователя</label>
            <input type="text" class="form-control" required id="name" name="name">
        </div>
        <div class="form-group">
            <label for="email">Электронная почта</label>
            <input type="email" class="form-control" id="email" required name="email">
        </div>
        <div class="form-group">
            <label for="phoneNumber">Номер телефона</label>
            <input type="text" class="form-control" id="numberPhone" required name="numberPhone">
        </div>
        <div class="form-group">
            <label for="password">Пароль</label>
            <input type="password" class="form-control mb-4" id="password" required name="password">
            <div id="passwordError" class="invalid-feedback"></div>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        <button type="submit" style="width: 100%" class="btn btn-success">Зарегистрироваться</button>
    </form>

    <script>
        function validatePassword() {
            const passwordInput = document.getElementById("password");
            const errorDiv = document.getElementById("passwordError");
            const password = passwordInput.value;
            const errors = [];

            // Проверки пароля
            if (password.length < 8) {
                errors.push("Пароль должен быть длиннее 8 символов.");
            }
            if (!/[A-Za-z]/.test(password) || !/[0-9]/.test(password)) {
                errors.push("Пароль должен содержать буквы и цифры.");
            }
            if (/(\w)\1{4,}/.test(password)) {
                errors.push("Пароль не должен содержать много повторяющихся символов.");
            }

            if (errors.length > 0) {
                errorDiv.textContent = errors.join(" ");
                errorDiv.style.display = "block";
                passwordInput.classList.add("is-invalid");
                return false; // Предотвратить отправку формы
            }

            errorDiv.style.display = "none";
            passwordInput.classList.remove("is-invalid");
            return true; // Разрешить отправку формы
        }

        // Скрытие всплывающего уведомления
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
