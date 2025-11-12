<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<t:layout title="403">
    <div class="fr error-page">
        <div class="fc popup error">
            <p class="error-status">403</p>
            <p>Отказано в доступе</p>
            <footer>
                <button class="plain-button" onclick="goTo('/')">Вернуться на главную</button>
            </footer>
        </div>
    </div>
</t:layout>
