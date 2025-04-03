<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<acme:form> 
	<acme:input-moment code="administrator.claim.form.label.registrationMoment" path="registrationMoment" readonly="true"/>
	<acme:input-email code="administrator.claim.form.label.passengerEmail" path="passengerEmail"/>
	<acme:input-textarea code="administrator.claim.form.label.description" path="description"/>
	<acme:input-select code="administrator.claim.form.label.claimType" path="claimType" choices="${claimTypes}"/>
	<acme:input-select code="administrator.claim.form.label.indicator" path="indicator" choices="${indicators}"/>
	<acme:input-select code="administrator.claim.form.label.legs" path="legs" choices="${legs}"/>

	
	<jstl:choose>	
		<jstl:when test="${acme:anyOf(_command, 'show')}">
			<acme:button code="administrator.claim.form.button.tracking-logs" action="/administrator/tracking-log/list?claimId=${id}"/>	
		</jstl:when>
	</jstl:choose>

</acme:form>