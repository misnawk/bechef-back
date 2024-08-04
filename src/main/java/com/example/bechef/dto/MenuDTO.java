package com.example.bechef.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MenuDTO {

    // 메뉴 등록할 때 사용
    private int storeId; // 가게의 고유 식별자
    private String menuName; // 메뉴의 이름
    private String menuDescription; // 메뉴의 설명
    private BigDecimal menuPrice; // 메뉴의 가격
    private String menuImageUrl; // 메뉴의 이미지 URL
    private int menuCookingTime; // 메뉴의 조리 시간
    private String menuDifficulty; // 메뉴의 난이도
    private String menuIngredients; // 메뉴의 재료
    private int menuCalories; // 메뉴의 칼로리
    private int quantity; // 메뉴의 수량
}
