<#import "blocks/template.ftlh" as t>
<@t.template user>
    <div class="container mt-4">
        <div class="row">
            <div class="col-md-6">
                <div id="carouselExampleIndicators" class="carousel slide" data-bs-ride="carousel">
                    <div class="carousel-indicators">
                        <#list images as image>
                            <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="${image_index}" class="<#if image.mainImage>active</#if>" aria-current="true" aria-label="Slide ${image_index + 1}"></button>
                        </#list>
                    </div>
                    <div class="carousel-inner">
                        <#list images as image>
                            <div class="carousel-item <#if image.mainImage>active</#if>">
                                <img src="/images/${image.id}" class="d-block w-100 product-image" alt="Slide">
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
            </div>

            <div class="col-md-6">
                <h1 class="text-start">${product.title}</h1>
                <h4 class="text-start">${product.price} BYN</h4>
                <p class="text-start">${product.description}</p>
                <p class="text-start">Номер телефона продавца: <b>${authorProduct.numberPhone}</b></p>

                <hr>

                <#if user?? && user.id??>
                    <form id="cartForm" action="/cart/add" method="post" class="text-start" onsubmit="return validateQuantity()"> <!-- Добавлена функция валидации -->
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="hidden" name="productId" value="${product.id}">
                        <label for="quantity">Количество:</label>
                        <input type="number" name="quantity" id="quantity" value="1" min="1" max="99" class="mx-2" style="width: 60px;">
                        <button type="submit" class="btn btn-primary">Добавить в корзину</button>
                        <p id="error-message" style="color: red; display: none;"></p> <!-- Сообщение об ошибке -->
                    </form>
                <#else>
                    <p style="text-align: left">Чтобы добавить товар в корзину, <a href="/login">войдите в систему</a> или <a href="/registration">зарегистрируйтесь</a>.</p>
                </#if>

                <hr>
                <p class="text-start">
                    <a href="/user/${authorProduct.id}">
                        <#if authorProduct.avatarImage?has_content>
                            <img src="/images/user/${authorProduct.id}" style="border:2px solid black; border-radius: 20px" width="30" height="30"><br>
                        <#else>
                            <img src="/image/avatar.png" style="border:2px solid black; border-radius: 20px" width="30" height="30">
                        </#if>
                        ${authorProduct.name}
                    </a>
                </p>
            </div>
        </div>
    </div>

    <script>
        function validateQuantity() {
            const quantityInput = document.getElementById('quantity');
            const errorMessage = document.getElementById('error-message');

            if (quantityInput.value > 99) {
                errorMessage.textContent = 'Выбрано слишком большое количество товаров. Максимум 99.';
                errorMessage.style.display = 'block';
                return false; // Отмена отправки формы
            } else {
                errorMessage.style.display = 'none'; // Скрыть сообщение об ошибке
                return true; // Разрешить отправку формы
            }
        }
    </script>
</@t.template>