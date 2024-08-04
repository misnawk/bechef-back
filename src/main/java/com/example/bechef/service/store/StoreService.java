package com.example.bechef.service.store;

import com.example.bechef.dto.StoreDTO;
import com.example.bechef.model.store.Store;

import java.util.List;

public interface StoreService {

    // 검색 쿼리를 사용하여 가게 목록을 검색하는 메서드
    List<StoreDTO> searchStores(String query);

    // 관리자 메뉴 등록 페이지에서 드롭박스에 모든 가게 이름을 나열하기 위한 메서드
    List<Store> findAll();

    // 별점, 리뷰 작성 평균값을 계산하여 가게 정보를 업데이트하는 메서드
    Store updateStore(Store store);

    // infoPage에서 가게 정보를 불러오는 메서드
    Store infoPageByStoreId(int storeId);

    // 가게의 평점을 업데이트하는 메서드
    void updateStoreRating(int storeId);
}
