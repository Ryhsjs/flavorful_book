<%@ tag description="popup for profile editing" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ attribute name="avatarUrl" required="true" %>
<div class="fr blockout none" id="blockout">
    <div class="fc popup" id="popup">
        <section class="fc user-edit">
            <figure class="user-figure">
                <t:img-user size="fa-6x" url="${avatarUrl}"/>
            </figure>
            <input class="full-width" type="file" accept="image/*" name="avatarImage" id="image" onchange="uploadImage()">
            <input type="hidden" id="type" value="avatar">
        </section>

        <form:form class="fc" modelAttribute="form" action="${pageContext.request.contextPath}/profile" method="post">
            <form:hidden path="avatarUrl" id="imageUrl"/>
            <label for="username">
                Имя пользователя:
                <form:errors path="username" cssClass="text-error" element="span"/>
                <form:input class="full-width" path="username"/>
            </label>
            <button class="filled-button" type="submit">Сохранить</button>
        </form:form>
    </div>
</div>
