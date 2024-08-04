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

@Service // Spring의 서비스 레이어를 나타내는 어노테이션
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreRepository storeRepository; // StoreRepository 주입

    @Autowired
    private ReviewRepository reviewRepository; // ReviewRepository 주입

    // 모든 가게 정보를 조회하는 메서드
    @Override
    public List<Store> findAll() {
        List<Store> stores = storeRepository.findAll(); // 모든 가게 정보를 조회
        return storeRepository.findAll(); // 조회한 가게 정보를 반환
    }

    // 특정 가게 ID로 가게 정보를 조회하는 메서드
    @Override
    public Store infoPageByStoreId(int storeId) {
        Store storeByInfo = storeRepository.findByStoreId(storeId); // 특정 가게 ID로 가게 정보를 조회
        return storeByInfo; // 조회한 가게 정보를 반환
    }

    // 검색어를 사용하여 가게 정보를 조회하는 메서드
    // 가게 정보와 리뷰 개수를 함께 반환
    @Override
    public List<StoreDTO> searchStores(String query) {
        List<Object[]> results = storeRepository.searchStoresWithReviewCountAndImages(query); // 검색어를 사용하여 가게 정보와 리뷰 개수를 조회
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
            stores.add(storeDto); // StoreDTO 객체를 리스트에 추가
        }
        return stores; // 생성된 StoreDTO 리스트를 반환
    }

    @Transactional // 트랜잭션 처리를 나타내는 어노테이션
    @Override
    public Store updateStore(Store store) {
        if (storeRepository.existsById(store.getStoreId())) { // 가게가 존재하는지 확인
            System.out.println("Before update: " + store); // 업데이트 전의 가게 정보 출력

            BigDecimal ratingAsBigDecimal = store.getStoreRating().setScale(1, RoundingMode.HALF_UP); // 평점을 소수점 첫째 자리로 반올림
            System.out.println("ratingAsBigDecimal" + ratingAsBigDecimal + "ratingAsBigDecimal"); // 반올림된 평점 출력

            storeRepository.updateStoreRating(store.getStoreId(), ratingAsBigDecimal); // 가게 평점 업데이트

            System.out.println("store22222" + store + "store22222"); // 업데이트된 가게 정보 출력

            // 업데이트된 엔티티를 다시 조회
            Store updatedStore = storeRepository.findById(store.getStoreId())
                    .orElseThrow(() -> new EntityNotFoundException("Store not found with id: " + store.getStoreId())); // 가게가 존재하지 않으면 예외 발생
            System.out.println("After update: " + updatedStore); // 업데이트 후의 가게 정보 출력

            return null; // 업데이트된 가게 정보를 반환
        } else {
            throw new EntityNotFoundException("Store not found with id: " + store.getStoreId()); // 가게가 존재하지 않으면 예외 발생
        }
    }

    // 가게의 평점을 업데이트하는 메서드
    @Override
    public void updateStoreRating(int storeId) {
        List<BigDecimal> ratings = reviewRepository.findAllRatingsByStoreId(storeId); // storeId에 해당하는 모든 리뷰의 별점을 조회
        if (ratings.isEmpty()) {
            storeRepository.updateStoreRating(storeId, null); // 별점이 없으면 평점을 null로 설정
        } else {
            BigDecimal averageRating = ratings.stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add) // 모든 별점을 더함
                    .divide(BigDecimal.valueOf(ratings.size()), RoundingMode.HALF_UP); // 별점의 평균을 계산하여 소수점 첫째 자리로 반올림
            storeRepository.updateStoreRating(storeId, averageRating); // 계산된 평균 평점으로 가게 평점을 업데이트
        }
    }
}
