package com.example.bechef.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuInfoPageDTO {

    private int store_id; // 가게의 고유 식별자
    private String store_name; // 가게의 이름
    private BigDecimal averageRating; // 가게의 평균 평점
    private String store_image_url; // 가게의 이미지 URL
    private String store_address; // 가게의 주소
    private String store_phone; // 가게의 전화번호
}
