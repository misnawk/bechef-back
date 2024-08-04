package com.example.bechef.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private int reviewId; // 리뷰의 고유 식별자
    private String userName; // 리뷰를 작성한 사용자 이름
    private String comment; // 리뷰 내용
    private BigDecimal reviewRating; // 리뷰 평점
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") // 날짜 및 시간 형식 지정
    private LocalDateTime reviewDate; // 리뷰 작성 날짜 및 시간
    private int memberIdx; // 회원의 고유 식별자
    private int review_rating; // 리뷰 평점 (중복된 필드로 보임)
    private int storeId; // 가게의 고유 식별자
    private String storeName; // 가게의 이름
}
