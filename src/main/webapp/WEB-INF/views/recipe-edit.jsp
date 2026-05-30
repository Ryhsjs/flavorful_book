<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%--@elvariable id="recipe" type="ru.itis.flavorful_book.DTO.RecipeSaveDTO"--%>
<%--@elvariable id="error" type="ru.itis.flavorful_book.exception.IllegalRecipeArgumentException"--%>
<%--@elvariable id="user" type="ru.itis.flavorful_book.entity.User"--%>
<%--@elvariable id="errorImage" type="java.lang.IllegalArgumentException"--%>
<jsp:useBean id="categories" scope="request" type="java.util.List"/>
<jsp:useBean id="ingredients" scope="request" type="java.util.List"/>
<jsp:useBean id="units" scope="request" type="java.util.List"/>
<t:main title="${not empty recipe.id() ? recipe.title().concat(' (Изменение)') : 'Написание рецепта'}">

    <input type="hidden" id="recipeId" value="${recipe.id()}">

    <input type="hidden" id="userId" value="${user.id}">

    <input type="hidden" id="imageUrl" value="${recipe.imageUrl()}">

    <input type="hidden" id="type" value="recipe">

    <header>
        <div class="fr header-container mb">
            <button class="plain-button" onclick="cancel()">Назад</button>
            <c:choose>
                <c:when test="${not empty recipe and recipe.id() != null}">
                    <button class="del-button" onclick="deleteRecipe(${recipe.id()})">Удалить</button>
                </c:when>
                <c:otherwise>
                    <div></div>
                </c:otherwise>
            </c:choose>

        </div>
        <section class="fr recipe-start">
            <div class="fc icon-text-container">
                <c:if test="${not empty errorImage}">
                    <div class="error-message">
                        <i class="fa-solid fa-circle-info error-icon"></i>
                        <p><c:out value="${errorImage}"/></p>
                    </div>
                </c:if>
                <figure class="recipe-info-figure">
                    <t:img-recipe url="${not empty recipe ? recipe.imageUrl() : null}"/>
                </figure>
                <label>
                    <input type="file" accept="image/*" name="recipeImage" id="image" onchange="uploadImage()">
                </label>
            </div>

            <div>
                <label for="title">
                    <c:if test="${not empty error and not empty error.titleState}">
                        <p class="text-error"><c:out value="${error.titleState}"/></p>
                    </c:if>

                    <input class="title-input" name="title" id="title" type="text" required
                           placeholder="Введите название"
                           value="${not empty recipe ? recipe.title(): ''}">
                </label>

                <c:if test="${not empty error.cookingTimeState}">
                    <p class="text-error">
                        <c:out value="${error.cookingTimeState}"/>
                    </p>
                </c:if>
                <div class="stats-grid">
                    <p>Общее время готовки (мин):
                        <c:if test="${not empty error.totalCookingTimeState}">
                            <br>
                            <span class="text-error">
                                <c:out value="${error.totalCookingTimeState}"/>
                            </span>
                        </c:if>
                    </p>
                    <label for="totalCookingTime">
                        <input class="full-width" type="number" name="totalCookingTime"
                               id="totalCookingTime" min="0" max="1440" required
                               value="${not empty recipe ? recipe.totalCookingTime() : ''}">
                    </label>

                    <p>Активное время готовки (мин):
                        <c:if test="${not empty error.activeCookingTimeState}">
                            <br>
                            <span class="text-error">
                                <c:out value="${error.activeCookingTimeState}"/>
                            </span>
                        </c:if>
                    </p>
                    <label for="activeCookingTime">
                        <input class="full-width" type="number" name="activeCookingTime"
                               id="activeCookingTime" min="0" max="1440" required
                               value="${not empty recipe ? recipe.activeCookingTime(): ''}">
                    </label>

                    <p>Количество порций:
                        <c:if test="${not empty error.servingsState}">
                            <br>
                            <span class="text-error">
                                <c:out value="${error.servingsState}"/>
                            </span>
                        </c:if>
                    </p>
                    <label for="servings">
                        <input class="full-width" type="number" name="servings"
                               id="servings" min="0" max="100" required
                               value="${not empty recipe ? recipe.servings() : ''}">
                    </label>
                </div>
            </div>
        </section>
    </header>

    <section>
        <h2>Категории</h2>
        <div class="fr categories" id="categories-section">
            <label for="categories-add">
                <select class="category" id="categories-add">
                    <option value="0">Добавить</option>
                    <hr>
                    <c:forEach var="category" items="${categories}">
                        <option value="${category.id}"><c:out value="${category.name}"/></option>
                    </c:forEach>
                </select>
            </label>
            <script>
                const existingCategories =
                ${not empty recipe.categories() ? recipe.categories() : []}
            </script>
        </div>
    </section>

    <section>
        <h2>Описание</h2>
        <label for="description">
                <textarea rows="5" name="description" id="description"><c:out
                        value="${recipe.description()}"/></textarea>
        </label>
    </section>

    <section class="ingredients">
        <h2>Ингредиенты</h2>
        <div class="ingredients-grid" id="ingredients-section">
            <div class="ingredient-row">
                <select name="ingredient" id="ingredient">
                    <option value="0">Продукт</option>
                    <hr>
                    <c:forEach var="ingredient" items="${ingredients}">
                        <option value="${ingredient.id}"><c:out value="${ingredient.name}"/></option>
                    </c:forEach>
                </select>
                <label>
                    <input name="ingredientNotes" id="ingredientNotes" type="text" placeholder="Примечание">
                </label>
                <p>—</p>
                <input name="ingredientQuantity" id="ingredientQuantity" min="0" type="number">

                <select name="ingredientUnit" id="ingredientUnit">
                    <c:forEach items="${units}" var="unit">
                        <%--@elvariable id="unit" type="ru.itis.flavorful_book.entity.enums.Unit"--%>
                        <option value="${unit.toString()}"><c:out value="${unit.unit}"/></option>
                    </c:forEach>
                </select>
                <button class="filled-button" id="ingredients-add" type="button">+</button>
                <div></div>
            </div>
        </div>

        <script>
            const existingIngredients = [
                <c:forEach var="ingredient" items="${recipe.ingredients()}">
                {
                    "id": ${ingredient.id()},
                    "quantity": ${ingredient.quantity()},
                    "unit": "${ingredient.unit()}",
                    "notes": "${ingredient.notes()}"
                },
                </c:forEach>
            ]
        </script>
    </section>

    <section>
        <h2>Рецепт</h2>
        <c:if test="${not empty error and not empty error.instructionsState}">
            <div class="error-message">
                <i class="fa-solid fa-circle-info error-icon"></i>
                <p><c:out value="${error.instructionsState}"/></p>
            </div>
        </c:if>
        <label for="instructions">
                <textarea rows="15" name="instructions" id="instructions" required><c:out
                        value="${recipe.instructions()}"/></textarea>
        </label>
    </section>

    <footer class="fr">
        <div class="foot">
            <button class="plain-button" type="button" onclick="cancel()">Отмена</button>
            <button class="filled-button" type="button" id="post-button">Сохранить</button>
        </div>
    </footer>
</t:main>
<script src="${pageContext.servletContext.contextPath}/js/RecipeEdit.js"></script>
