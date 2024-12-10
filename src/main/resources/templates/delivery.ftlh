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
                <th>Действие</th>
            </tr>
            </thead>
            <tbody>
        <#list deliveries as delivery>
            <tr>
                <td>${delivery.deliveryName}</td>
                <td>${delivery.admissionDate! "Дата не указана"}</td>
                <td>${delivery.arrivalDate! "Дата не указана"}</td>
                <td>${delivery.placeInStock! "Дата не указана"}</td>
                <td>
                    <button type="button" class="btn btn-link" data-bs-toggle="modal" data-bs-target="#detailsModal-${delivery.deliveryId}">
                        Подробнее...
                    </button>
                </td>
            </tr>
        </#list>

        <#list deliveries as delivery>
            <div class="modal fade" id="detailsModal-${delivery.deliveryId}" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Информация о доставке</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Закрыть"></button>
                        </div>
                        <div class="modal-body">
                            <p><strong>Название доставки:</strong> ${delivery.deliveryName}</p>
                            <p><strong>Дата отправки:</strong> ${delivery.admissionDate! "Дата не указана"}</p>
                            <p><strong>Ожидаемая дата прибытия:</strong> ${delivery.arrivalDate! "Дата не указана"}</p>
                            <p><strong>Место на складе:</strong> ${delivery.placeInStock! "Не указано"}</p>

                            <#if delivery.pickUpPoint??>
                                <p><strong>Пункт выдачи:</strong> ${delivery.pickUpPoint.name!} - ${delivery.pickUpPoint.location!}, ${delivery.pickUpPoint.city.cityName!}</p>
                            <#else>
                                <p><strong>Пункт выдачи:</strong> Не указан</p>
                            </#if>

                            <p><strong>Список товаров:</strong></p>
                            <ul>
                                <#list deliveryProducts[delivery.deliveryId?string] as product>
                                    <li>${product.title} - ${product.price} BYN</li>
                                </#list>
                            </ul>

                            <p><em>Оплата осуществляется при получении.</em></p>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Закрыть</button>
                        </div>
                    </div>
                </div>
            </div>
        </#list>
    </#if>
</@t.template>