
package acme.features.administrator.maintenanceRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.maintenanceRecord.MaintenanceRecord;

@GuiController
public class AdministratorMaintenanceRecordController extends AbstractGuiController<Administrator, MaintenanceRecord> {

	//Internal state --------------------------------------------------------------

	@Autowired
	private AdministratorMaintenanceRecordListService listService;

	//Constructors ----------------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
	}

}
