package com.example.bechef.service.storeImage;

import com.example.bechef.model.storeImage.StoreImage;

public interface StoreImageService {

    // 특정 가게 ID에 해당하는 가게 이미지를 조회하는 메서드
    StoreImage storeImgByStoreId(int storeId);
}
