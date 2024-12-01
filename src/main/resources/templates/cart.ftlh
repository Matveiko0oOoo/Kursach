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
            <a href="/delivery" class="btn btn-primary">Оформить заказ</a>
        </div>
        <div style="text-align: center">
            <form action="/cart/clear" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <button type="submit" class="btn btn-warning">Очистить корзину</button>
            </form>
        </div>
    </#if>
</@t.template>