package com.example.bechef.service.storeHour;

import com.example.bechef.model.storeHours.StoreHours;

import java.util.List;

public interface StoreHoursService {

    // 특정 가게 ID에 해당하는 영업시간 목록을 조회하는 메서드
    List<StoreHours> findByStoreId(int storeId);
}
