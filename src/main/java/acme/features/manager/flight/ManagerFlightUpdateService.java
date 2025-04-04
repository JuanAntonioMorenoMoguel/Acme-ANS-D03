
package acme.features.manager.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.realms.Manager;

@GuiService
public class ManagerFlightUpdateService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private ManagerFlightRepository repository;


	@Override
	public void authorise() {
		Flight object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findFlightById(id);
		Manager manager = object == null ? null : object.getManager();
		boolean status = object != null && object.getDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);

		super.getResponse().setAuthorised(status);
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
		super.bindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");
	}

	@Override
	public void validate(final Flight flight) {
		assert flight != null;

		if (!this.getBuffer().getErrors().hasErrors("tag") && flight.getTag() != null)
			super.state(flight.getTag().length() <= 50, "tag", "manager.flight.form.error.name", flight);
		if (!this.getBuffer().getErrors().hasErrors("requiresSelfTransfer") && flight.getRequiresSelfTransfer() != null)
			super.state(flight.getRequiresSelfTransfer() != null, "requiresSelfTransfer", "manager.flight.form.error.requiresSelfTransfer", flight);
		if (!this.getBuffer().getErrors().hasErrors("cost") && flight.getCost() != null)
			super.state(flight.getCost().getAmount() >= 0, "cost", "manager.flight.form.error.cost", flight);
		if (!this.getBuffer().getErrors().hasErrors("description") && flight.getDescription() != null)
			super.state(flight.getDescription().length() <= 255, "tag", "manager.flight.form.error.description", flight);
	}

	@Override
	public void perform(final Flight flight) {
		assert flight != null;

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
