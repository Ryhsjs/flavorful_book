<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%--@elvariable id="error" type="java.lang.String"--%>
<%--@elvariable id="email" type="java.lang.String"--%>
<%--@elvariable id="username" type="java.lang.String"--%>
<%--@elvariable id="emailState" type="java.lang.String"--%>
<%--@elvariable id="usernameState" type="java.lang.String"--%>
<%--@elvariable id="passwordState" type="java.lang.String"--%>
<t:layout title="Регистрация">
    <t:auth action="/signup">
        <c:if test="${not empty error}">
            <div class="error-message">
                <i class="fa-solid fa-circle-info error-icon"></i>
                <p><c:out value="${error}"/></p>
            </div>
        </c:if>
        <label for="email">
            Email:
            <span class="text-error">${(not empty error) && (not empty emailState) ? emailState : ""}</span>
            <input class="full-width" type="email" id="email" name="email" placeholder="Введите"
                   value="${not empty email ? email : ''}">
        </label>
        <label for="username">
            Имя пользователя:
            <span class="text-error">${(not empty error) && (not empty usernameState) ? usernameState : ""}</span>
            <input class="full-width" type="text" id="username" name="username" placeholder="Введите"
                   value="${not empty username ? username : ''}">
        </label>
        <label for="password">
            Пароль:
            <span class="text-error">${(not empty error) && (not empty passwordState) ? passwordState : ""}</span>
            <input class="full-width" type="password" id="password" name="password" placeholder="Введите">
        </label>
        <label for="passwordRepeat">
            Повторите пароль:
            <input class="full-width" type="password" id="passwordRepeat" name="passwordRepeat"
                   placeholder="Введите">
        </label>
    </t:auth>
</t:layout>