
package acme.features.manager.leg;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.leg.Leg;
import acme.realms.Manager;

@GuiService
public class ManagerLegDeleteService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository repository;


	@Override
	public void authorise() {
		int legId = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(legId);
		Manager manager = leg == null ? null : leg.getManager();
		boolean allowed = leg != null && leg.getDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);
		super.getResponse().setAuthorised(allowed);
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
	public void bind(final Leg leg) {
		assert leg != null;
		super.bindObject(leg, "id");
	}

	@Override
	public void validate(final Leg object) {
		assert object != null;

		super.state(object.getDraftMode(), "*", "manager.leg.form.error.draftMode", object);
	}

	@Override
	public void perform(final Leg object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final Leg leg) {
		assert leg != null;

		Dataset dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "departureAirport", "arrivalAirport", "aircraft", "flight", "draftMode");
		super.getResponse().addData(dataset);
	}
}
