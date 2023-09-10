package ordersystem.controllers;


import ordersystem.DAL.OrderLineRepository;
import ordersystem.domain.OrderLine;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


// this controller made only for testing

@RestController
@RequestMapping("/orderLines")
public class OrderLineController {

    private final OrderLineRepository repo;


    public OrderLineController(OrderLineRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<Iterable<OrderLine>> findAll() {
        Iterable<OrderLine> orderLines = repo.findAll();

        return new ResponseEntity<>(orderLines, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrderLine> save(@RequestBody OrderLine orderLine) {
        OrderLine savedOrderLine = repo.save(orderLine);

        return new ResponseEntity<>(savedOrderLine, HttpStatus.CREATED);
    }
}
