package ordersystem.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_lines")
public class OrderLine {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    private Product product;

    @ManyToOne
    @JsonIgnoreProperties("orderLines")
    private Order order;
}
