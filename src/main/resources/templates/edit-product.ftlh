<#import "blocks/template.ftlh" as t>
<@t.template user>
<form action="/product/update/${product.id}" method="post" enctype="multipart/form-data">
    <div class="form-group">
        <label for="title">Название товара</label>
        <input type="text" required class="form-control" id="title" name="title" value="${product.title}">
    </div>
    <div class="form-group">
        <label for="description">Описание товара</label>
        <textarea required class="form-control" id="description" name="description">${product.description}</textarea>
    </div>
    <div class="form-group">
        <label for="price">Цена товара</label>
        <input type="number" required class="form-control" id="price" name="price" value="${product.price}">
    </div>
    <div class="form-group">
        <label for="city">Город</label>
        <select id="city" required name="city" class="form-control">
            <#list cities as city>
                <option value="${city.cityName}" <#if product.city == city.cityName>selected</#if>>${city.cityName}</option>
            </#list>
        </select>
    </div>
    <div class="form-group">
        <label for="file1">Первая фотография</label>
        <input type="file" class="form-control-file" id="file1" name="file1">
    </div>
    <div class="form-group">
        <label for="file2">Вторая фотография</label>
        <input type="file" class="form-control-file" id="file2" name="file2">
    </div>
    <div class="form-group">
        <label for="file3">Третья фотография</label>
        <input type="file" class="form-control-file" id="file3" name="file3">
    </div>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <button type="submit" class="btn btn-dark">Сохранить изменения</button>
</form>
</@t.template>