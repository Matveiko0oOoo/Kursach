<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Ваши доставки</h1>
    <hr>
    <#if deliveries?size == 0>
        <p style="text-align: center">У вас пока нет оформленных доставок.</p>
    <#else>
        <table class="table">
            <thead>
            <tr>
                <th>Название доставки</th>
                <th>Дата отправления</th>
                <th>Дата прибытия</th>
                <th>Место на складе</th>
                <th>Товары</th>
            </tr>
            </thead>
            <tbody>
            <#list deliveries as delivery>
                <tr>
                    <td>${delivery.deliveryName}</td>
                    <td>${delivery.admissionDate}</td>
                    <td>${delivery.arrivalDate}</td>
                    <td>${delivery.placeInStock}</td>
                    <td>
                        <ul>
                            <#list delivery.items as item>
                                <li>
                                    <img src="/images/${item.product.mainImageId}" alt="${item.product.title}" width="50" height="50">
                                    ${item.product.title} (Количество: ${item.quantity})
                                </li>
                            </#list>
                        </ul>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </#if>
</@t.template>
