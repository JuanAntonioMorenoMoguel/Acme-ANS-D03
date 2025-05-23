
package acme.features.authenticated.customer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.realms.Customer;

@Repository
public interface AuthenticatedCustomerRepository extends AbstractRepository {

	@Query("SELECT u FROM UserAccount u WHERE u.id = :id")
	UserAccount findUserAccountById(int id);

	@Query("SELECT c FROM Customer c WHERE c.userAccount.id = :id")
	Customer findCustomerByUserAccountId(int id);

	@Query("SELECT c FROM Customer c WHERE c.identifier =:identifier")
	Customer findCustomerByCustomerIdentifier(String identifier);
}
