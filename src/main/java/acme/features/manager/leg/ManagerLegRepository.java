
package acme.features.manager.leg;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;

@Repository
public interface ManagerLegRepository extends AbstractRepository {

	@Query("select l from Leg l where l.id= :id")
	Leg findLegById(int id);

	@Query("select f from Flight f where f.id = :id")
	Flight findFlightById(int id);

	@Query("select l from Leg l where l.manager.userAccount.id = :id")
	List<Leg> findLegsByManager(int id);

	@Query("select l from Leg l where l.flight.id = :id")
	List<Leg> findLegsByFlight(int id);

	@Query("select MIN(l.scheduledDeparture) from Leg l where l.flight.id = :flightId")
	Date findDepartureByFlightId(int flightId);

	@Query("select MAX(l.scheduledArrival) from Leg l where l.flight.id = :flightId")
	Date findArrivalByFlightId(int flightId);

	@Query("select l.departureAirport.city from Leg l where l.flight.id = :flightId order by l.scheduledDeparture ASC")
	List<String> findOriginCityByFlightId(int flightId);

	@Query("select l.arrivalAirport.city from Leg l where l.flight.id = :flightId order by l.scheduledDeparture DESC")
	List<String> findDestinationCityByFlightId(int flightId);

	@Query("select count(l) from Leg l where l.flight.id = :flightId")
	int numberOfLayovers(int flightId);

	@Query("SELECT l FROM Leg l WHERE l.aircraft.id = :aircraftId AND l.id != :legId")
	List<Leg> findByAircraftId(int aircraftId, int legId);

	@Query("SELECT l FROM Leg l WHERE l.flightNumber = :flightNumber AND l.id != :legId")
	Optional<Leg> findByFlightNumber(String flightNumber, int legId);

	@Query("select a from Airport a")
	List<Airport> findAllAirports();

	@Query("select a from Aircraft a")
	List<Aircraft> findAllAircraft();

	@Query("select f from Flight f")
	List<Flight> findAllFlights();

	@Query("select f from Flight f where f.manager.userAccount.id =:id")
	Collection<Flight> findFlightsByManagerId(int id);
}
