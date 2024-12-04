<#import "blocks/template.ftlh" as t>
<@t.template user>
    <div id="carouselExampleIndicators" class="carousel slide" data-bs-ride="carousel">
        <div class="carousel-indicators">
            <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
            <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="1" aria-label="Slide 2"></button>
            <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="2" aria-label="Slide 3"></button>
        </div>
        <div class="carousel-inner">
            <#list images as image>
                <div class="carousel-item <#if image.mainImage>active</#if>">
                    <svg class="bd-placeholder-img" width="100%" height="100%" xmlns="http://www.w3.org/2000/svg"
                         role="img" aria-label=" :  " preserveAspectRatio="xMidYMid slice" focusable="false">
                        <title></title>
                        <rect width="100%" height="100%" fill="#777"></rect>
                        <text x="50%" y="50%" fill="#777" dy=".3em"></text>
                    </svg>

                    <div class="container">
                        <div class="carousel-caption">
                            <a href="/images/${image.id}">
                                <img class="product" style="height: 100px" src="/images/${image.id}">
                            </a>
                        </div>
                    </div>
                </div>
            </#list>
        </div>
        <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Предыдущая</span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Следующая</span>
        </button>
    </div>

    <#if user?? && user.id??>
        <form action="/cart/add" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="productId" value="${product.id}">
            <label for="quantity">Количество:</label>
            <input type="number" name="quantity" id="quantity" value="1" min="1">
            <button type="submit" class="btn btn-primary">Добавить в корзину</button>
        </form>
    <#else>
        <p style="text-align: center">Чтобы добавить товар в корзину, <a href="/login">войдите в систему</a> или <a href="/registration">зарегистрируйтесь</a>.</p>
    </#if>

    <hr>
    <p><h1 style="text-align: center">${product.title} | ${product.price} BYN</h1></p>
    <p style="text-align: center">${product.description}</p>
    <p style="text-align: center">Номер телефона продавца: <b>${authorProduct.numberPhone}</b></p>

    <hr>
    <p style="text-align: center">
        <a href="/user/${authorProduct.id}">
            <#if user.avatarImage?has_content>
                <img src="/images/user/${user.id}" style="border:2px solid black; border-radius: 20px" width="30"
                     height="30"><br>
            <#else>
                <img src="/image/avatar.png" style="border:2px solid black; border-radius: 20px" width="30" height="30">
                <br>
            </#if> ${authorProduct.name}
        </a>
    </p>

</@t.template>