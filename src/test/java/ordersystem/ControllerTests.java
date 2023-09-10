package ordersystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import ordersystem.BLL.OrderService;
import ordersystem.domain.Customer;
import ordersystem.domain.Order;
import ordersystem.domain.OrderLine;
import ordersystem.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void saveCustomerTest() throws Exception {

        Customer customer = new Customer("customer", "code", "customer@mail.com", "555555555");
        String customerJson = objectMapper.writeValueAsString(customer);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.fullName").value("customer"));
    }

    @Test
    public void saveProductTest() throws Exception {

        Product product = new Product();
        product.setName("macbook");
        product.setSkuCode("MNYF2XX/A");
        product.setUnitPrice(800);
        String productJson = objectMapper.writeValueAsString(product);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("macbook"))
                .andExpect(jsonPath("$.unitPrice").value(800));
    }

    @Test
    public void saveOrderTest() throws Exception {

        Date testDate = new Date();
        Order order = new Order();
        order.setDate(testDate);
        String orderJson = objectMapper.writeValueAsString(order);

        MvcResult result = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Order savedOrder = objectMapper.readValue(jsonResponse, Order.class);

        Date savedDate = savedOrder.getDate();

        assertEquals(savedDate, testDate);

    }

    @Test
    public void updateLinesTest() throws Exception {
        OrderLine orderLine = new OrderLine();
        orderLine.setQuantity(2);
        String orderLineJson = objectMapper.writeValueAsString(orderLine);

        MvcResult result = mockMvc.perform(post("/orderLines")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(orderLineJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.quantity").value(2))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        OrderLine savedOrderLine = objectMapper.readValue(jsonResponse, OrderLine.class);
        Long orderLineId = savedOrderLine.getId();

        mockMvc.perform(post("/orders/updateLine/" + orderLineId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("newQuantity", "4"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.id").value(orderLineId))
                .andExpect(jsonPath("$.quantity").value(4));

    }

    @Test
    public void findOrdersByCustomerTest() throws Exception {

        Customer customer1 = new Customer("customer1", "code1", "customer1@mail.com", "555555555");
        Customer customer2 = new Customer("customer2", "code2", "customer2@mail.com", "555555556");

        MvcResult result = mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer1)))
                .andReturn();
        mockMvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(customer2)));

        String jsonResponse = result.getResponse().getContentAsString();
        Customer savedCustomer1 = objectMapper.readValue(jsonResponse, Customer.class);

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        Order order = new Order();
        order.setCustomer(savedCustomer1);
        mockMvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(order)));

        mockMvc.perform(get("/orders/byCustomerId")
                        .param("customerId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mockMvc.perform(get("/orders/byCustomerId")
                        .param("customerId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));



    }

    @Test
    public void findOrdersByProductTest() throws Exception {

        insertProducts();

        mockMvc.perform(get("/orders/byProductId")
                        .param("productId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mockMvc.perform(get("/orders/byProductId")
                        .param("productId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));


    }

    @Test
    public void insertProducts() throws Exception {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("macbook");
        product1.setUnitPrice(800);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("iphone");
        product2.setUnitPrice(600);

        mockMvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(product1)));
        mockMvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(product2)));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));


        // Order1 has both product1 and product2 in orderLines
        Order order1 = new Order();
        OrderLine orderLine1 = new OrderLine();
        orderLine1.setProduct(product1);
        orderLine1.setQuantity(1);
        OrderLine orderLine2 = new OrderLine();
        orderLine2.setProduct(product2);
        orderLine2.setQuantity(1);
        List<OrderLine> orderLines1 = new ArrayList<>();
        orderLines1.add(orderLine1);
        orderLines1.add(orderLine2);
        order1.setOrderLines(orderLines1);

        // Order2 has only product2
        Order order2 = new Order();
        OrderLine orderLine3 = new OrderLine();
        orderLine3.setProduct(product2);
        orderLine3.setQuantity(2);
        List<OrderLine> orderLines2 = new ArrayList<>();
        orderLines2.add(orderLine3);
        order2.setOrderLines(orderLines2);


        mockMvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(order1)));
        mockMvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(order2)));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        mockMvc.perform(get("/orderLines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

}
