<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<jsp:useBean id="recipe" scope="request" type="ru.itis.flavorful_book.dto.RecipeInfoDTO"/>
<jsp:useBean id="reviews" scope="request" type="java.util.List"/>
<%--@elvariable id="recipeCategories" type="java.util.List"--%>
<%--@elvariable id="recipeIngredients" type="java.util.List"--%>
<%--@elvariable id="userReview" type="ru.itis.flavorful_book.dto.ReviewDTO"--%>
<t:main title="${recipe.title()}">
    <div class="fc recipe-info mb">
        <header>
            <div class="fr header-container mb">
                <button class="plain-button" onclick="cancel()">Назад</button>
                <c:if test="${not empty currentUser}">
                    <c:choose>
                        <c:when test="${currentUser.id == recipe.userId()}">
                            <button class="filled-button"
                                    onclick="goTo('/recipes/' + ${recipe.id()} + '/edit')">Изменить
                            </button>
                        </c:when>
                        <c:otherwise>
                            <t:favorites-button isFavorite="${isFavorite}"/>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </div>
            <figure class="recipe-info-figure">
                <t:img-recipe url="${recipe.imageUrl()}"/>
            </figure>

            <div class="fr recipe-stats">
                <h1><c:out value="${recipe.title()}"/></h1>
                <c:if test="${recipe.rating() != null}">
                    <h2 class="fr stars">
                        <i class="fa-solid fa-star"></i>
                        <b>${recipe.rating()}</b>
                    </h2>
                </c:if>
                <c:if test="${recipe.rating() == null}">
                    <p class="text-subtle"><b>(Пока нет оценки)</b></p>
                </c:if>
            </div>

            <div class="fr icon-text-container">
                <figure class="user-figure">
                    <t:img-user size="fa-2x" url="${recipe.avatarUrl()}"/>
                </figure>
                <h2><c:out value="${recipe.username()}"/></h2>
            </div>

            <p class="text-subtle">
                ${recipe.createdAt().toLocalDate()}
                <c:if test="${not empty recipe.updatedAt()}">
                    (Изменено ${recipe.updatedAt().toLocalDate()})
                </c:if>
            </p>

            <p>Порций: ${recipe.servings()}</p>
            <p>${recipe.totalCookingTime()} мин (Ваши ${recipe.activeCookingTime()} мин)</p>
        </header>

        <section>
            <p><b>Описание: </b>
                <c:out value="${not empty recipe.description() ? recipe.description() : 'нет'}"/>
            </p>
            <h2>Категории</h2>
            <div class="fr categories">
                <c:forEach var="category" items="${recipeCategories}">
                    <div class="category"><c:out value="${category.name}"/></div>
                </c:forEach>
            </div>
        </section>

        <section class="ingredients">
            <h2>Ингредиенты</h2>
            <ul class="ingredients-list">
                <c:forEach var="ingredient" items="${recipeIngredients}">
                    <li>
                        <c:out value="${ingredient.name()}"/>
                        <c:if test="${not empty ingredient.notes()}">
                            (<c:out value="${ingredient.notes()}"/>)
                        </c:if>
                        — ${ingredient.quantity()} ${ingredient.unit().unit}
                    </li>
                </c:forEach>
            </ul>
        </section>

        <section>
            <h2>Рецепт</h2>
            <p class="text"><c:out value="${recipe.instructions()}"/></p>
        </section>

        <div class="fr recipe-stats">
            <div class="fr icon-text-container">
                <i class="fa-solid fa-eye fa-2x"></i>
                <p>${recipe.views()}</p>
            </div>
            <div class="fr icon-text-container">
                <i class="fa-solid fa-heart fa-2x"></i>
                <p>${recipe.likes()}</p>
            </div>
            <div class="fr icon-text-container">
                <i class="fa-solid fa-comment fa-2x"></i>
                <p>${reviews.size()}</p>
            </div>
        </div>
        <hr>
    </div>

    <div class="fc reviews">
        <h2>Отзывы</h2>
        <t:review-list reviews="${reviews}" recipeId="${recipe.id()}"
                       userReview="${not empty userReview ? userReview : null}" authorId="${recipe.userId()}"/>
    </div>
</t:main>
