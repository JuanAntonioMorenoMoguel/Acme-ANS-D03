
package acme.features.technician.maintenanceRecord;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.maintenanceRecord.MaintenanceStatus;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordCreateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {
		boolean authorised = false;
		Principal principal = super.getRequest().getPrincipal();
		int userAccountId = principal.getAccountId();

		// Buscar al técnico que está actualmente logueado
		Technician technician = this.repository.findOneTechnicianByUserAccoundId(userAccountId);

		if (technician != null)
			// Verificar si el técnico está registrado en el sistema
			authorised = true;

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		Date moment = MomentHelper.getCurrentMoment();
		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		maintenanceRecord = new MaintenanceRecord();
		maintenanceRecord.setTechnician(technician);
		maintenanceRecord.setMaintenanceMoment(moment);
		maintenanceRecord.setDraftMode(true);
		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void bind(final MaintenanceRecord maintenanceRecord) {
		super.bindObject(maintenanceRecord, "status", "nextInspectionDue", "estimatedCost", "notes", "aircraft");
	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {

		if (!this.getBuffer().getErrors().hasErrors("status"))
			super.state(maintenanceRecord.getStatus() != null, "status", "technician.maintenance-record.form.error.noStatus", maintenanceRecord);

		if (!this.getBuffer().getErrors().hasErrors("nextInspectionDue") && maintenanceRecord.getNextInspectionDue() != null)
			super.state(maintenanceRecord.getNextInspectionDue().compareTo(maintenanceRecord.getMaintenanceMoment()) > 0, "nextInspectionDue", "technician.maintenance-record.form.error.nextInspectionDue", maintenanceRecord);

		if (!this.getBuffer().getErrors().hasErrors("estimatedCost") && maintenanceRecord.getEstimatedCost() != null)
			super.state(0.00 <= maintenanceRecord.getEstimatedCost().getAmount() && maintenanceRecord.getEstimatedCost().getAmount() <= 1000000.00, "estimatedCost", "technician.maintenance-record.form.error.estimatedCost", maintenanceRecord);

		if (!this.getBuffer().getErrors().hasErrors("notes") && maintenanceRecord.getNotes() != null)
			super.state(maintenanceRecord.getNotes().length() <= 255, "notes", "technician.maintenance-record.form.error.notes", maintenanceRecord);

		if (!this.getBuffer().getErrors().hasErrors("aircraft") && maintenanceRecord.getAircraft() != null)
			super.state(this.repository.findAllAircrafts().contains(maintenanceRecord.getAircraft()), "aircraft", "technician.maintenance-record.form.error.aircraft", maintenanceRecord);
	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		assert maintenanceRecord != null;

		this.repository.save(maintenanceRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		SelectChoices choices;
		Collection<Aircraft> aircrafts;
		SelectChoices aircraft;

		Dataset dataset;
		aircrafts = this.repository.findAllAircrafts();
		choices = SelectChoices.from(MaintenanceStatus.class, maintenanceRecord.getStatus());
		aircraft = SelectChoices.from(aircrafts, "id", maintenanceRecord.getAircraft());

		dataset = super.unbindObject(maintenanceRecord, "status", "nextInspectionDue", "estimatedCost", "notes", "aircraft", "draftMode");

		dataset.put("status", choices.getSelected().getKey());
		dataset.put("status", choices);
		dataset.put("aircraft", aircraft.getSelected().getKey());
		dataset.put("aircraft", aircraft);

		super.getResponse().addData(dataset);
	}

}
