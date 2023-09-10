package ordersystem.controllers;

import ordersystem.BLL.CustomerService;
import ordersystem.domain.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<Iterable<Customer>> getCustomers() {
        Iterable<Customer> customers = service.findAll();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Customer>> getByCustomerId(@PathVariable Long id) {
        Optional<Customer> customer = service.findById(id);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Customer> saveCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = service.save(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }


}
