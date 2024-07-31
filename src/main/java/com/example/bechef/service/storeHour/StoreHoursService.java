package com.example.bechef.service.storeHour;

import com.example.bechef.model.storeHours.StoreHours;

import java.util.List;

public interface StoreHoursService {
    List<StoreHours> findByStoreId(int storeId);
}
