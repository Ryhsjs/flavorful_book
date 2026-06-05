<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<t:layout title="Регистрация">
    <t:popup>
        <nav class="fr pages-nav">
            <a class="page-link active" onclick="goTo('/signup')">Регистрация</a>
            <a class="page-link" onclick="goTo('/login')">Вход</a>
        </nav>
        <form:form class="fc" modelAttribute="form" action="${pageContext.request.contextPath}/signup" method="post">
            <label for="email">
                Email:
                <form:errors path="email" cssClass="text-error" element="span"/>
                <form:input class="full-width" type="email" path="email" placeholder="Введите"/>
            </label>
            <label for="username">
                Имя пользователя:
                <form:errors path="username" cssClass="text-error" element="span"/>
                <form:input class="full-width" path="username" placeholder="Введите"/>
            </label>
            <label for="password">
                Пароль:
                <form:errors path="password" cssClass="text-error" element="span"/>
                <form:password class="full-width" path="password" placeholder="Введите"/>
            </label>
            <label for="passwordRepeat">
                Повторите пароль:
                <form:errors path="passwordRepeat" cssClass="text-error" element="span"/>
                <form:password class="full-width" path="passwordRepeat" placeholder="Введите"/>
            </label>
            <button class="filled-button" type="submit">Зарегистрироваться</button>
        </form:form>
    </t:popup>
</t:layout>
