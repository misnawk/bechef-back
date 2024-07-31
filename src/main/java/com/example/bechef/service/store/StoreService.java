package com.example.bechef.service.store;

import com.example.bechef.dto.StoreDTO;
import com.example.bechef.model.store.Store;

import java.util.List;

public interface StoreService {

    List<StoreDTO> searchStores(String query);

    //관리자 메뉴등록 페이지에서 드롭박스에 가게이름 전부 나열하기 위한 메서드
    List<Store> findAll();

    // 별점, 리뷰 작성 평균값 계산 해서 새로 렌더링
    Store updateStore(Store store);

    //infoPage 가게 정보 불러오기
    Store infoPageByStoreId(int storeId);


    void updateStoreRating(int storeId);


}
