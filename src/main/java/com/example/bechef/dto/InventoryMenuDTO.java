package com.example.bechef.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryMenuDTO {
    private int menuId; // 메뉴의 고유 식별자
    private String menuName; // 메뉴의 이름
    private String menuDescription; // 메뉴의 설명
    private BigDecimal menuPrice; // 메뉴의 가격
    private String menuImageUrl; // 메뉴의 이미지 URL
    private int quantity; // 메뉴의 재고 수량
    private int storeId; // 가게의 고유 식별자
    private String storeName; // 가게의 이름
}
