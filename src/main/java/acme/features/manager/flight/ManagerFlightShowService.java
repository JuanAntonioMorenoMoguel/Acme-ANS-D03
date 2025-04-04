
package acme.features.manager.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.realms.Manager;

@GuiService
public class ManagerFlightShowService extends AbstractGuiService<Manager, Flight> {

	// Internal State -------------------------------------------------------

	@Autowired
	private ManagerFlightRepository repository;

	// AbstractGuiService interface -----------------------------------------


	@Override
	public void authorise() {
		Flight object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findFlightById(id);

		Manager manager = object == null ? null : object.getManager();
		boolean allowed = object != null && super.getRequest().getPrincipal().hasRealm(manager);

		super.getResponse().setAuthorised(allowed);
	}

	@Override
	public void load() {
		Flight flight;
		int id;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(id);

		super.getBuffer().addData(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		assert flight != null;

		Dataset dataset;
		dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "draftMode");
		dataset.put("scheduledDeparture", flight.getScheduledDeparture());
		dataset.put("scheduledArrival", flight.getScheduledArrival());
		dataset.put("originCity", flight.getOriginCity());
		dataset.put("destinationCity", flight.getDestinationCity());
		dataset.put("layovers", flight.getNumberOfLayovers());

		if (flight.getScheduledDeparture() == null)
			dataset.put("scheduledDeparture", "NA");
		if (flight.getScheduledArrival() == null)
			dataset.put("scheduledArrival", "NA");

		super.getResponse().addData(dataset);
	}
}
