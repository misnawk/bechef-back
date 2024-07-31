package com.example.bechef.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuInfoPageDTO {

    private int store_id;
    private String store_name;
    private BigDecimal averageRating;
    private String store_image_url;
    private String store_address;
    private String store_phone;
}
