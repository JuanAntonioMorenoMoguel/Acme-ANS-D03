
package acme.features.manager.leg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.realms.Manager;

@GuiService
public class ManagerLegCreateService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(super.getRequest().getPrincipal().getActiveRealm().getClass().equals(Manager.class));
	}

	@Override
	public void load() {
		int flightId = super.getRequest().getData("flightId", int.class);
		Flight flight = this.repository.findFlightById(flightId);
		Manager manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();

		Leg leg = new Leg();
		leg.setFlight(flight);
		leg.setDraftMode(true);
		leg.setManager(manager);
		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		assert leg != null;

		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "departureAirport", "arrivalAirport", "aircraft");
	}

	@Override
	public void validate(final Leg leg) {
		assert leg != null;
		if (!this.getBuffer().getErrors().hasErrors("arrivalAirport") && leg.getArrivalAirport() != null)
			super.state(leg.getArrivalAirport() != leg.getDepartureAirport(), "arrivalAirport", "manager.flight.form.error.same-Airports", leg);

	}

	@Override
	public void perform(final Leg leg) {
		assert leg != null;
		int flightId = super.getRequest().getData("flightId", int.class);

		// Si hay ya un aircraft asignado a otro leg del mismo vuelo se queda ese aircraft
		List<Leg> legsFromFlight = this.repository.findLegsByFlight(flightId);
		if (legsFromFlight.size() > 0)
			leg.setAircraft(legsFromFlight.get(0).getAircraft());

		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		assert leg != null;

		Dataset dataset;
		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "departureAirport", "arrivalAirport", "flight", "draftMode");

		SelectChoices choices = SelectChoices.from(LegStatus.class, leg.getStatus());
		SelectChoices aircraftChoices = SelectChoices.from(this.repository.findAllAircraft(), "registrationNumber", leg.getAircraft());
		SelectChoices airportChoices = SelectChoices.from(this.repository.findAllAirports(), "iataCode", leg.getDepartureAirport());

		dataset.put("choices", choices);
		dataset.put("legStatus", leg.getStatus());
		dataset.put("aircraftChoices", aircraftChoices);
		dataset.put("flightId", leg.getFlight().getId());
		dataset.put("airportChoices", airportChoices);

		super.getResponse().addData(dataset);
	}
}
