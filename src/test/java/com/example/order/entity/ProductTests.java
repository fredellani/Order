package com.example.order.entity;

import com.example.order.dto.ProductCreate;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ProductTests {
    // Convert single ProductCreate to Product with valid name and price
    @Test()
    public void test_convert_single_product_create_to_product() {
        ProductCreate productCreate = new ProductCreate();
        productCreate.setName("Test Product");
        productCreate.setPrice(10.99);

        Product product = new Product();
        Set<ProductCreate> productCreates = Set.of(productCreate);

        Set<Product> result = product.toProductSet(productCreates);

        assertNotNull(result);
        assertEquals(1, result.size());
        Product convertedProduct = result.iterator().next();
        assertEquals("Test Product", convertedProduct.getName());
        assertEquals(10.99, convertedProduct.getPrice(), 0.001);
    }

    // Handle empty Set of ProductCreate objects
    @Test
    public void test_convert_empty_product_create_set() {
        Product product = new Product();
        Set<ProductCreate> emptySet = Set.of();

        Set<Product> result = product.toProductSet(emptySet);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Convert single ProductCreate to Product with valid name and price
    @Test
    public void convert_productcreate_to_product_with_valid_name_and_price() {
        // Arrange
        ProductCreate productCreate = new ProductCreate();
        productCreate.setName("Test Product");
        productCreate.setPrice(99.99);

        // Act
        Product product = new Product();
        Product result = product.toProduct(productCreate);

        // Assert
        assertEquals("Test Product", result.getName());
        assertEquals(99.99, result.getPrice(), 0.01);
    }

    // Get product name and price using getter methods
    @Test
    public void test_get_product_name_and_price() {
        Product product = new Product("Sample Product", 29.99);

        assertEquals("Sample Product", product.getName());
        assertEquals(29.99, product.getPrice(), 0.001);
    }

    // Convert Set of ProductCreate objects to Set of Products
    @Test
    public void test_convert_set_of_product_create_to_set_of_products() {
        ProductCreate productCreate1 = new ProductCreate();
        productCreate1.setName("Product 1");
        productCreate1.setPrice(20.99);

        ProductCreate productCreate2 = new ProductCreate();
        productCreate2.setName("Product 2");
        productCreate2.setPrice(30.99);

        Product product = new Product();
        Set<ProductCreate> productCreates = Set.of(productCreate1, productCreate2);

        Set<Product> result = product.toProductSet(productCreates);

        assertNotNull(result);
        assertEquals(2, result.size());

        for (Product convertedProduct : result) {
            if (convertedProduct.getName().equals("Product 1")) {
                assertEquals(20.99, convertedProduct.getPrice(), 0.001);
            } else if (convertedProduct.getName().equals("Product 2")) {
                assertEquals(30.99, convertedProduct.getPrice(), 0.001);
            } else {
                fail("Unexpected product name: " + convertedProduct.getName());
            }
        }
    }

    // Handle empty Set of ProductCreate objects
    @Test
    public void test_empty_product_create_set() {
        Product product = new Product();
        Set<ProductCreate> emptyProductCreates = Set.of();

        Set<Product> result = product.toProductSet(emptyProductCreates);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Handle extremely large price values
    @Test
    public void test_handle_extremely_large_price_values() {
        ProductCreate productCreate = new ProductCreate();
        productCreate.setName("Expensive Product");
        productCreate.setPrice(Double.MAX_VALUE);

        Product product = new Product();
        Set<ProductCreate> productCreates = Set.of(productCreate);

        Set<Product> result = product.toProductSet(productCreates);

        assertNotNull(result);
        assertEquals(1, result.size());
        Product convertedProduct = result.iterator().next();
        assertEquals("Expensive Product", convertedProduct.getName());
        assertEquals(Double.MAX_VALUE, convertedProduct.getPrice(), 0.001);
    }

    // Ensure thread safety during batch conversion
    @Test
    public void test_thread_safety_during_batch_conversion() throws InterruptedException {
        ProductCreate productCreate1 = new ProductCreate();
        productCreate1.setName("Product 1");
        productCreate1.setPrice(20.0);

        ProductCreate productCreate2 = new ProductCreate();
        productCreate2.setName("Product 2");
        productCreate2.setPrice(30.0);

        Set<ProductCreate> productCreates = Set.of(productCreate1, productCreate2);

        Product product = new Product();

        Runnable task = () -> {
            Set<Product> result = product.toProductSet(productCreates);
            assertNotNull(result);
            assertEquals(2, result.size());
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

    // Verify memory usage with large Sets
    @Test
    public void test_memory_usage_with_large_product_set() {
        Set<ProductCreate> largeProductCreates = new HashSet<>();
        for (int i = 0; i < 1000000; i++) {
            ProductCreate productCreate = new ProductCreate();
            productCreate.setName("Product " + i);
            productCreate.setPrice(i * 1.0);
            largeProductCreates.add(productCreate);
        }

        Product product = new Product();
        Set<Product> result = product.toProductSet(largeProductCreates);

        assertNotNull(result);
        assertEquals(1000000, result.size());
    }
}
