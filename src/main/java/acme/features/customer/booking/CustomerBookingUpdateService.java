
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingUpdateService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository customerBookingRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		Integer bookingId = super.getRequest().getData("id", int.class);
		Booking booking = this.customerBookingRepository.findBookingById(bookingId);

		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		status = status && booking.getCustomer().getId() == customerId && !booking.getPublished();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Integer id = super.getRequest().getData("id", int.class);
		Booking booking = this.customerBookingRepository.findBookingById(id);
		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "flight", "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble");
	}

	@Override
	public void validate(final Booking booking) {

	}

	@Override
	public void perform(final Booking booking) {
		this.customerBookingRepository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		Collection<Flight> flights = this.customerBookingRepository.findAllPublishedFlights();
		SelectChoices flightChoices = SelectChoices.from(flights, "flightSummary", booking.getFlight());

		Dataset dataset = super.unbindObject(booking, "flight", "customer", "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble", "published", "id");
		dataset.put("travelClass", travelClasses);
		dataset.put("flights", flightChoices);

		super.getResponse().addData(dataset);
	}

}
