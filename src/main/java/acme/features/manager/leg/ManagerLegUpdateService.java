
package acme.features.manager.leg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.realms.Manager;

@GuiService
public class ManagerLegUpdateService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository repository;


	@Override
	public void authorise() {
		Leg object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findLegById(id);

		Manager manager = object == null ? null : object.getManager();
		boolean allowed = object != null && super.getRequest().getPrincipal().hasRealm(manager);

		super.getResponse().setAuthorised(allowed);
	}

	@Override
	public void load() {
		Leg leg;

		leg = this.repository.findLegById(super.getRequest().getData("id", int.class));

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		assert leg != null;

		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "departureAirport", "arrivalAirport", "aircraft");
		leg.setDraftMode(true);
	}

	@Override
	public void validate(final Leg leg) {
		assert leg != null;
	}

	@Override
	public void perform(final Leg leg) {
		assert leg != null;
		int id = leg.getFlight().getId();

		// Si hay ya un aircraft asignado a otro leg del mismo vuelo se queda ese aircraft
		List<Leg> legsFromFlight = this.repository.findLegsByFlight(id);
		if (legsFromFlight.size() > 0)
			leg.setAircraft(legsFromFlight.get(0).getAircraft());

		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		assert leg != null;

		Dataset dataset;
		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "departureAirport", "arrivalAirport", "draftMode");

		SelectChoices choices = SelectChoices.from(LegStatus.class, leg.getStatus());
		SelectChoices aircraftChoices = SelectChoices.from(this.repository.findAllAircraft(), "registrationNumber", leg.getAircraft());
		SelectChoices airportChoices = SelectChoices.from(this.repository.findAllAirports(), "iataCode", leg.getDepartureAirport());

		dataset.put("choices", choices);
		dataset.put("legStatus", leg.getStatus());
		dataset.put("aircraftChoices", aircraftChoices);
		dataset.put("airportChoices", airportChoices);

		super.getResponse().addData(dataset);
	}
}
