<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Личный кабинет</h1>
    <hr>
    <br>
    <div style="text-align: center">
        <#if user??>
            <#if user.avatarImage?has_content>
                <img src="/images/user/${user.id}" style="border: 2px solid black; border-radius: 20px;" width="200" height="200"><br>
            <#else>
                <img src="/image/avatar.png" style="border: 2px solid black; border-radius: 20px;" width="200" height="200"><br>
            </#if>
            <br>
            <h2>${user.name!''}</h2>
            <br>
            <div>
                Электронная почта: <b>${user.email!''}</b><br>
                Номер телефона: <b>${user.numberPhone!''}</b>
            </div>
            <br>
            <#if user.isAdmin()>
                <a class="btn btn-dark mb-3" style="width: 100%" href="/admin">Панель администратора</a>
                <a class="btn btn-dark mb-3" style="width: 100%" href="/orders/all">Продажи</a>
                <a class="btn btn-dark mb-3" style="width: 100%" href="/admin/cities">Города, покрытые доставкой</a>
                <a class="btn btn-dark mb-3" style="width: 100%" href="/my/products">Мои товары</a>
                <a class="btn btn-dark mb-3" style="width: 100%" href="/pickup/points/add">Добавить новый пункт выдачи</a>
            </#if>
            <a class="btn btn-dark mb-3" style="width: 100%" data-bs-toggle="collapse" href="#collapseEditProfile" role="button" aria-expanded="false" aria-controls="collapseEditProfile">Редактировать профиль</a>
            <div class="collapse" id="collapseEditProfile">
                <div class="card card-body">
                    <form action="/profile/edit/${user.id}" method="post" enctype="multipart/form-data">
                        <div class="form-group">
                            <label for="username">Имя пользователя</label>
                            <input type="text" required class="form-control" id="username" name="name" value="${user.name!''}">
                        </div>
                        <div class="form-group">
                            <label for="phone">Номер телефона</label>
                            <input type="tel" required class="form-control" id="phone" name="numberPhone" value="${user.numberPhone!''}">
                        </div>
                        <div class="form-group">
                            <label for="avatar">Изображение профиля</label>
                            <input type="file" class="form-control mb-3" id="avatar" name="avatarImage">
                        </div>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <button type="submit" style="width: 100%" class="btn btn-dark">Сохранить изменения</button>
                    </form>
                </div>
            </div>
            <form action="/logout" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit" style="width: 100%" class="btn btn-danger mb-2">Выйти из аккаунта</button>
            </form>
        <#else>
            <h2>Пользователь не найден</h2>
        </#if>
    </div>
</@t.template>