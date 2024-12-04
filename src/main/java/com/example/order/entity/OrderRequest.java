package com.example.order.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.transaction.Transactional;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Transactional
public class OrderRequest {
    @Id
    private String id;

    private String description;

    private double finalPrice;

    @OneToMany(cascade= CascadeType.ALL)
    private Set<Product> products;

    public Double calculateTotalProductValue() {
        this.finalPrice = products.stream()
                .mapToDouble(Product::getPrice)
                .sum();
        return this.finalPrice;
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", finalPrice=" + finalPrice +
                ", products=" + products +
                '}';
    }
}