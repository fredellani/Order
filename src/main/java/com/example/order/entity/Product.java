package com.example.order.entity;

import com.example.order.dto.ProductCreate;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    @NonNull
    private String name;
    @NonNull
    private double price;

    public Set<Product> toProductSet(Set<ProductCreate> productsCreate) {
        return productsCreate.stream()
                .map(this::toProduct)
                .collect(Collectors.toSet());
    }

    public Product toProduct(ProductCreate productCreate) {
        this.name = productCreate.getName();
        this.price = productCreate.getPrice();
        return new Product(this.name, this.price);
    }
}
