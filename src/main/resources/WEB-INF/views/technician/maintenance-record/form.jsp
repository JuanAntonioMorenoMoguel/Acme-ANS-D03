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
	
	<acme:input-select code = "technician.maintenance-record.form.label.status" path = "status" choices="${status}"/>
	<acme:input-moment code = "technician.maintenance-record.form.label.nextInspectionDue" path = "nextInspectionDue"/>
	<acme:input-money code = "technician.maintenance-record.form.label.estimatedCost" path = "estimatedCost"/>
	<acme:input-textbox code = "technician.maintenance-record.form.label.notes" path = "notes"/>
	<acme:input-select code = "technician.maintenance-record.form.label.aircraft" path = "aircraft" choices="${aircraft}"/>
	
	<jstl:choose>
	<jstl:when test="${_command == 'show' && draftMode}">
			<acme:button code="technician.maintenance-record.form.button.tasks" action="/technician/task/list?maintenanceRecordId=${id}"/>			
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command,'show|update|delete') && !draftMode }">
			<acme:button code="technician.maintenance-record.form.button.tasks" action="/technician/task/list?maintenanceRecordId=${id}"/>
			<acme:submit code="technician.maintenance-record.form.button.update" action="/technician/maintenance-record/update"/>	
			<%--<acme:submit code="technician.maintenance-record.form.button.delete" action="/technician/maintenance-record/delete"/>%>	--%>
			<acme:submit code="technician.maintenance-record.form.button.publish" action="/technician/maintenance-record/publish?maintenanceRecordId=${id}"/>
		</jstl:when>
		<jstl:when  test="${acme:anyOf(_command,'create')}">
			<acme:submit code="technician.maintenance-record.form.button.create" action="/technician/maintenance-record/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>
