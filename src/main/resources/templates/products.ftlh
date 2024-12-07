<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Поиск товаров</h1><hr>
    <!-- Форма фильтрации и сортировки -->
    <form method="get" action="/">
        <div class="form-row">
            <div class="form-group col-md-2">
                <label for="searchCity">Город</label>
                <select id="searchCity" name="searchCity" class="form-control">
                    <option value="">Город не выбран</option>
                    <#list cities as city>
                        <option value="${city.cityName}" <#if searchCity! == city.cityName>selected</#if>>${city.cityName}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group col-md-2">
                <label for="sortOrder">Сортировать по:</label>
                <select id="sortOrder" name="sortOrder" class="form-control">
                    <option value="">Без сортировки</option>
                    <option value="priceAsc" <#if sortOrder! == "priceAsc">selected</#if>>Цене (по возрастанию)</option>
                    <option value="priceDesc" <#if sortOrder! == "priceDesc">selected</#if>>Цене (по убыванию)</option>
                    <option value="nameAsc" <#if sortOrder! == "nameAsc">selected</#if>>Названию (по алфавиту)</option>
                    <option value="nameDesc" <#if sortOrder! == "nameDesc">selected</#if>>Названию (обратно алфавиту)</option>
                </select>
            </div>
            <div class="form-group col-md-8">
                <label for="searchWord">Поиск по объявлениям</label>
                <input type="text" name="searchWord" <#if searchWord??>value="${searchWord}"</#if> class="form-control"
                       id="searchWord">
            </div>
            <button style="width: 100%" type="submit" class="btn btn-dark">Поиск</button>
        </div>
    </form>
    <hr>
    <!-- Отображение списка продуктов -->
    <div class="card-columns">
        <#list products as product>
            <a href="/product/${product.id}" class="product-link">
                <div class="card text-white bg-dark my-3 product">
                    <#if product.mainImageId?has_content>
                        <img src="/images/${product.mainImageId}" class="card-img-top" alt="Product Image">
                    <#else>
                        <img src="/images/avatar.png" class="card-img-top" alt="Default Image">
                    </#if>
                    <div class="card-body">
                        <h5 class="card-title">${product.title}</h5>
                        <p class="card-text">${product.price} BYN</p>
                    </div>
                    <div class="card-footer">
                        ${product.city}
                    </div>
                </div>
            </a>
        <#else>
            <h1 style="text-align: center">Не найдено</h1>
        </#list>
    </div>
</@t.template>
