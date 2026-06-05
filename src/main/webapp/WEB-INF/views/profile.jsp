<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:useBean id="section" scope="request" type="java.lang.String"/>
<jsp:useBean id="recipes" scope="request" type="java.util.List"/>
<jsp:useBean id="user" scope="request" type="ru.itis.flavorful_book.entity.User"/>
<t:main title="Профиль">
    <header class="fr header-container">
        <section class="fr user">
            <figure class="user-figure">
                <t:img-user size="fa-6x" url="${user.avatarUrl}"/>
            </figure>

            <div class="user-info">
                <h2><c:out value="${user.username}"/></h2>
                <p class="text-subtle">${user.createdAt.toLocalDate()}</p>
            </div>
            <i class="fa-solid fa-pen-to-square pointer fa-2x" onclick="showPopup()"></i>
        </section>

        <i class="fa-solid fa-right-from-bracket pointer fa-2x" onclick="logout()"></i>
    </header>

    <nav class="fr pages-nav">
        <a class="page-link ${section.equals('my') ? 'active' : ''}"
           onclick="goTo('/profile', {'section': 'my'})">Мои рецепты</a>
        <a class="page-link ${section.equals('favorites') ? 'active' : ''}"
           onclick="goTo('/profile', {'section': 'favorites'})">Избранное</a>
    </nav>

    <nav class="fr nav-container">
        <c:if test="${section == 'my'}">
            <button class="plain-button" onclick="goTo('/recipes/new')">Создать</button>
        </c:if>
        <p>Рецептов: ${recipes.size()}</p>
    </nav>
    <t:recipe-list recipes="${recipes}"/>

    <c:if test="${empty recipes}">
        <h2 class="text-subtle">Здесь пока ничего нет</h2>
    </c:if>

    <t:profile-edit avatarUrl="${user.avatarUrl}"/>
</t:main>
