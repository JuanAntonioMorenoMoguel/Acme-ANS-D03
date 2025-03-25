
package acme.features.authenticated.manager.leg;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.realms.Manager;

@GuiService
public class ManagerLegPublishService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository repository;


	@Override
	public void authorise() {
		Leg object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findLegById(id);

		Manager manager = object == null ? null : object.getManager();
		boolean allowed = object != null && object.getDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);

		super.getResponse().setAuthorised(allowed);
	}

	@Override
	public void load() {
		Leg object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findLegById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Leg object) {
		assert object != null;
	}

	@Override
	public void validate(final Leg object) {
		assert object != null;
	}

	@Override
	public void perform(final Leg object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Leg object) {
		assert object != null;

		Dataset dataset;
		SelectChoices choices;
		choices = SelectChoices.from(LegStatus.class, object.getStatus());
		dataset = super.unbindObject(object, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "departureAirport", "arrivalAirport", "aircraft", "flight", "draftMode");
		dataset.put("choices", choices);

		super.getResponse().addData(dataset);
	}
}
