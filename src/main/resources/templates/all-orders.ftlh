<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Все заказы</h1>
    <hr>
    <#if products?size == 0>
        <p style="text-align: center">Заказов пока нет.</p>
    <#else>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>Название товара</th>
                <th>Цена</th>
            </tr>
            </thead>
            <tbody>
            <#list products as product>
                <tr>
                    <td>${product.title}</td>
                    <td>${product.price} руб.</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </#if>
</@t.template>
