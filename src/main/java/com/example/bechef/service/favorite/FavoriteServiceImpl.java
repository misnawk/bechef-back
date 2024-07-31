package com.example.bechef.service.favorite;

import com.example.bechef.dto.FavoriteDTO;
import com.example.bechef.model.favorite.Favorite;
import com.example.bechef.model.store.Store;
import com.example.bechef.repository.favorite.FavoriteRepository;
import com.example.bechef.repository.store.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private StoreRepository storeRepository;

    // 마이 페이지 찜목록
    @Override
    public List<FavoriteDTO> getFavoriteStores(int memberIdx) {
        return favoriteRepository.findByMemberIdxAndFavoriteTrue(memberIdx)
                .stream()
                .map(favorite -> {
                    FavoriteDTO favoriteDTO = new FavoriteDTO();
                    favoriteDTO.setId(favorite.getId());
                    favoriteDTO.setMemberIdx(favorite.getMemberIdx());
                    favoriteDTO.setStoreId(favorite.getStoreId());
                    favoriteDTO.setFavorite(favorite.isFavorite());
                    Store store = storeRepository.findById(favorite.getStoreId()).orElse(null);
                    if (store != null) {
                        favoriteDTO.setStoreName(store.getStoreName());
                    }
                    return favoriteDTO;
                })
                .collect(Collectors.toList());
    }

    // 인포페이지
    @Override
    public Favorite getFavoriteById(int storeId, int memberIdx) {
        return favoriteRepository.findByStoreIdAndMemberIdx(storeId, memberIdx);
    }


    @Override
    public Favorite updateOrCreateFavorite(int memberIdx, int storeId, boolean favorite) {
        Optional<Favorite> existingFavorite = favoriteRepository.findByMemberIdxAndStoreId(memberIdx, storeId);

        //이미 memberIdx 와 storeId 와 favorite 의 값이 있다면
        if (existingFavorite.isPresent()) {
            //메서드를 사용해서 가져온 데이터를 새로운 객체에 담기
            Favorite favoriteToUpdate = existingFavorite.get();

            //새로운 객체에 새로운 찜상태 저장
            favoriteToUpdate.setFavorite(favorite);

            return favoriteRepository.save(favoriteToUpdate);
        } else {
            Favorite newFavorite = new Favorite();
            newFavorite.setMemberIdx(memberIdx);
            newFavorite.setStoreId(storeId);
            newFavorite.setFavorite(favorite);
            return favoriteRepository.save(newFavorite);

        }
    }
}
