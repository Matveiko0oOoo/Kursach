<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Все заказы</h1>
    <hr>
    <div style="text-align: right; margin-bottom: 20px;">
        <select id="viewMode" class="form-select" style="width: 300px; display: inline-block;">
            <option value="list" selected>Показать все товары</option>
            <option value="grouped">Объединить по названию</option>
        </select>
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
            <table class="table table-bordered table-success" style="border-radius: 10px; overflow: hidden;">
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
        <a href="/profile" class="btn btn-secondary mt-3">Назад к панели администратора</a>
    </div>

    <div id="groupedView" style="display: none;">
        <#if groupedProducts?size == 0>
            <p style="text-align: center">Заказов пока нет.</p>
        <#else>
            <table class="table table-bordered table-success" style="border-radius: 10px; overflow: hidden;">
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
        <a href="/profile" class="btn btn-secondary mt-3">Назад к панели администратора</a>

    </div>

    <hr>
    <div style="display: flex; justify-content: space-between; align-items: center; font-weight: bold; font-size: 18px;">
        <div>
            Общее количество проданных товаров: ${totalQuantity} <br>
            Итоговая прибыль: ${totalRevenue} BYN
        </div>
        <button id="analyticsButton" class="btn btn-info">Аналитика</button>
    </div>
<hr>
    <div id="analyticsContainer" style="display: none; text-align: center; margin-top: 20px;">
        <h3>Статистика заказов</h3>
        <canvas id="analyticsChart" width="100" height="100"></canvas>
    </div>
    <hr>
    <button id="salesChartButton" class="btn btn-info" style="margin-left: 20px;">График продаж</button>
    <div id="salesChartContainer" style="display: none; text-align: center; margin-top: 20px;">
        <h3>График продаж</h3>
        <canvas id="salesChart" width="100" height="100"></canvas>
    </div>

    <script>
        const salesChartButton = document.getElementById('salesChartButton');
        const salesChartContainer = document.getElementById('salesChartContainer');
        const salesChartCtx = document.getElementById('salesChart').getContext('2d');
        let salesChart;

        salesChartButton.addEventListener('click', function () {
            if (salesChartContainer.style.display === 'none') {
                salesChartContainer.style.display = 'block';

                // Данные для графика продаж из сервера
                fetch('/orders/sales-analytics')
                    .then(response => response.json())
                    .then(data => {
                        if (salesChart) salesChart.destroy(); // Удаляем старую диаграмму
                        salesChart = new Chart(salesChartCtx, {
                            type: 'line', // Линейный график
                            data: {
                                labels: data.dates, // Даты на оси X
                                datasets: [{
                                    label: 'Количество проданных товаров',
                                    data: data.sales, // Данные по продажам
                                    borderColor: '#4CAF50', // Цвет линии
                                    fill: false,
                                    tension: 0.1
                                }]
                            },
                            options: {
                                responsive: true,
                                maintainAspectRatio: true,
                                aspectRatio: 2.5,
                                scales: {
                                    x: { title: { display: true, text: 'Дата' } },
                                    y: { title: { display: true, text: 'Количество' } }
                                }
                            }
                        });
                    });
            } else {
                salesChartContainer.style.display = 'none';
            }
        });
    </script>

    <hr>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script>
        const analyticsButton = document.getElementById('analyticsButton');
        const analyticsContainer = document.getElementById('analyticsContainer');
        const analyticsChartCtx = document.getElementById('analyticsChart').getContext('2d');
        let chart;

        analyticsButton.addEventListener('click', function () {
            if (analyticsContainer.style.display === 'none') {
                analyticsContainer.style.display = 'block';

                // Данные для диаграммы из сервера
                fetch('/orders/analytics')
                    .then(response => response.json())
                    .then(data => {
                        if (chart) chart.destroy(); // Удаляем старую диаграмму
                        chart = new Chart(analyticsChartCtx, {
                            type: 'pie',
                            data: {
                                labels: data.labels,
                                datasets: [{
                                    data: data.values,
                                    backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4CAF50', '#FF9F40', '#FFC107', '#8E44AD', '#00BCD4', '#E91E63', '#009688'],
                                }]
                            },
                            options: {
                                responsive: true,
                                maintainAspectRatio: true,
                                aspectRatio: 2.5,
                            }
                        });
                    });
            } else {
                analyticsContainer.style.display = 'none';
            }
        });
    </script>

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
