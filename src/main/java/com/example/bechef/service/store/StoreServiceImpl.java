package com.example.bechef.service.store;

import com.example.bechef.dto.StoreDTO;
import com.example.bechef.model.store.Store;
import com.example.bechef.repository.review.ReviewRepository;
import com.example.bechef.repository.store.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    // 모든 가게 정보를 조회하는 메서드.
    @Override
    public List<Store> findAll() {
        List<Store> stores = storeRepository.findAll();
        return storeRepository.findAll();
    }

    // 특정 가게 ID로 가게 정보를 조회하는 메서드.
    @Override
    public Store infoPageByStoreId(int storeId) {
        Store storeByInfo = storeRepository.findByStoreId(storeId);
        return storeByInfo;
    }

    // 검색어를 사용하여 가게 정보를 조회하는 메서드.
    // 가게 정보와 리뷰 개수를 함께 반환.
    @Override
    public List<StoreDTO> searchStores(String query) {
        List<Object[]> results = storeRepository.searchStoresWithReviewCountAndImages(query);
        List<StoreDTO> stores = new ArrayList<>();
        for (Object[] result : results) {
            StoreDTO storeDto = new StoreDTO(
                    // 검색 결과에서 가게 정보를 추출하여 StoreDTO 객체를 생성
                    (Integer) result[0],
                    (String) result[1],
                    (String) result[2],
                    (BigDecimal) result[3],
                    (BigDecimal) result[4],
                    (BigDecimal) result[5],
                    ((Number) result[6]).intValue(),
                    (String) result[7]
            );
            stores.add(storeDto);


        }
        return stores;
    }


    @Transactional
    @Override
    public Store updateStore(Store store) {
        if (storeRepository.existsById(store.getStoreId())) {
            System.out.println("Before update: " + store);

            BigDecimal ratingAsBigDecimal = store.getStoreRating().setScale(1, RoundingMode.HALF_UP);
            System.out.println("ratingAsBigDecimal" + ratingAsBigDecimal + "ratingAsBigDecimal");


            storeRepository.updateStoreRating(store.getStoreId(), ratingAsBigDecimal);

            System.out.println("store22222" + store + "store22222");

            // 업데이트된 엔티티를 다시 조회
            Store updatedStore = storeRepository.findById(store.getStoreId())
                    .orElseThrow(() -> new EntityNotFoundException("Store not found with id: " + store.getStoreId()));
            System.out.println("After update: " + updatedStore);


            return null;
        } else {
            throw new EntityNotFoundException("Store not found with id: " + store.getStoreId());
        }
    }

    @Override
    public void updateStoreRating(int storeId) {
        List<BigDecimal> ratings = reviewRepository.findAllRatingsByStoreId(storeId);
        if (ratings.isEmpty()) {
            storeRepository.updateStoreRating(storeId, null);
        } else {
            BigDecimal averageRating = ratings.stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(ratings.size()), RoundingMode.HALF_UP);
            storeRepository.updateStoreRating(storeId, averageRating);
        }
    }
}

