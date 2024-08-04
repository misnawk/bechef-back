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
    private FavoriteRepository favoriteRepository; // FavoriteRepository 주입

    @Autowired
    private StoreRepository storeRepository; // StoreRepository 주입

    // 마이 페이지 찜목록 불러오기
    @Override
    public List<FavoriteDTO> getFavoriteStores(int memberIdx) {
        return favoriteRepository.findByMemberIdxAndFavoriteTrue(memberIdx) // memberIdx와 favorite이 true인 찜목록 찾기
                .stream()
                .map(favorite -> {
                    FavoriteDTO favoriteDTO = new FavoriteDTO();
                    favoriteDTO.setId(favorite.getId()); // 찜 ID 설정
                    favoriteDTO.setMemberIdx(favorite.getMemberIdx()); // 회원 ID 설정
                    favoriteDTO.setStoreId(favorite.getStoreId()); // 가게 ID 설정
                    favoriteDTO.setFavorite(favorite.isFavorite()); // 찜 상태 설정
                    Store store = storeRepository.findById(favorite.getStoreId()).orElse(null); // 가게 정보 가져오기
                    if (store != null) {
                        favoriteDTO.setStoreName(store.getStoreName()); // 가게 이름 설정
                    }
                    return favoriteDTO;
                })
                .collect(Collectors.toList()); // List로 변환하여 반환
    }

    // 인포 페이지 찜 정보 불러오기
    @Override
    public Favorite getFavoriteById(int storeId, int memberIdx) {
        return favoriteRepository.findByStoreIdAndMemberIdx(storeId, memberIdx); // storeId와 memberIdx로 찜 정보 찾기
    }

    // 찜 상태 업데이트 또는 생성
    @Override
    public Favorite updateOrCreateFavorite(int memberIdx, int storeId, boolean favorite) {
        Optional<Favorite> existingFavorite = favoriteRepository.findByMemberIdxAndStoreId(memberIdx, storeId); // memberIdx와 storeId로 기존 찜 정보 찾기

        // 이미 memberIdx와 storeId와 favorite의 값이 있다면
        if (existingFavorite.isPresent()) {
            // 메서드를 사용해서 가져온 데이터를 새로운 객체에 담기
            Favorite favoriteToUpdate = existingFavorite.get();

            // 새로운 객체에 새로운 찜 상태 저장
            favoriteToUpdate.setFavorite(favorite);

            return favoriteRepository.save(favoriteToUpdate); // 업데이트된 찜 정보 저장
        } else {
            Favorite newFavorite = new Favorite();
            newFavorite.setMemberIdx(memberIdx); // 회원 ID 설정
            newFavorite.setStoreId(storeId); // 가게 ID 설정
            newFavorite.setFavorite(favorite); // 찜 상태 설정
            return favoriteRepository.save(newFavorite); // 새로운 찜 정보 저장
        }
    }
}
