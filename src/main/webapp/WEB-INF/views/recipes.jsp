<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:useBean id="recipes" scope="request" type="java.util.List"/>
<t:main title="Рецепты">
    <nav class="main-navigation">
        <div class="fr header-container">
            <h2>Рецептов: ${recipes.size()}</h2>
        </div>
    </nav>
    <t:recipe-list recipes="${recipes}"/>
</t:main>
