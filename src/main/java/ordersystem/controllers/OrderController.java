package ordersystem.controllers;

import ordersystem.BLL.OrderService;
import ordersystem.domain.Order;
import ordersystem.domain.OrderLine;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Order> saveOrder(@RequestBody Order order) {
        Order savedOrder = service.save(order);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Iterable<Order>> getOrders() {
        Iterable<Order> orders = service.findAll();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/byProductId")
    public ResponseEntity<Iterable<Order>> getByProductId(@RequestParam Long productId) {
        Iterable<Order> orders = service.findAllByProductId(productId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/byCustomerId")
    public ResponseEntity<Iterable<Order>> getByCustomerId(@RequestParam Long customerId) {
        Iterable<Order> order = service.findAllByCustomerId(customerId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PostMapping("/updateLine/{lineId}")
    public ResponseEntity<OrderLine> updateLines(@PathVariable Long lineId, @RequestParam int newQuantity) {
        OrderLine updatedOrderLine = service.updateOrderLineQuantity(lineId, newQuantity);
        return new ResponseEntity<>(updatedOrderLine, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/byDate")
    public ResponseEntity<Iterable<Order>> findAllByDate(@RequestParam Date date) {
        Iterable<Order> orders = service.findAllByDate(date);
        return new ResponseEntity<>(orders, HttpStatus.OK);

    }
}
