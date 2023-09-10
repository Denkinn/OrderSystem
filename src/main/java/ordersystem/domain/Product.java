package ordersystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "products")
@JsonIgnoreProperties({"orderLines"})
public class Product {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "sku_code")
    private String skuCode;

    @Column(name = "unit_price")
    private double unitPrice;

    @OneToMany(mappedBy = "product")
    private List<OrderLine> orderLines;

}
