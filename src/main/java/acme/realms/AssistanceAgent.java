
package acme.realms;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidEmployeeCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AssistanceAgent extends AbstractRole {

	// Serialisation version ------------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -----------------------------------------------------------------
	@Mandatory
	@ValidEmployeeCode
	@Automapped
	private String				employeeCode;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				spokenLanguages;

	@Mandatory
	@ValidString(max = 100)
	@Automapped
	private String				airline;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				employmentStartDate;  // Fecha de inicio en la aerolínea (en el pasado)

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				bio;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				salary;

	@Mandatory
	@ValidUrl
	@Automapped
	private String				photoUrl;

	// Derived attributes --------------------------------------------------------
	// Aquí puedes agregar atributos derivados si es necesario

	// Relationships -------------------------------------------------------------
	// Relaciones con otras entidades, si las hubiera, se definirían aquí
}
