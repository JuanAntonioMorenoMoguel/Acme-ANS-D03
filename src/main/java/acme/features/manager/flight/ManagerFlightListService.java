
package acme.features.manager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.realms.Manager;

@GuiService
public class ManagerFlightListService extends AbstractGuiService<Manager, Flight> {

	// Internal State -------------------------------------------------------

	@Autowired
	private ManagerFlightRepository repository;

	// AbstractGuiService interface -----------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Flight> flights;

		int id = super.getRequest().getPrincipal().getAccountId();

		flights = this.repository.findFlightsByManagerId(id);

		super.getBuffer().addData(flights);
	}

	@Override
	public void unbind(final Flight flight) {
		assert flight != null;

		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "cost", "description", "draftMode");
		super.getResponse().addData(dataset);
	}
}
