<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<t:main title="Добро пожаловать">
    <div class="fc welcome">
        <h1>Вкусный букварь</h1>
        <p class="text-subtle">Находите рецепты, сохраняйте избранное, делитесь своими блюдами</p>
        <div class="fr">
            <button class="filled-button" onclick="goTo('/recipes')">Смотреть рецепты</button>
            <c:if test="${empty currentUser}">
                <button class="plain-button" onclick="goTo('/signup')">Зарегистрироваться</button>
            </c:if>
        </div>
    </div>
</t:main>
