<#import "blocks/template.ftlh" as t>
<@t.template user>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Добавить пункт выдачи</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
    </head>
    <body>
    <div class="container mt-5">
        <h1 style="text-align: center">Действующие пункты выдачи</h1>

        <!-- Кнопка для добавления нового пункта выдачи -->
        <a class="btn btn-dark mt-3 mb-3 w-100" data-bs-toggle="collapse" href="#collapseAddPickup" role="button" aria-expanded="false" aria-controls="collapseAddPickup">
            Добавить новый пункт выдачи
        </a>

        <div class="collapse" id="collapseAddPickup">
            <div class="card card-body">
                <form action="/pickup/points/add" method="post" onsubmit="return validateForm()">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> <!-- Поле CSRF -->
                    <div class="form-group">
                        <label for="name">Название пункта выдачи:</label>
                        <input type="text" id="name" name="name" required class="form-control mb-3">
                        <small id="nameError" class="text-danger"></small>
                    </div>
                    <div class="form-group">
                        <label for="location">Местоположение:</label>
                        <input type="text" id="location" name="location" required class="form-control mb-3">
                        <small id="locationError" class="text-danger"></small>
                    </div>
                    <div class="form-group">
                        <label for="city">Город:</label>
                        <select id="city" name="city.id" required class="form-control mb-3">
                            <#list cities as city>
                                <option value="${city.id}">${city.cityName}</option>
                            </#list>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-dark w-100">Добавить пункт выдачи</button>
                </form>
            </div>
        </div>

        <hr>

        <h2 style="text-align: center">Существующие пункты выдачи:</h2>
        <table class="table table-success" style="border-radius: 10px; overflow: hidden;">
            <thead>
            <tr>
                <th>ID</th>
                <th>Название</th>
                <th>Местоположение</th>
                <th>Город</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <#if pickupPoints?has_content>
                <#list pickupPoints as point>
                    <tr>
                        <td>${point.pickUpPointId}</td>
                        <td>${point.name}</td>
                        <td>${point.location}</td>
                        <td>${point.city.cityName}</td>
                        <td>
                            <button type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#deletePickupModal" data-id="${point.pickUpPointId}" data-name="${point.name}">
                                Удалить
                            </button>
                        </td>
                    </tr>
                </#list>
            <#else>
                <tr>
                    <td colspan="5" class="text-center">Нет действующих пунктов выдачи.</td>
                </tr>
            </#if>
            </tbody>
        </table>

        <a href="/profile" class="btn btn-secondary mt-3">Назад к панели администратора</a>
    </div>

    <!-- Модальное окно для подтверждения удаления -->
    <div class="modal fade" id="deletePickupModal" tabindex="-1" aria-labelledby="deletePickupModalLabel" aria-hidden="true">
        <div class="modal-dialog" style="max-width: 400px; width: 100%;">
            <div class="modal-content" style="border-radius: 10px;">
                <div class="modal-header">
                    <h5 class="modal-title" id="deletePickupModalLabel">Подтверждение удаления</h5>
                </div>
                <div class="modal-body">
                    <p>Вы уверены, что хотите удалить пункт выдачи <span id="pickupNameToDelete"></span>?</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Отмена</button>
                    <a id="confirmDeleteButton" class="btn btn-danger" href="#">Удалить</a>
                </div>
            </div>
        </div>
    </div>

    <script>
        $('#deletePickupModal').on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget);
            var pickUpPointId = button.data('id');
            var pickUpPointName = button.data('name');

            var modal = $(this);
            modal.find('#pickupNameToDelete').text(pickUpPointName);
            modal.find('#confirmDeleteButton').attr('href', '/pickup/points/delete/' + pickUpPointId);
        });

        function validateForm() {
            const name = document.getElementById('name').value;
            const location = document.getElementById('location').value;
            const specialCharPattern = /[!@#$%^&*(),.?":{}|<>]/;
            let valid = true;

            // Сброс ошибок
            document.getElementById('nameError').textContent = '';
            document.getElementById('locationError').textContent = '';

            if (specialCharPattern.test(name)) {
                document.getElementById('nameError').textContent = "Название не должно содержать специальные символы.";
                valid = false;
            }

            if (specialCharPattern.test(location)) {
                document.getElementById('locationError').textContent = "Местоположение не должно содержать специальные символы.";
                valid = false;
            }

            return valid; // Возвращаем результат валидации
        }
    </script>

    </body>
    </html>
</@t.template>