<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Поиск товаров</h1><hr>
    <!-- Форма фильтрации и сортировки -->
    <form method="get" action="/">
        <div class="form-row">
            <div class="form-group col-md-2">
                <label for="searchCity">Город:</label>
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
            <div class="form-group col-md-12">
                <label for="searchWord">Поиск по объявлениям:</label>
                <input type="text" name="searchWord" <#if searchWord??>value="${searchWord}"</#if> class="form-control mb-4" id="searchWord">
            </div>
            <button style="width: 100%" type="submit" class="btn btn-dark">Поиск</button>
        </div>
    </form>
    <hr>

    <!-- Отображение списка продуктов -->
    <div class="row">
        <#list products as product>
            <div class="col-md-4 d-flex align-items-stretch mb-4">
                <a href="/product/${product.id}" class="product-link flex-fill">
                    <div class="card text-white bg-dark product flex-fill" style="display: flex; flex-direction: column; height: 100%;">
                        <img src="/images/${product.mainImageId}" class="card-img-top" alt="Product Image">
                        <div class="card-body d-flex flex-column flex-grow-1">
                            <h5 class="card-title text-white">${product.title}</h5>
                            <p class="card-text text-white" style="margin: 0;">${product.price} BYN</p>
                            <div class="d-flex justify-content-between align-items-center mt-auto">
                                <span class="text-white">${product.city}</span>
                                <#if user?? && user.id??>
                                    <form action="/cart/add" method="post" style="display: inline;">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                        <input type="hidden" name="productId" value="${product.id}">
                                        <input type="hidden" name="quantity" value="1"> <!-- Добавляем поле для количества -->
                                        <button type="submit" class="add-to-cart" style="border: none; background: none; cursor: pointer;">
                                            <img src="/image/CART_onProduct.png" alt="Добавить в корзину" style="width: 24px; height: 24px;">
                                        </button>
                                    </form>
                                <#else>
                                    <span class="text-danger" style="cursor: pointer;" onclick="alert('Пожалуйста, войдите в систему, чтобы добавить товары в корзину.')">
                                    <img src="/image/CART_onProduct.png" alt="Добавить в корзину" style="width: 24px; height: 24px;">
                                </span>
                                </#if>
                            </div>
                        </div>
                    </div>
                </a>
            </div>
        </#list>
    </div>
</@t.template>