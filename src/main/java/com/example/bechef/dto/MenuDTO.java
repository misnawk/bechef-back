package com.example.bechef.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MenuDTO {

    //메뉴등록할때 사용
    private int storeId;
    private String menuName;
    private String menuDescription;
    private BigDecimal menuPrice;
    private String menuImageUrl;
    private int menuCookingTime;
    private String menuDifficulty;
    private String menuIngredients;
    private int menuCalories;
    private int quantity;
}
