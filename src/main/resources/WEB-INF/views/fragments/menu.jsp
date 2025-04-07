<%--
- menu.jsp
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
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:menu-bar>
	<acme:menu-left>
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="master.menu.anonymous.favourite-link" action="http://www.example.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.group-planning" action="https://github.com/users/JuanAntonioMorenoMoguel/projects/3"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.juamormog" action="https://loldle.net/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.enrgaraba" action="https://www.amazon.es/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.felpennun" action="https://github.com/felpennun"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.dandelpin" action="https://es.aliexpress.com/"/>			
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.anglopoli" action="https://www.zara.com/es/"/>			
		</acme:menu-option>

		<acme:menu-option code="master.menu.administrator" access="hasRealm('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.list-user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-initial" action="/administrator/system/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-sample" action="/administrator/system/populate-sample"/>			
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shut-system-down" action="/administrator/system/shut-down"/>
      <acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.list-airports" action="/administrator/airport/list"/>

			<acme:menu-suboption code="master.menu.administrator.aircraft.aircraft-list" action="/administrator/aircraft/list"/>
			<acme:menu-suboption code="master.menu.administrator.maintenance-record-list" action="/administrator/maintenance-record/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-airlines" action="/administrator/airline/list"/>

			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.list-claims" action="/administrator/claim/list"/>

		</acme:menu-option>
		


		<acme:menu-option code="master.menu.provider" access="hasRealm('Provider')">
			<acme:menu-suboption code="master.menu.provider.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.consumer" access="hasRealm('Consumer')">
			<acme:menu-suboption code="master.menu.consumer.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>
		<acme:menu-option code="master.menu.manager" access="hasRealm('Manager')">
			<acme:menu-suboption code="master.menu.manager.list-flights" action="/manager/flight/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.manager.dashboard" action="/manager/manager-dashboard/show"/>
		</acme:menu-option>
		<acme:menu-option code="master.menu.technician" access="hasRealm('Technician')">
			<acme:menu-suboption code="master.menu.technician.maintenanceRecords.maintenanceRecords-list" action="/technician/maintenance-record/list"/>
			<acme:menu-suboption code="master.menu.technician.tasks-list" action="/technician/task/list?mine=true" />	

      <acme:menu-suboption code="master.menu.technician.dashboard" action="/technician/technician-dashboard/show" />

		</acme:menu-option>
		<acme:menu-option code="master.menu.assistance-agent.claim" access="hasRealm('AssistanceAgent')">
			<acme:menu-suboption code="master.menu.assistance-agent.claim.list-mine-completed" action="/assistance-agent/claim/list-mine"/>
			<acme:menu-suboption code="master.menu.assistance-agent.claim.list-mine-undergoing" action="/assistance-agent/claim/list-undergoing"/>
		</acme:menu-option>	
		<acme:menu-option code="master.menu.assistance-agent.assistance-agent-dashboard" access="hasRealm('AssistanceAgent')">
			<acme:menu-suboption code="master.menu.assistance-agent.assistance-agent-dashboard.list" action="/assistance-agent/assistance-agent-dashboard/list"/>

		</acme:menu-option>	

		<acme:menu-option code="master.menu.flightcrewmember" access="hasRealm('FlightCrewMember')">
			<acme:menu-suboption code="master.menu.flightcrewmember.flightassignments.flightAssignments-list-completed" action="/flight-crew-member/flight-assignment/list-completed"/>
			<acme:menu-suboption code="master.menu.flightcrewmember.flightassignments.flightAssignments-list-planned" action="/flight-crew-member/flight-assignment/list-planned"/>
			<acme:menu-suboption code="master.menu.flightcrewmember.activitylogs.activitylogs-list" action="/flight-crew-member/activity-log/list"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.customer" access="hasRealm('Customer')">
			<acme:menu-suboption code="master.menu.customer.dashboard" action="/customer/customer-dashboard/show"/>
			<acme:menu-suboption code="master.menu.customer.list-bookings" action="/customer/booking/list"/>
			<acme:menu-suboption code="master.menu.customer.list-passengers" action="/customer/passenger/list"/>
			<acme:menu-suboption code="master.menu.customer.list-booking-records" action="/customer/booking-record/list"/>
		</acme:menu-option>		

    		


	</acme:menu-left>

	<acme:menu-right>		
		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-profile" action="/authenticated/user-account/update"/>
			<acme:menu-suboption code="master.menu.user-account.become-provider" action="/authenticated/provider/create" access="!hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.provider-profile" action="/authenticated/provider/update" access="hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.become-consumer" action="/authenticated/consumer/create" access="!hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.consumer-profile" action="/authenticated/consumer/update" access="hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.become-technician" action="/authenticated/technician/create" access="!hasRealm('Technician')"/>

			<acme:menu-suboption code="master.menu.user-account.manager-profile" action="/authenticated/manager/update" access="hasRealm('Manager')"/>
			<acme:menu-suboption code="master.menu.user-account.become-manager" action="/authenticated/manager/create" access="!hasRealm('Manager')"/>

			<acme:menu-suboption code="master.menu.user-account.technician-profile" action="/authenticated/technician/update" access="hasRealm('Technician')"/>

			<acme:menu-suboption code="master.menu.user-account.become-assistance-agent" action="/authenticated/assistance-agent/create" access="!hasRealm('AssistanceAgent')"/>
			<acme:menu-suboption code="master.menu.user-account.assistance-agent-profile" action="/authenticated/assistance-agent/update" access="hasRealm('AssistanceAgent')"/>
			

		</acme:menu-option>
	</acme:menu-right>
</acme:menu-bar>

