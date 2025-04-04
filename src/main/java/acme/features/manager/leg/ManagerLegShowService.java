
package acme.features.manager.leg;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.realms.Manager;

@GuiService
public class ManagerLegShowService extends AbstractGuiService<Manager, Leg> {

	// Internal State -------------------------------------------------------

	@Autowired
	private ManagerLegRepository repository;

	// AbstractGuiService interface -----------------------------------------


	@Override
	public void authorise() {
		boolean exist;
		boolean ownsFlight;
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(id);

		exist = leg != null;
		ownsFlight = leg.getManager().getUserAccount().getId() == super.getRequest().getPrincipal().getAccountId();
		if (!(exist && ownsFlight))
			exist = false;
		super.getResponse().setAuthorised(exist);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		assert leg != null;

		Dataset dataset;
		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "departureAirport", "arrivalAirport", "aircraft", "flight", "draftMode");
		dataset.put("duration", leg.durationHours());

		SelectChoices choices = SelectChoices.from(LegStatus.class, leg.getStatus());
		SelectChoices aircraftChoices = SelectChoices.from(this.repository.findAllAircraft(), "registrationNumber", leg.getAircraft());
		SelectChoices airportChoices = SelectChoices.from(this.repository.findAllAirports(), "iataCode", leg.getDepartureAirport());

		dataset.put("legStatus", leg.getStatus());
		dataset.put("choices", choices);
		dataset.put("legStatus", leg.getStatus());
		dataset.put("aircraftChoices", aircraftChoices);
		dataset.put("airportChoices", airportChoices);

		super.getResponse().addData(dataset);
	}
}
