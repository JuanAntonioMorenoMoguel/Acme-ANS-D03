
package acme.entities.flight;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidFlight;
import acme.entities.leg.LegRepository;
import acme.realms.Manager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidFlight
public class Flight extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				requiresSelfTransfer;

	@Mandatory
	@ValidMoney(min = 0)
	@Automapped
	private Money				cost;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				draftMode;

	@Mandatory
	@Valid
	@ManyToOne(optional = true)
	private Manager				manager;

	// Derived attributes -----------------------------------------------------


	@Transient()
	public Date getScheduledDeparture() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		return repository.findFirstScheduledDeparture(this.getId()).orElse(null);
	}

	@Transient()
	public Date getScheduledArrival() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		return repository.findLastScheduledArrival(this.getId()).orElse(null);
	}

	@Transient()
	public String getOriginCity() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		return repository.findFirstOriginCity(this.getId()).orElse("Desconocido");
	}

	@Transient()
	public String getDestinationCity() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		return repository.findLastDestinationCity(this.getId()).orElse("Desconocido");
	}

	@Transient()
	public Integer getNumberOfLayovers() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		Integer layovers = repository.numberOfLavoyers(this.getId());
		return layovers == null || layovers == 0 ? 0 : layovers - 1;
	}

	@Transient()
	public String getLabel() {
		String origin = this.getOriginCity();
		String destination = this.getDestinationCity();
		return origin + "-" + destination;
	}
}
