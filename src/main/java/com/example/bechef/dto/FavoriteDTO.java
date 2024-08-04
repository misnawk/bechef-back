package com.example.bechef.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDTO {

    private int id; // 즐겨찾기 항목의 고유 식별자
    private int memberIdx; // 회원의 고유 식별자
    private int storeId; // 가게의 고유 식별자
    private boolean favorite; // 즐겨찾기 여부를 나타내는 필드
    private String storeName; // 가게의 이름

}
