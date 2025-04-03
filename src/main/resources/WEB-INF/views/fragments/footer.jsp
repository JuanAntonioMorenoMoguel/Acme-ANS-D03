<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:footer-panel>
	<acme:footer-subpanel code="master.footer.title.about">
		<acme:footer-option icon="fa fa-building" code="master.footer.label.company" action="/any/system/company"/>
		<acme:footer-option icon="fa fa-file" code="master.footer.label.license" action="/any/system/license"/>
	</acme:footer-subpanel>

	<acme:footer-subpanel code="master.footer.title.social">
		<acme:print var="$linkedin$url" code="master.footer.url.linkedin"/>
		<acme:footer-option icon="fab fa-linkedin" code="master.footer.label.linked-in" action="${$linkedin$url}" newTab="true"/>
		<acme:print var="$twitter$url" code="master.footer.url.twitter"/>
		<acme:footer-option icon="fab fa-twitter" code="master.footer.label.twitter" action="${$twitter$url}" newTab="true"/>
	</acme:footer-subpanel>

	<acme:footer-subpanel code="master.footer.title.languages">
		<acme:footer-option icon="fa fa-language" code="master.footer.label.english" action="/?locale=en"/>
		<acme:footer-option icon="fa fa-language" code="master.footer.label.spanish" action="/?locale=es"/>
	</acme:footer-subpanel>

	<acme:menu-option code="master.footer.title.service" access="isAuthenticated()">
     	<acme:footer-logo logo="images/service.png" alt="${picture}">
    	</acme:footer-logo>
	</acme:menu-option>
  

	<acme:footer-logo logo="images/logo.png" alt="master.company.name">
		<acme:footer-copyright code="master.company.name"/>
	</acme:footer-logo>
</acme:footer-panel>
