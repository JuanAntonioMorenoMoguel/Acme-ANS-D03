
package acme.entities.leg;

import java.beans.Transient;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.MomentHelper;
import acme.constraints.ValidLeg;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.realms.Manager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@ValidLeg
@Setter
public class Leg extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z]{3}\\d{4}$")
	@Column(unique = true)
	private String				flightNumber;

	@Mandatory
	@ValidMoment(past = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment(past = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Mandatory
	@Valid
	@Automapped
	private LegStatus			status;

	@Mandatory
	@Valid
	@ManyToOne
	private Airport				departureAirport;

	@Mandatory
	@Valid
	@ManyToOne
	private Airport				arrivalAirport;

	@Mandatory
	@Valid
	@ManyToOne
	private Aircraft			aircraft;

	@Mandatory
	@Valid
	@ManyToOne
	private Manager				manager;

	@Mandatory
	@Valid
	@ManyToOne
	private Flight				flight;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				draftMode;


	@Transient
	public double durationHours() {
		if (this.scheduledArrival == null && this.scheduledDeparture == null)
			return 0.;
		double duration = MomentHelper.computeDuration(this.scheduledDeparture, this.scheduledArrival).toMinutes() / 60.0;

		return duration;
	};
}
