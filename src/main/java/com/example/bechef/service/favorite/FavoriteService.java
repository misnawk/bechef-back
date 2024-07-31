package com.example.bechef.service.favorite;

import com.example.bechef.dto.FavoriteDTO;
import com.example.bechef.model.favorite.Favorite;

import java.util.List;

public interface FavoriteService {
    // 마이페이지 좋아요 불러오기
    public List<FavoriteDTO> getFavoriteStores(int memberIdx);

    // 인포페이지 찜 불러오기
    Favorite getFavoriteById(int storeId, int memberIdx);

    //찜 업데이트
    Favorite updateOrCreateFavorite(int memberIdx, int storeId, boolean favorite);
}
