package com.example.bechef.repository.storeImage;

import com.example.bechef.model.storeImage.StoreImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreImageRepository extends JpaRepository<StoreImage,Integer> {
    StoreImage findByStoreId(int storeId);
}
