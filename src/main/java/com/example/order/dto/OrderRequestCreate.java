package com.example.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class OrderRequestCreate implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @NotNull(message = "Id is mandatory.")
    private String id;
    @NotNull(message = "Description is mandatory.")
    private String description;
    @NotNull(message = "Product items is mandatory.")
    private List<ProductCreate> items;
}
