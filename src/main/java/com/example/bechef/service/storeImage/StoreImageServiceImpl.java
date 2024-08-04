package com.example.bechef.service.storeImage;

import com.example.bechef.model.storeImage.StoreImage;
import com.example.bechef.repository.storeImage.StoreImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreImageServiceImpl implements StoreImageService{

    @Autowired
    private StoreImageRepository storeImageRepository;

    @Override
    // 특정 가게 ID에 해당하는 가게 이미지를 조회하는 메서드
    public StoreImage storeImgByStoreId(int storeId) {
        return storeImageRepository.findByStoreId(storeId);
    }
}
