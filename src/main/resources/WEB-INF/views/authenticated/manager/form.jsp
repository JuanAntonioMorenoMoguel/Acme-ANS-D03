<%@page %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-textbox code="authenticated.manager.form.label.identifier" path="idNumber"/>
    <acme:input-textbox code="authenticated.manager.form.label.experience" path="xpYears"/>
    <acme:input-moment code="authenticated.manager.form.label.birthDate" path="dateOfBirth"/>
    <acme:input-url code="authenticated.manager.form.label.pictureLink" path="linkToPicture"/>

    <jstl:choose>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="authenticated.manager.form.button.create" action="/authenticated/manager/create"/>
        </jstl:when>
        <jstl:when test="${_command == 'update'}">
            <acme:submit code="authenticated.manager.form.button.update" action="/authenticated/manager/update"/>
        </jstl:when>
    </jstl:choose>
</acme:form>