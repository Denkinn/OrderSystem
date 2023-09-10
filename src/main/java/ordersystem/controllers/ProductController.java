package ordersystem.controllers;

import ordersystem.BLL.ProductService;
import ordersystem.domain.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Iterable<Product>> getProducts() {
        Iterable<Product> products = service.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> saveProduct(@RequestBody Product product) {
        Product savedProduct = service.save(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

}
