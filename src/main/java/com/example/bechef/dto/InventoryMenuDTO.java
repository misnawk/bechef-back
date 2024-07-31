package com.example.bechef.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryMenuDTO {
    private int menuId;
    private String menuName;
    private String menuDescription;
    private BigDecimal menuPrice;
    private String menuImageUrl;
    private int quantity;
    private int storeId;
    private String storeName;
}
