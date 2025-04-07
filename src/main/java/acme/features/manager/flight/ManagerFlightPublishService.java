
package acme.features.manager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.realms.Manager;

@GuiService
public class ManagerFlightPublishService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private ManagerFlightRepository repository;


	@Override
	public void authorise() {
		Flight object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findFlightById(id);

		Manager manager = object == null ? null : object.getManager();
		boolean allowed = object != null && object.getDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);

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
	public void bind(final Flight flight) {
		assert flight != null;
	}

	@Override
	public void validate(final Flight flight) {
		assert flight != null;

		super.state(!flight.getDraftMode(), "*", "manager.flight.form.error.draftMode", flight);
		super.state(flight.getNumberOfLayovers() > 0, "layovers", "manager.flight.form.error.layovers", flight);

		// Todos los legs deben estar publicados
		Collection<Leg> legs = this.repository.findLegsByFlightId(flight.getId());
		boolean allPublished = legs.stream().allMatch(leg -> !leg.getDraftMode());
		super.state(allPublished, "*", "manager.flight.noLegsPublished");
	}

	@Override
	public void perform(final Flight flight) {
		assert flight != null;

		flight.setDraftMode(false);
		this.repository.save(flight);
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
