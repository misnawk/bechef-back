package com.example.bechef.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class StoreDTO {
    private int store_id;
    private String store_name;
    private String store_address;
    private BigDecimal store_latitude;
    private BigDecimal store_longitude;
    private BigDecimal store_rating;
    private long reviewCount;
    private String img;
}