package com.example.order.utils;

import com.example.order.entity.Product;
import com.example.order.utils.MapProductToJsonConverter;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MapProductToJsonConverterTests {
    // Convert a valid product map to JSON string successfully
    @Test
    public void test_convert_valid_product_map_to_json() {
        MapProductToJsonConverter converter = new MapProductToJsonConverter();
        Map<String, Product> productMap = Map.of(
                "1", new Product("Product1", 10),
                "2", new Product("Product2", 20)
        );
        String jsonResult = converter.convertToDatabaseColumn(productMap);
        assertNotNull(jsonResult);
        assertTrue(jsonResult.contains("\"name\":\"Product1\""));
        assertTrue(jsonResult.contains("\"price\":10.0"));
    }

    // Handle an empty product map conversion to JSON
    @Test
    public void test_convert_empty_product_map_to_json() {
        MapProductToJsonConverter converter = new MapProductToJsonConverter();
        Map<String, Product> emptyProductMap = Map.of();
        String jsonResult = converter.convertToDatabaseColumn(emptyProductMap);
        assertNotNull(jsonResult);
        assertEquals("{}", jsonResult);
    }

    // Convert a valid JSON string back to a product map successfully
    @Test
    public void test_convert_valid_json_to_product_map() {
        MapProductToJsonConverter converter = new MapProductToJsonConverter();
        String jsonInput = "{\"1\":{\"id\":\"1\",\"name\":\"Product1\",\"price\":10.0},\"2\":{\"id\":\"2\",\"name\":\"Product2\",\"price\":20.0}}";
        Map<String, Product> productMap = converter.convertToEntityAttribute(jsonInput);
        assertNotNull(productMap);
        assertEquals(2, productMap.size());
        assertTrue(productMap.containsKey("1"));
        assertEquals("Product1", productMap.get("1").getName());
        assertEquals(10.0, productMap.get("1").getPrice(), 0.1);
    }

    @Test
    public void test_convert_to_database_column_throws_exception() {
        MapProductToJsonConverter converter = new MapProductToJsonConverter();
        Map<String, Product> invalidProductMap = new HashMap<>();
        invalidProductMap.put(null, null);
        assertThrows(IllegalArgumentException.class, () -> {
            converter.convertToDatabaseColumn(invalidProductMap);
        });
    }

    // Handle an empty JSON string conversion to a product map
    @Test
    public void test_convert_empty_json_to_product_map() {
        MapProductToJsonConverter converter = new MapProductToJsonConverter();
        String emptyJson = "{}";
        Map<String, Product> productMap = converter.convertToEntityAttribute(emptyJson);
        assertNotNull(productMap);
        assertTrue(productMap.isEmpty());
    }

    // Validate that the ObjectMapper is correctly configured for conversion
    @Test
    public void test_object_mapper_configuration() {
        MapProductToJsonConverter converter = new MapProductToJsonConverter();
        Map<String, Product> productMap = Map.of(
                "1", new Product("Product1", 10),
                "2", new Product("Product2", 20)
        );
        String jsonResult = converter.convertToDatabaseColumn(productMap);
        assertNotNull(jsonResult);
        assertTrue(jsonResult.contains("\"name\":\"Product1\""));
        assertTrue(jsonResult.contains("\"price\":10.0"));
        Map<String, Product> resultMap = converter.convertToEntityAttribute(jsonResult);
        assertNotNull(resultMap);
        assertEquals(2, resultMap.size());
        assertEquals("Product1", resultMap.get("1").getName());
        assertEquals(10.0, resultMap.get("1").getPrice(), 0.1);
    }

    // Ensure exceptions are thrown with meaningful messages on conversion failures
    @Test
    public void test_convert_invalid_json_to_product_map_throws_exception() {
        MapProductToJsonConverter converter = new MapProductToJsonConverter();
        String invalidJson = "{invalidJson}";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            converter.convertToEntityAttribute(invalidJson);
        });
        assertEquals("Error converting product JSON to map", exception.getMessage());
    }

    // Test with large product maps for performance considerations
    @Test
    public void test_convert_large_product_map_to_json_performance() {
        MapProductToJsonConverter converter = new MapProductToJsonConverter();
        Map<String, Product> largeProductMap = new HashMap<>();
        for (int i = 0; i < 10000; i++) {
            largeProductMap.put(String.valueOf(i),
                    new Product("Product" + i, i * 10.0));
        }
        long startTime = System.currentTimeMillis();
        String jsonResult = converter.convertToDatabaseColumn(largeProductMap);
        long endTime = System.currentTimeMillis();
        assertNotNull(jsonResult);
        assertTrue("Conversion took too long", endTime - startTime < 5000);
    }

    // Verify thread safety of the converter in concurrent environments
    @Test
    public void test_thread_safety_of_converter() throws InterruptedException {
        MapProductToJsonConverter converter = new MapProductToJsonConverter();
        Map<String, Product> productMap = Map.of(
                "1", new Product("Product1", 10),
                "2", new Product("Product2", 20)
        );
        Runnable task = () -> {
            String jsonResult = converter.convertToDatabaseColumn(productMap);
            assertNotNull(jsonResult);
            assertTrue(jsonResult.contains("\"id\":\"1\""));
            assertTrue(jsonResult.contains("\"name\":\"Product1\""));
            assertTrue(jsonResult.contains("\"price\":10.0"));
        };
        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }
}