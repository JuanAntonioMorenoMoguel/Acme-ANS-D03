
package acme.features.manager.dashboard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.configuration.SystemConfiguration;
import acme.realms.Manager;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {

	@Query("select m from Manager m where m.id = :id")
	Manager findManagerById(int id);

	@Query("select count(m) from Manager m where m.xpYears > (select m2.xpYears from Manager m2 where m2.id = :id)")
	int findRankingByManagerId(int id);

	@Query("select 65 - m.xpYears from Manager m where m.id = :id")
	int findYearsToRetire(int id);

	@Query("select count(l) from Leg l where l.manager.id = :id and l.status = acme.entities.leg.LegStatus.ON_TIME")
	int countOnTimeLegs(int id);

	@Query("select count(l) from Leg l where l.manager.id = :id and l.status = acme.entities.leg.LegStatus.DELAYED")
	int countDelayedLegs(int id);

	@Query("select avg(f.cost.amount), min(f.cost.amount), max(f.cost.amount), stddev(f.cost.amount) from Flight f where f.manager.id = :id")
	List<Object[]> statsCost(int id);

	@Query("select sc from SystemConfiguration sc")
	SystemConfiguration getSystemConfiguration();
}
