package ordersystem.DAL;

import ordersystem.domain.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {


    @Query("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.orders")
    Iterable<Customer> findAllWithOrders();
}
