package com.example.order.entity;

import com.example.order.repository.OrderRequestRepository;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderRequestTests {
    // Calculate total price correctly for multiple products in order
    @Test
    public void calculate_total_price_for_multiple_products() {
        Product product1 = new Product("Product 1", 10.0);
        Product product2 = new Product("Product 2", 20.0);
        Set<Product> products = Set.of(product1, product2);

        OrderRequest orderRequest = OrderRequest.builder()
                .id("123")
                .description("Test Order")
                .products(products)
                .build();

        double totalPrice = orderRequest.calculateTotalProductValue();

        assertEquals(30.0, totalPrice, 0.1);
        assertEquals(30.0, orderRequest.getFinalPrice(), 0.1);
    }

    // Calculate total for order with empty product set
    @Test
    public void calculate_total_price_for_empty_product_set() {
        OrderRequest orderRequest = OrderRequest.builder()
                .id("123")
                .description("Empty Order")
                .products(new HashSet<>())
                .build();

        double totalPrice = orderRequest.calculateTotalProductValue();

        assertEquals(0.0, totalPrice, 0.1);
        assertEquals(0.0, orderRequest.getFinalPrice(), 0.1);
    }

    // Create new order with valid products, description and ID
    @Test
    public void create_new_order_with_valid_products_description_and_id() {
        Product product1 = new Product("Product 1", 15.0);
        Product product2 = new Product("Product 2", 25.0);
        Set<Product> products = Set.of(product1, product2);

        OrderRequest orderRequest = OrderRequest.builder()
                .id("order123")
                .description("Sample Order")
                .products(products)
                .build();

        assertEquals("order123", orderRequest.getId());
        assertEquals("Sample Order", orderRequest.getDescription());
        assertEquals(products, orderRequest.getProducts());
    }

    // Persist order with products to database
    @Test
    public void persist_order_with_products_to_database() {
        Product product1 = new Product("Product 1", 10.0);
        Product product2 = new Product("Product 2", 20.0);
        Set<Product> products = Set.of(product1, product2);

        OrderRequest orderRequest = OrderRequest.builder()
                .id("123")
                .description("Test Order")
                .products(products)
                .build();
        orderRequest.calculateTotalProductValue();

        // Assuming there's a repository or service to persist the order
        OrderRequestRepository orderRepository = mock(OrderRequestRepository.class);
        when(orderRepository.save(orderRequest)).thenReturn(orderRequest);

        OrderRequest savedOrder = orderRepository.save(orderRequest);

        assertNotNull(savedOrder);
        assertEquals("123", savedOrder.getId());
        assertEquals("Test Order", savedOrder.getDescription());
        assertEquals(2, savedOrder.getProducts().size());
        assertEquals(30.0, savedOrder.getFinalPrice(), 0.1);
    }

    // Create order with null description
    @Test
    public void create_order_with_null_description() {
        Product product1 = new Product("Product 1", 10.0);
        Product product2 = new Product("Product 2", 20.0);
        Set<Product> products = Set.of(product1, product2);

        OrderRequest orderRequest = OrderRequest.builder()
                .id("123")
                .description(null)
                .products(products)
                .build();

        double totalPrice = orderRequest.calculateTotalProductValue();

        assertEquals(30.0, totalPrice, 0.1);
        assertEquals(30.0, orderRequest.getFinalPrice(), 0.1);
        assertNull(orderRequest.getDescription());
    }

    // Create order with null ID
    @Test
    public void create_order_with_null_id() {
        Product product1 = new Product("Product 1", 10.0);
        Product product2 = new Product("Product 2", 20.0);
        Set<Product> products = Set.of(product1, product2);

        OrderRequest orderRequest = OrderRequest.builder()
                .id(null)
                .description("Test Order with Null ID")
                .products(products)
                .build();

        assertNull(orderRequest.getId());
        assertEquals("Test Order with Null ID", orderRequest.getDescription());
        assertEquals(products, orderRequest.getProducts());
    }

    // Handle very large numbers of products
    @Test
    public void calculate_total_price_for_large_number_of_products() {
        Set<Product> products = new HashSet<>();
        for (int i = 0; i < 1000000; i++) {
            products.add(new Product("Product " + i, 1.0));
        }

        OrderRequest orderRequest = OrderRequest.builder()
                .id("large_test")
                .description("Large Test Order")
                .products(products)
                .build();

        double totalPrice = orderRequest.calculateTotalProductValue();

        assertEquals(1000000.0, totalPrice, 0.1);
        assertEquals(1000000.0, orderRequest.getFinalPrice(), 0.1);
    }

    // Handle very large product prices that could cause overflow
    @Test
    public void calculate_total_price_with_large_values() {
        Product product1 = new Product("Expensive Product 1", Double.MAX_VALUE / 2);
        Product product2 = new Product("Expensive Product 2", Double.MAX_VALUE / 2);
        Set<Product> products = Set.of(product1, product2);

        OrderRequest orderRequest = OrderRequest.builder()
                .id("456")
                .description("Large Value Order")
                .products(products)
                .build();

        double totalPrice = orderRequest.calculateTotalProductValue();

        assertEquals(Double.MAX_VALUE, totalPrice, 0.1);
        assertEquals(Double.MAX_VALUE, orderRequest.getFinalPrice(), 0.1);
    }

    // Verify cascade operations when adding/removing products
    @Test
    public void test_cascade_operations_on_product_addition_removal() {
        Product product1 = new Product("Product 1", 10.0);
        Product product2 = new Product("Product 2", 20.0);
        Set<Product> initialProducts = Set.of(product1);

        OrderRequest orderRequest = OrderRequest.builder()
                .id("123")
                .description("Test Order")
                .products(initialProducts)
                .build();

        // Add a product and verify cascade operation
        Set<Product> productSet = new HashSet<>();
        productSet.addAll(orderRequest.getProducts());
        productSet.add(product2);
        orderRequest.setProducts(productSet);
        double totalPriceAfterAddition = orderRequest.calculateTotalProductValue();
        assertEquals(30.0, totalPriceAfterAddition, 0.1);

        // Remove a product and verify cascade operation
        orderRequest.getProducts().remove(product1);
        double totalPriceAfterRemoval = orderRequest.calculateTotalProductValue();
        assertEquals(20.0, totalPriceAfterRemoval, 0.1);
    }

    // Validate transaction boundaries
    @Test
    public void validate_transaction_boundaries() {
        Product product1 = new Product("Product 1", 15.0);
        Product product2 = new Product("Product 2", 25.0);
        Set<Product> products = Set.of(product1, product2);

        OrderRequest orderRequest = OrderRequest.builder()
                .id("456")
                .description("Boundary Test Order")
                .products(products)
                .build();

        double totalPrice = orderRequest.calculateTotalProductValue();

        assertEquals(40.0, totalPrice, 0.1);
        assertEquals(40.0, orderRequest.getFinalPrice(), 0.1);
    }

    // Verify product references are maintained correctly
    @Test
    public void verify_product_references_are_maintained() {
        Product product1 = new Product("Product 1", 10.0);
        Product product2 = new Product("Product 2", 20.0);
        Set<Product> products = Set.of(product1, product2);

        OrderRequest orderRequest = OrderRequest.builder()
                .id("123")
                .description("Test Order")
                .products(products)
                .build();

        assertEquals(products, orderRequest.getProducts());
    }
    // Returns string with all fields populated in correct format
    @Test
    public void test_to_string_with_populated_fields() {
        Set<Product> products = new HashSet<>();
        Product product = Product.builder()
                .name("Test Product")
                .price(10.0)
                .build();
        products.add(product);

        OrderRequest orderRequest = OrderRequest.builder()
                .id("123")
                .description("Test Order")
                .finalPrice(10.0)
                .products(products)
                .build();

        String expected = "OrderRequest{id='123', description='Test Order', finalPrice=10.0, products=[" + product + "]}";
        assertEquals(expected, orderRequest.toString());
    }

    // Returns string with null id
    @Test
    public void test_to_string_with_null_id() {
        Set<Product> products = new HashSet<>();
        Product product = Product.builder()
                .name("Test Product")
                .price(10.0)
                .build();
        products.add(product);

        OrderRequest orderRequest = OrderRequest.builder()
                .description("Test Order")
                .finalPrice(10.0)
                .products(products)
                .build();

        String expected = "OrderRequest{id='null', description='Test Order', finalPrice=10.0, products=[" + product + "]}";
        assertEquals(expected, orderRequest.toString());
    }
}


