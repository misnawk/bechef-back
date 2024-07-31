package com.example.bechef.service.review;

import com.example.bechef.dto.ReviewDTO;
import com.example.bechef.model.review.Review;

import java.math.BigDecimal;
import java.util.List;

public interface ReviewService {

    //마이페이지 리뷰 불러오기
    public List<ReviewDTO> getUserReviews(int memberIdx);

    //가게에 해당하는 리뷰 찾기
    List<Review> findReviewByStoreId(int storeId);

    //리뷰 DB에 저장하기
    Review createReview(int memberIdx, int storeId, String comment, BigDecimal rating);


    // storeId에 해당하는 모든 리뷰의 별점을 가져오는 로직
    List<BigDecimal> getAllRatingsByStoreId(int storeId);

    //리뷰 수정하기
    Review updateReview(int reviewId, String comment, BigDecimal rating);

    //리뷰 삭제
    void  deleteReview(int reviewId);

    //reviewId 맞는 review 테이블에서 데이터 가져옴
    Review getReviewById(int reviewId);


}
