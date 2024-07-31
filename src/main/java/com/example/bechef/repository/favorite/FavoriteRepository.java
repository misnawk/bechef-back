package com.example.bechef.repository.favorite;

import com.example.bechef.model.favorite.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    List<Favorite> findByMemberIdxAndFavoriteTrue(int memberIdx); //목록불러오기

    Favorite findByStoreIdAndMemberIdx(int storeId,int memberIdx);

    Optional<Favorite> findByMemberIdxAndStoreId(int memberIdx, int storeId);
}
