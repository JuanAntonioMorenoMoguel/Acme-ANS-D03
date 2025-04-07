<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<jstl:choose>
			<jstl:when  test="${acme:anyOf(_command,'create')}">
			<acme:input-textbox code="manager.form.label.tag" path="tag"/>
			<acme:input-checkbox code="manager.form.label.requiresSelfTransfer" path="requiresSelfTransfer"/>
			<acme:input-textbox code="manager.form.label.cost" path="cost"/>
			<acme:input-textbox code="manager.form.label.description" path="description"/>
			<acme:submit code="manager.flight.form.button.create" action="/manager/flight/create"/>
		</jstl:when>
	
		<jstl:when test="${acme:anyOf(_command,'show|update|publish|delete')}">
			<acme:input-textbox code="manager.form.label.tag" path="tag"/>
			<acme:input-checkbox code="manager.form.label.requiresSelfTransfer" path="requiresSelfTransfer"/>
			<acme:input-textbox code="manager.form.label.cost" path="cost"/>
			<acme:input-textbox code="manager.form.label.description" path="description"/>
			<acme:input-textbox code="manager.form.label.scheduledDeparture" path="scheduledDeparture" readonly="true"/>
			<acme:input-textbox code="manager.form.label.scheduledArrival" path="scheduledArrival" readonly="true"/>
			<acme:input-textbox code="manager.form.label.originCity" path="originCity" readonly="true"/>
			<acme:input-textbox code="manager.form.label.destinationCity" path="destinationCity" readonly="true"/>
			<acme:input-textbox code="manager.form.label.layovers" path="layovers" readonly="true"/>
			<jstl:if test="${draftMode}">
				<acme:submit code="manager.flight.form.button.update" action="/manager/flight/update"/>	
				<acme:submit code="manager.flight.form.button.delete" action="/manager/flight/delete"/>	
			</jstl:if>
		</jstl:when>
	</jstl:choose>
	<jstl:if test="${draftMode && acme:anyOf(_command,'show|update|publish|delete')}">
		<acme:submit code="manager.flight.form.button.publish" action="/manager/flight/publish"/>	
	</jstl:if>
	<acme:button code="manager.flight.form.button.legs" action="/manager/leg/list?flightId=${id}"/>
</acme:form>