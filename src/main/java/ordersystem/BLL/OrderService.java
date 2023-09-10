package ordersystem.BLL;

import jakarta.persistence.EntityNotFoundException;
import ordersystem.DAL.CustomerRepository;
import ordersystem.DAL.OrderLineRepository;
import ordersystem.DAL.OrderRepository;
import ordersystem.domain.Customer;
import ordersystem.domain.Order;
import ordersystem.domain.OrderLine;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderLineRepository orderLineRepository;


    public OrderService(OrderRepository orderRepository,
                        CustomerRepository customerRepository,
                        OrderLineRepository orderLineRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.orderLineRepository = orderLineRepository;
    }

    public Order save(Order order) {

        // save customer if he is not in db
        Customer customer = order.getCustomer();
        if (customer != null && customer.getId() == null) {
            customerRepository.save(customer);
        }

        // save order
        Order savedOrder = orderRepository.save(order);

        // check order lines and save those, which are not in database
        List<OrderLine> orderLines = order.getOrderLines();
        if (orderLines != null && !orderLines.isEmpty()) {
            for (OrderLine orderLine : orderLines) {
                if (orderLine.getId() == null) {
                    orderLine.setOrder(savedOrder);
                    orderLineRepository.save(orderLine);
                }
            }
        }

        return savedOrder;

    }

    public Iterable<Order> findAll() {
        return orderRepository.findAll();
    }

    public Iterable<Order> findAllByProductId(Long productId) {
        return orderRepository.findAllByProductId(productId);
    }

    public Iterable<Order> findAllByCustomerId(Long customerId) {
        return orderRepository.findAllByCustomerId(customerId);
    }

    public OrderLine updateOrderLineQuantity(Long orderLineId, int newQuantity) {
        Optional<OrderLine> optionalOrderLine = orderLineRepository.findById(orderLineId);

        if (optionalOrderLine.isPresent()) {
            OrderLine orderLine = optionalOrderLine.get();

            orderLine.setQuantity(newQuantity);

            return orderLineRepository.save(orderLine);
        } else {
            throw new EntityNotFoundException("OrderLine not found with ID: " + orderLineId);
        }
    }

    public Iterable<Order> findAllByDate(Date date) {
        return orderRepository.findAllByDate(date);
    }


}
