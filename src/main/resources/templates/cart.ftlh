<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Корзина заказов</h1>
    <hr>
    <#if cart?size == 0>
        <p style="text-align: center">Ваша корзина пуста.</p>
    <#else>
        <table class="table table-success table-striped" style="border-radius: 10px; overflow: hidden;">
            <thead>
            <tr>
                <th>Товар</th>
                <th>Цена</th>
                <th>Количество</th>
                <th>Итого</th>
                <th>Действие</th>
            </tr>
            </thead>
            <tbody>
            <#list cart as item>
                <tr>
                    <td>${item.product.title}</td>
                    <td>${item.product.price} BYN</td>
                    <td>${item.quantity}</td>
                    <td>${item.product.price * item.quantity} BYN</td>
                    <td>
                        <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteItemModal" data-item-id="${item.id}" data-item-title="${item.product.title}">
                            Удалить
                        </button>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
<hr>
        <p style="text-align: center">Общая сумма: ${total} BYN</p>

        <div style="display: flex; justify-content: space-between;">
            <form action="/cart/clear" method="post" style="margin-left: 20px;" onsubmit="showNotification()">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <button type="button" class="btn btn-warning" data-bs-toggle="modal" data-bs-target="#clearCartModal">Очистить корзину</button>
            </form>

            <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#orderModal" style="margin-right: 20px;">Оформить заказ</button>
        </div>

        <!-- Модальное окно для подтверждения удаления товара -->
        <div class="modal fade" id="deleteItemModal" tabindex="-1" aria-labelledby="deleteItemModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="deleteItemModalLabel">Подтверждение удаления</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <p>Вы уверены, что хотите удалить товар <span id="itemTitleToDelete"></span>?</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Отмена</button>
                        <form id="confirmDeleteForm" action="/cart/remove" method="post" onsubmit="showNotification()">
                            <input type="hidden" name="itemId" id="itemIdToDelete" value="">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            <button type="submit" class="btn btn-danger">Удалить</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Модальное окно для очистки корзины -->
        <div class="modal fade" id="clearCartModal" tabindex="-1" aria-labelledby="clearCartModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="clearCartModalLabel">Подтверждение очистки корзины</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <p>Вы уверены, что хотите очистить корзину?</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Отмена</button>
                        <form action="/cart/clear" method="post" onsubmit="showNotification()">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            <button type="submit" class="btn btn-danger">Очистить корзину</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Модальное окно для оформления заказа -->
        <div class="modal fade" id="orderModal" tabindex="-1" aria-labelledby="orderModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="/delivery/submit" method="post">
                        <div class="modal-header">
                            <h5 class="modal-title" id="orderModalLabel">Оформление заказа</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-3">
                                <label for="pickupPoint" class="form-label">Пункты выдачи:</label>
                                <select class="form-select" id="pickupPoint" name="pickupPointId" required>
                                    <option value="" disabled selected>Выберите пункт выдачи</option>
                                    <#list pickupPoints as point>
                                        <option value="${point.pickUpPointId}">${point.name!} - ${point.location!}, ${point.city.cityName!}</option>
                                    </#list>
                                </select>
                            </div>

                            <div>
                                <label class="form-label">Выберите товары для заказа:</label>
                                <#list cart as item>
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" value="${item.id}" id="item-${item.id}" name="selectedItems">
                                        <label class="form-check-label" for="item-${item.id}">
                                            ${item.product.title} (${item.quantity} шт.)
                                        </label>
                                    </div>
                                </#list>
                            </div>
                        </div>

                        <div class="modal-footer">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            <button type="submit" class="btn btn-primary">Подтвердить заказ</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- Уведомления -->
        <div id="notification" style="display: none;">
            <div class="alert alert-success d-flex align-items-center" role="alert">
                <svg class="bi flex-shrink-0 me-2" role="img" aria-label="Success:"><use xlink:href="#check-circle-fill"/></svg>
                <div>
                    Операция выполнена успешно!
                </div>
            </div>
        </div>

        <!-- SVG иконки Bootstrap -->
        <svg xmlns="http://www.w3.org/2000/svg" style="display: none;">
            <symbol id="check-circle-fill" viewBox="0 0 16 16">
                <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zm-3.97-3.03a.75.75 0 0 0-1.08.022L7.477 9.417 5.384 7.323a.75.75 0 0 0-1.06 1.06L6.97 11.03a.75.75 0 0 0 1.079-.02l3.992-4.99a.75.75 0 0 0-.01-1.05z"/>
            </symbol>
            <symbol id="info-fill" viewBox="0 0 16 16">
                <path d="M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16zm.93-9.412-1 4.705c-.07.34.029.533.304.533.194 0 .487-.07.686-.246l-.088.416c-.287.346-.92.598-1.465.598-.703 0-1.002-.422-.808-1.319l.738-3.468c.064-.293.006-.399-.287-.47l-.451-.081.082-.381 2.29-.287zM8 5.5a1 1 0 1 1 0-2 1 1 0 0 1 0 2z"/>
            </symbol>
            <symbol id="exclamation-triangle-fill" viewBox="0 0 16 16">
                <path d="M8.982 1.566a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767L8.982 1.566zM8 5c.535 0 .954.462.9.995l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995A.905.905 0 0 1 8 5zm.002 6a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"/>
            </symbol>
        </svg>
    </#if>

    <script>
        // Обработка нажатия на кнопку удаления
        const deleteItemModal = document.getElementById('deleteItemModal');
        deleteItemModal.addEventListener('show.bs.modal', event => {
            const button = event.relatedTarget;
            const itemId = button.getAttribute('data-item-id');
            const itemTitle = button.getAttribute('data-item-title');
            const modalTitle = deleteItemModal.querySelector('#itemTitleToDelete');
            const inputId = deleteItemModal.querySelector('#itemIdToDelete');
            modalTitle.textContent = itemTitle;
            inputId.value = itemId;
        });

        // Уведомления
        const showNotification = () => {
            const notification = document.getElementById('notification');
            notification.style.display = 'block';
            setTimeout(() => {
                notification.style.display = 'none';
            }, 3000); // Скрыть уведомление через 3 секунды
        };
    </script>
</@t.template>