
package acme.features.technician.maintenanceRecordTask;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Task;

@Repository
public interface TechnicianMaintenanceRecordTaskRepository extends AbstractRepository {

	@Query("SELECT t FROM Task t WHERE t.technician.id = :technicianId ")
	Collection<Task> findAllTaskByTechnicianId(final int technicianId);

	@Query("SELECT t FROM Task t WHERE t.id = :id")
	Task findTaskById(int id);

	@Query("SELECT m FROM MaintenanceRecord m WHERE m.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("SELECT mrt.task FROM MaintenanceRecordTask mrt WHERE mrt.maintenanceRecord.id = :maintenanceRecordId")
	Collection<Task> findTaskOfMaintenanceRecord(int maintenanceRecordId);

	@Query("select t from Task t where t.draftMode = false")
	Collection<Task> findAllPublishedTasks();

}
