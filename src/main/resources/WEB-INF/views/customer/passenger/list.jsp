<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.passenger.list.label.fullName" path="fullName" width="20%"/>
	<acme:list-column code="customer.passenger.list.label.passportNumber" path="passportNumber" width="20%"/>
	<acme:list-column code="customer.passenger.list.label.birthDate" path="dateOfBirth" width="20%"/>
	<acme:list-column code="customer.passenger.list.label.published" path="published" width="20%"/>
</acme:list>	
	
<jstl:if test="${acme:anyOf(_command, 'list') && !containsBookingId}">
	<acme:button code="customer.passenger.list.button.create" action="/customer/passenger/create"/>
</jstl:if>	