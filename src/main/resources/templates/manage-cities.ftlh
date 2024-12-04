<#import "blocks/template.ftlh" as t>
<@t.template user>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Управление городами</title>
        <!-- Bootstrap подключен в вашем шаблоне, лишнее удалено -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
    </head>
    <body>
    <div class="container mt-5">
        <h1 style="text-align: center">Города, по которым осуществляется доставка</h1>

        <!-- Кнопка для добавления нового города -->
        <a class="btn btn-dark mt-3 mb-3" data-bs-toggle="collapse" href="#collapseAddCity" role="button" aria-expanded="false" aria-controls="collapseAddCity" style="display: block; margin: 0 auto; width: 200px; text-align: center;">
            Добавить новый город
        </a>

        <div class="collapse" id="collapseAddCity">
            <div class="card card-body">
                <form action="/admin/cities/add" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> <!-- Поле CSRF -->
                    <div class="form-group">
                        <label for="cityName">Название города</label>
                        <input type="text" class="form-control" id="cityName" name="cityName" required>
                    </div>
                    <button type="submit" class="btn btn-dark" style="width: 100%;">Добавить город</button>
                </form>
            </div>
        </div>

        <hr>

        <h2 style="text-align: center">Существующие города</h2>
        <table class="table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Название города</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <#list cities as city>
                <tr>
                    <td>${city.id}</td>
                    <td>${city.cityName}</td>
                    <td>
                        <!-- Кнопка для удаления города, которая открывает модальное окно -->
                        <button type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#deleteCityModal" data-id="${city.id}" data-name="${city.cityName}">
                            Удалить
                        </button>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>

        <a href="/profile" class="btn btn-secondary mt-3">Назад к панели администратора</a>
    </div>

    <!-- Модальное окно для подтверждения удаления -->
    <div class="modal fade" id="deleteCityModal" tabindex="-1" aria-labelledby="deleteCityModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-scrollable">
            <div class="modal-content" style="border-radius: 10px;">
                <div class="modal-header">
                    <h5 class="modal-title" id="deleteCityModalLabel">Подтверждение удаления</h5>
                </div>
                <div class="modal-body">
                    <p>Вы уверены, что хотите удалить город <span id="cityNameToDelete"></span>?</p>
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
        $('#deleteCityModal').on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget); // Кнопка, которая вызвала модальное окно
            var cityId = button.data('id'); // Получаем ID города
            var cityName = button.data('name'); // Получаем название города

            // Обновляем содержимое модального окна
            var modal = $(this);
            modal.find('#cityNameToDelete').text(cityName);
            modal.find('#confirmDeleteButton').attr('href', '/admin/cities/delete/' + cityId); // Устанавливаем ссылку для подтверждения удаления
        });
    </script>

    </body>
    </html>
</@t.template>
