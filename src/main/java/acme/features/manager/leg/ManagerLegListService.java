
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.leg.Leg;
import acme.features.manager.flight.ManagerFlightRepository;
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
		int flightId;
		int principalId;
		int id;

		flightId = super.getRequest().getData("flightId", int.class);
		principalId = this.flightRepository.findFlightById(flightId).getManager().getUserAccount().getId();
		id = super.getRequest().getPrincipal().getAccountId();

		super.getResponse().setAuthorised(id == principalId);
	}

	@Override
	public void load() {
		Collection<Leg> legs;
		int flightId;

		flightId = super.getRequest().getData("flightId", int.class);
		legs = this.repository.findLegsByFlight(flightId);

		super.getResponse().addGlobal("flightId", flightId);
		super.getBuffer().addData(legs);
	}

	@Override
	public void unbind(final Leg leg) {
		assert leg != null;

		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "status", "scheduledDeparture", "draftMode");
		dataset.put("flightId", leg.getFlight().getId());
		super.getResponse().addData(dataset);
	}
}
