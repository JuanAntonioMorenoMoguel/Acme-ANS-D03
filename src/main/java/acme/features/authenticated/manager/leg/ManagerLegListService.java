
package acme.features.authenticated.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.leg.Leg;
import acme.features.authenticated.manager.flight.ManagerFlightRepository;
import acme.realms.Manager;

@GuiService
public class ManagerLegListService extends AbstractGuiService<Manager, Leg> {

	// Internal State -------------------------------------------------------

	@Autowired
	private ManagerLegRepository	repository;

	@Autowired
	private ManagerFlightRepository	flightRepository;

	// AbstractGuiService interface -----------------------------------------


	@Override
	public void authorise() {
		int masterId;
		int principalId;
		int id;

		masterId = super.getRequest().getData("masterId", int.class);
		principalId = this.flightRepository.findFlightById(masterId).getManager().getUserAccount().getId();
		id = super.getRequest().getPrincipal().getAccountId();

		super.getResponse().setAuthorised(id == principalId);
	}

	@Override
	public void load() {
		Collection<Leg> legs;

		int masterId = super.getRequest().getData("masterId", int.class);

		legs = this.repository.findLegsByFlight(masterId);

		super.getBuffer().addData(legs);
	}

	@Override
	public void unbind(final Leg leg) {
		assert leg != null;

		int masterId = super.getRequest().getData("masterId", int.class);

		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "status", "scheduledDeparture", "draftMode");
		dataset.put("masterId", masterId);
		super.getResponse().addData(dataset);
	}
}
