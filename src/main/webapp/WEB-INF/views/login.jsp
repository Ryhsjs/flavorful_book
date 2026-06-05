<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<t:layout title="Вход">
    <t:auth action="/login">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <c:if test="${param.error != null}">
            <div class="error-message">
                <i class="fa-solid fa-circle-info error-icon"></i>
                <p>Неверное имя пользователя или пароль</p>
            </div>
        </c:if>
        <label for="username">
            Email:
            <input class="full-width" type="email" id="username" name="username" placeholder="Введите">
        </label>
        <label for="password">
            Пароль:
            <input class="full-width" type="password" id="password" name="password" placeholder="Введите">
        </label>
    </t:auth>
</t:layout>
