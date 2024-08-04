package com.example.bechef.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class StoreDTO {
    private int store_id; // 가게의 고유 식별자
    private String store_name; // 가게의 이름
    private String store_address; // 가게의 주소
    private BigDecimal store_latitude; // 가게의 위도
    private BigDecimal store_longitude; // 가게의 경도
    private BigDecimal store_rating; // 가게의 평점
    private long reviewCount; // 리뷰 개수
    private String img; // 가게 이미지 URL
}