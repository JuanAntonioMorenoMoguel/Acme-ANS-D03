<%--
- form.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<acme:form>
	
	<acme:input-select code = "administrator.maintenance-record.form.label.status" path = "status" choices="${status}"/>
	<acme:input-moment code = "administrator.maintenance-record.form.label.nextInspectionDue" path = "nextInspectionDue"/>
	<acme:input-money code = "administrator.maintenance-record.form.label.estimatedCost" path = "estimatedCost"/>
	<acme:input-textbox code = "administrator.maintenance-record.form.label.notes" path = "notes"/>
	<acme:input-select code = "administrator.maintenance-record.form.label.aircraft" path = "aircraft" choices="${aircraft}"/>
	
	<jstl:choose>
	<jstl:when test="${_command == 'show'}">
			<acme:button code="administrator.maintenance-record.form.button.tasks" action="/administrator/task/list?maintenanceRecordId=${id}"/>			
	</jstl:when>
	</jstl:choose>
</acme:form>
