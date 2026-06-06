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

    <c:if test="${not empty mealOfTheDay}">
        <div class="meal-of-the-day">
            <h2>Блюдо дня</h2>
            <p class="text-subtle">Вдохновение от мировой кухни</p>
            <div class="meal-card fr">
                <img src="${mealOfTheDay.thumbnailUrl}" alt="${mealOfTheDay.name}" class="meal-thumb"/>
                <div class="meal-info fc">
                    <h3>${mealOfTheDay.name}</h3>
                    <p>
                        <c:if test="${not empty mealOfTheDay.category}">
                            <span class="tag">${mealOfTheDay.category}</span>
                        </c:if>
                        <c:if test="${not empty mealOfTheDay.area}">
                            <span class="tag">${mealOfTheDay.area}</span>
                        </c:if>
                    </p>
                    <c:if test="${not empty mealOfTheDay.youtubeUrl}">
                        <a href="${mealOfTheDay.youtubeUrl}" target="_blank" rel="noopener noreferrer" class="plain-button">
                            Смотреть на YouTube
                        </a>
                    </c:if>
                    <c:if test="${not empty mealOfTheDay.sourceUrl}">
                        <a href="${mealOfTheDay.sourceUrl}" target="_blank" rel="noopener noreferrer" class="plain-button">
                            Источник рецепта
                        </a>
                    </c:if>
                </div>
            </div>
        </div>
    </c:if>
</t:main>
