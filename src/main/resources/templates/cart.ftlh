<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Корзина заказов</h1>
    <hr>
    <#if cart?size == 0>
        <p style="text-align: center">Ваша корзина пуста.</p>
    <#else>
        <table class="table">
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
                        <form action="/cart/remove" method="post" style="display:inline;">
                            <input type="hidden" name="itemId" value="${item.id}">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            <button type="submit" class="btn btn-danger">Удалить</button>
                        </form>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>

        <p style="text-align: center">Общая сумма: ${total} BYN</p>

        <div style="text-align: center">
            <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#orderModal">Оформить заказ</button>
        </div>

        <div style="text-align: center">
            <form action="/cart/clear" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <button type="submit" class="btn btn-warning">Очистить корзину</button>
            </form>
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
    </#if>
</@t.template>
