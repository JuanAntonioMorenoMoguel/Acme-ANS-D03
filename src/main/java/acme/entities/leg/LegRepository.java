
package acme.entities.leg;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface LegRepository extends AbstractRepository {

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledDeparture ASC")
	List<Leg> findByFlightIdOrdered(int flightId);

	@Query("SELECT l FROM Leg l WHERE l.aircraft.id = :aircraftId AND l.id != :legId")
	List<Leg> findByAircraftId(int aircraftId, int legId);

	@Query("SELECT l FROM Leg l WHERE l.flightNumber = :flightNumber AND l.id != :legId")
	Optional<Leg> findByFlightNumber(String flightNumber, int legId);

	@Query("SELECT COUNT(l) from Leg l WHERE l.flight.id = :flightId")
	Integer numberOfLavoyers(int flightId);

	@Query(value = "SELECT l.scheduledDeparture FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledDeparture ASC LIMIT 1", nativeQuery = true)
	Optional<Date> findFirstScheduledDeparture(int flightId);

	@Query(value = "SELECT l.scheduleArrival FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledArrival DESC LIMIT 1", nativeQuery = true)
	Optional<Date> findLastScheduledArrival(int flightId);

	@Query(value = "SELECT a.city FROM Leg l JOIN Airport a ON l.departureAirport.id = a.id WHERE l.flight.Id = :flightId ORDER BY l.scheduledDeparture ASC LIMIT 1", nativeQuery = true)
	Optional<String> findFirstOriginCity(int flightId);

	@Query(value = "SELECT a.city FROM Leg l JOIN Airport a ON l.arrivalAirport.id = a.id WHERE l.flight.Id = :flightId ORDER BY l.scheduledArrival DESC LIMIT 1", nativeQuery = true)
	Optional<String> findLastDestinationCity(int flightId);
}
