<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Все заказы</h1>
    <hr>
    <div style="text-align: right; margin-bottom: 20px;">
        <select id="viewMode" class="form-select" style="width: 300px; display: inline-block;">
            <option value="list" selected>Показать все товары</option>
            <option value="grouped">Объединить по названию</option>
        </select>
        <!-- Кнопка для скачивания PDF отчета -->
        <a href="/orders/report/pdf" class="btn btn-primary" style="margin-left: 20px;">Скачать отчет PDF</a>
        <a href="/orders/report/xml" class="btn btn-primary" style="margin-left: 20px;">Скачать отчет XML</a>
        <a href="/orders/report/json" class="btn btn-primary" style="margin-left: 20px;">Скачать отчет JSON</a>
        <a href="/orders/report/docx" class="btn btn-primary" style="margin-left: 20px;">Скачать отчет DOCX</a>
        <a href="/orders/report/xlsx" class="btn btn-primary" style="margin-left: 20px;">Скачать отчет XLSX</a>
    </div>

    <div id="listView">
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
    </div>

    <div id="groupedView" style="display: none;">
        <#if groupedProducts?size == 0>
            <p style="text-align: center">Заказов пока нет.</p>
        <#else>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>Название товара</th>
                    <th>Количество</th>
                </tr>
                </thead>
                <tbody>
                <#list groupedProducts?keys as title>
                    <tr>
                        <td>${title}</td>
                        <td>${groupedProducts[title]}</td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </#if>
    </div>

    <hr>
    <div style="text-align: right; font-weight: bold; font-size: 18px;">
        Общее количество проданных товаров: ${totalQuantity} <br>
        Итоговая прибыль: ${totalRevenue} BYN
    </div>

    <script>
        const viewMode = document.getElementById('viewMode');
        const listView = document.getElementById('listView');
        const groupedView = document.getElementById('groupedView');

        viewMode.addEventListener('change', function () {
            if (this.value === 'list') {
                listView.style.display = 'block';
                groupedView.style.display = 'none';
            } else {
                listView.style.display = 'none';
                groupedView.style.display = 'block';
            }
        });
    </script>
</@t.template>