package com.example.bechef.repository.store;

import com.example.bechef.dto.StoreDTO;
import com.example.bechef.model.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {

    @Query(value = "SELECT s.store_id, s.store_name, s.store_address, s.store_latitude, s.store_longitude, s.store_rating, " +
            "COUNT(DISTINCT r.review_id) as reviewCount, " +
            "GROUP_CONCAT(DISTINCT si.image_url SEPARATOR ',') as imageUrls " +
            "FROM stores s " +
            "LEFT JOIN reviews r ON s.store_id = r.store_id " +
            "LEFT JOIN store_images si ON s.store_id = si.store_id " +
            "WHERE s.store_name LIKE %:query% OR s.store_address LIKE %:query% " +
            "GROUP BY s.store_id", nativeQuery = true)
    List<Object[]> searchStoresWithReviewCountAndImages(@Param("query") String query);

    Store findByStoreId(int storeId);



    @Modifying
    @Query("UPDATE Store s SET s.storeRating = :rating WHERE s.storeId = :storeId")
    void updateStoreRating(@Param("storeId") int storeId, @Param("rating") BigDecimal rating);

}

