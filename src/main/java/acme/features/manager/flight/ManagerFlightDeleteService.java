
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
public class ManagerFlightDeleteService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private ManagerFlightRepository repository;


	@Override
	public void authorise() {
		int flightId = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findFlightById(flightId);
		Manager manager = flight == null ? null : flight.getManager();
		boolean allowed = flight != null && flight.getDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);
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
	public void bind(final Flight object) {
		assert object != null;
	}

	@Override
	public void validate(final Flight object) {
		assert object != null;

		super.state(object.getDraftMode(), "draftMode", "manager.flight.form.error.draftMode", object);
	}

	@Override
	public void perform(final Flight object) {
		assert object != null;

		Collection<Leg> legs = this.repository.findLegsByFlightId(object.getId());
		this.repository.deleteAll(legs);

		this.repository.delete(object);
	}

	@Override
	public void unbind(final Flight flight) {
		assert flight != null;

		Dataset dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "draftMode");
		super.getResponse().addData(dataset);
	}
}
