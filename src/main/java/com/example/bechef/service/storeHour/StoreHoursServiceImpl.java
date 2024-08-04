package com.example.bechef.service.storeHour;

import com.example.bechef.model.storeHours.StoreHours;
import com.example.bechef.repository.store.StoreHoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreHoursServiceImpl implements StoreHoursService {


    @Autowired
    private StoreHoursRepository storeHoursRepository;

    @Override
    // 특정 가게 ID에 해당하는 영업시간 목록을 조회하는 메서드
    public List<StoreHours> findByStoreId(int storeId) {
        return  storeHoursRepository.findAllByStoreId(storeId);
    }
}
