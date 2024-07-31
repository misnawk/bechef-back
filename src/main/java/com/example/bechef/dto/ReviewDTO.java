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
    private int reviewId;
    private String userName;
    private String comment;
    private BigDecimal reviewRating;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime reviewDate;
    private int memberIdx;
    private int review_rating;
    private int storeId; //
    private String storeName; //
}
