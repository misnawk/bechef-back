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

    private int menuId; // 메뉴의 고유 식별자
    private String kitName; // 키트의 이름
    private String kitIngredient; // 키트의 재료
    private String imageUrl; // 이미지 URL
    private int cookingTime; // 조리 시간
    private String difficulty; // 난이도
    private int calories; // 칼로리
    private String description; // 설명
    private int kitCount; // 수량
}
