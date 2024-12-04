package com.example.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.checkerframework.checker.index.qual.NonNegative;

import java.io.Serializable;

@Getter
@Setter
public class ProductCreate implements Serializable {
    @NonNull
    private String name;
    @NonNegative
    private double price;
}
