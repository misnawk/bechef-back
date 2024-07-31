package com.example.bechef.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDTO {

    private int id;
    private int memberIdx;
    private int storeId;
    private boolean favorite;
    private String storeName;

}
