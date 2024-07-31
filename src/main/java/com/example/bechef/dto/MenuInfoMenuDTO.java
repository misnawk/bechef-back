package com.example.bechef.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuInfoMenuDTO {

    private int menuId;
    private String kitName;
    private String kitIngredient;
    private String imageUrl;
    private int cookingTime;
    private String difficulty;
    private int calories;
    private String description; //설명
    private int kitCount; //수량
}
