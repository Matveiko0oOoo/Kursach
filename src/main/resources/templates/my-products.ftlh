<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Мои товары</h1>
    <hr>

    <a class="btn btn-dark mt-3 mb-3" style="text-align: center; width: 100%; padding: 10px 0;"
       data-bs-toggle="collapse" href="#collapseAddProduct" role="button"
       aria-expanded="true" aria-controls="collapseAddProduct">
        Добавить новый товар
    </a>
    <div class="collapse" id="collapseAddProduct">
        <div class="card card-body">
            <form action="/product/create" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="productName">Название товара</label>
                    <input type="text" required class="form-control" id="productName" name="title">
                </div>
                <div class="form-group">
                    <label for="price">Цена товара</label>
                    <div class="input-group mb-3">
                        <input type="number" class="form-control" id="price" name="price" min="0.01" max="10000" step="0.01" placeholder="Введите цену" required>
                        <div class="input-group-append">
                            <span class="input-group-text">BYN</span>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label for="searchCity">Город</label>
                    <select id="searchCity" required name="city" class="form-control">
                        <option value="">Город не выбран</option>
                        <#list cities as city>
                            <option value="${city.cityName}">${city.cityName}</option>
                        </#list>
                    </select>
                </div>
                <div class="form-group">
                    <label for="description">Описание товара</label>
                    <textarea required class="form-control mb-3" id="description" name="description"></textarea>
                </div>
                <div class="form-group">
                    <div class="custom-file">
                        <input required type="file" name="file1" id="file1">
                        <label class="custom-file-label mb-3" for="file1">Первая фотография к товару</label>
                    </div>
                </div>
                <div class="form-group">
                    <div class="custom-file">
                        <input type="file" name="file2" id="file2">
                        <label class="custom-file-label mb-3" for="file2">Вторая фотография к товару</label>
                    </div>
                </div>
                <div class="form-group">
                    <div class="custom-file">
                        <input type="file" name="file3" id="file3">
                        <label class="custom-file-label mb-3" for="file3">Третья фотография к товару</label>
                    </div>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit" style="width: 100%; padding: 10px 0;" class="btn btn-dark mt-2 mb-2">Добавить</button>
            </form>
        </div>
    </div>

    <a href="/profile" class="btn btn-secondary mt-3">Назад к панели администратора</a>

    <div class="card-columns">
        <#list products as product>
            <a href="/product/${product.id}" class="product-link">
                <div class="card text-white bg-dark my-3 product">
                    <div class="card-top" style="display: flex; justify-content: space-between; align-items: center;">
                        <div style="flex: 1;">
                            <form action="/product/delete/${product.id}" method="post" style="display: inline;">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                <button type="submit" class="btn btn-danger btn-sm" style="margin-bottom: 10px; padding: 8px 12px;">Удалить</button>
                            </form>
                        </div>
                        <div style="flex: 1; text-align: center;">
                            <a href="/product/edit/${product.id}" class="btn btn-warning btn-sm" style="padding: 8px 12px; margin-bottom: 10px;">Изменить</a>
                        </div>
                        <div style="flex: 1; text-align: right;">
                            <a href="/product/${product.id}" class="btn btn-info btn-sm" style="padding: 8px 12px; margin-bottom: 10px;">Подробнее</a>
                        </div>
                    </div>
                    <#if product.mainImageId?has_content>
                        <img src="/images/${product.mainImageId}" class="card-img-top" alt="Изображение товара">
                    <#else>
                        <img src="/images/avatar.png" class="card-img-top" alt="Изображение по умолчанию">
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