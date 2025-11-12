<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page isErrorPage="true" %>
<t:layout title="404">
    <div class="fr error-page">
        <div class="fc popup error">
            <p class="error-status">404</p>
            <p>Ничего не найдено</p>
            <p>${requestScope.errorMessage}</p>
            <footer>
                <button class="plain-button" onclick="goTo('/')">Вернуться на главную</button>
            </footer>
        </div>
    </div>
</t:layout>
