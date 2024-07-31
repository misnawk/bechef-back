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
    public StoreImage storeImgByStoreId(int storeId) {
        return storeImageRepository.findByStoreId(storeId);
    }
}
