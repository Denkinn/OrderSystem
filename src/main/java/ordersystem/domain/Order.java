package ordersystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name = "date")
    private Date date = new Date();

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "order")
    @JsonIgnoreProperties("order")
    private List<OrderLine> orderLines;

}
