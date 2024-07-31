package com.example.bechef.repository.review;

import com.example.bechef.model.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    // MemberIdx를 사용하여 리뷰를 찾는 메서드
    List<Review> findByMemberIdx(int memberIdx);

    // StoreId를 사용하여 리뷰를 찾는 메서드
    List<Review> findByStoreId(int storeId);



    //리뷰 DB에 넣기위해 해당 데이터 가져오기
    List<Review> findByMemberIdxAndStoreId(int memberIdx, int storeId);


    //storeId 에 해당하는 리뷰 테이블의 별점 모두가져오기
    @Query("SELECT r.reviewRating FROM Review r WHERE r.storeId = :storeId")
    List<BigDecimal> findAllRatingsByStoreId(@Param("storeId") int storeId);
}
