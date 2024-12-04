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
        <h1>Действующие пункты выдачи</h1>

        <!-- Кнопка для добавления нового пункта выдачи -->
        <a class="btn btn-dark mt-3 mb-3" data-bs-toggle="collapse" href="#collapseAddPickup" role="button" aria-expanded="false" aria-controls="collapseAddPickup">
            Добавить новый пункт выдачи
        </a>

        <div class="collapse" id="collapseAddPickup">
            <div class="card card-body">
                <form action="/pickup/points/add" method="post">
                    <div class="form-group">
                        <label for="name">Название пункта выдачи:</label>
                        <input type="text" id="name" name="name" required class="form-control">
                    </div>
                    <div class="form-group">
                        <label for="location">Местоположение:</label>
                        <input type="text" id="location" name="location" required class="form-control">
                    </div>
                    <div class="form-group">
                        <label for="city">Город:</label>
                        <select id="city" name="city.id" required class="form-control">
                            <#list cities as city>
                                <option value="${city.id}">${city.cityName}</option>
                            </#list>
                        </select>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button type="submit" class="btn btn-primary">Добавить пункт выдачи</button>
                </form>
            </div>
        </div>

        <hr>

        <!-- Список действующих пунктов выдачи -->
        <h2>Существующие пункты выдачи:</h2>
        <ul class="list-group">
            <#if pickupPoints?has_content>
                <#list pickupPoints as point>
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        <span>
                            <strong>${point.name!}</strong> - ${point.location!}, ${point.city.cityName!}
                        </span>
                        <!-- Кнопка для удаления, которая вызывает модальное окно -->
                        <button type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#deletePickupModal" data-id="${point.pickUpPointId}" data-name="${point.name}">
                            Удалить
                        </button>
                    </li>
                </#list>
            <#else>
                <li class="list-group-item">Нет действующих пунктов выдачи.</li>
            </#if>
        </ul>

        <a href="/profile" class="btn btn-secondary mt-3">Назад к панели администратора</a>
    </div>

    <!-- Модальное окно для подтверждения удаления с уменьшенным размером -->
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
        // Когда кнопка "Удалить" нажата, заполняем модальное окно нужными данными
        $('#deletePickupModal').on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget); // Кнопка, которая вызвала модальное окно
            var pickUpPointId = button.data('id'); // Получаем ID пункта выдачи
            var pickUpPointName = button.data('name'); // Получаем название пункта выдачи

            // Обновляем содержимое модального окна
            var modal = $(this);
            modal.find('#pickupNameToDelete').text(pickUpPointName);
            modal.find('#confirmDeleteButton').attr('href', '/pickup/points/delete/' + pickUpPointId); // Устанавливаем ссылку для подтверждения удаления
        });
    </script>

    </body>
    </html>
</@t.template>
