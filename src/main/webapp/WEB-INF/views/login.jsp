<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%--@elvariable id="error" type="java.lang.String"--%>
<%--@elvariable id="email" type="java.lang.String"--%>
<t:layout title="Вход">
    <t:auth action="/login">
        <c:if test="${not empty error}">
            <div class="error-message">
                <i class="fa-solid fa-circle-info error-icon"></i>
                <p><c:out value="${error}"/></p>
            </div>
        </c:if>
        <label for="email">
            Email:
            <input class="full-width" type="email" id="email" name="email" placeholder="Введите"
                   value="${not empty email ? email : ''}">
        </label>
        <label for="password">
            Пароль:
            <input class="full-width" type="password" id="password" name="password" placeholder="Введите">
        </label>
    </t:auth>
</t:layout>