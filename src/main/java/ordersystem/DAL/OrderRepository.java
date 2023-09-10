package ordersystem.DAL;

import ordersystem.domain.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findAllByOrderByDate();
    List<Order> findAllByDate(Date date);

    @Query("SELECT o FROM Order o JOIN o.orderLines ol WHERE ol.product.id = :productId")
    List<Order> findAllByProductId(@Param("productId") Long productId);

    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId")
    List<Order> findAllByCustomerId(@Param("customerId") Long customerId);
}
