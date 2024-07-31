package com.example.bechef.service.review;

import com.example.bechef.dto.ReviewDTO;
import com.example.bechef.model.review.Review;
import com.example.bechef.model.store.Store;
import com.example.bechef.repository.review.ReviewRepository;
import com.example.bechef.repository.store.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Override
    public List<ReviewDTO> getUserReviews(int memberIdx) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return reviewRepository.findByMemberIdx(memberIdx).stream()
                .map(review -> {
                    ReviewDTO reviewDTO = new ReviewDTO();
                    reviewDTO.setReviewId(review.getReviewId());
                    reviewDTO.setComment(review.getComment());
                    reviewDTO.setReviewRating(review.getReviewRating());
                    reviewDTO.setReviewDate(review.getReviewDate());
                    reviewDTO.setMemberIdx(review.getMemberIdx());
                    reviewDTO.setStoreId(review.getStoreId());



                    Store store = storeRepository.findById(review.getStoreId()).orElse(null);
                    if (store != null) {
                        reviewDTO.setStoreName(store.getStoreName());
                    } return reviewDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Review> findReviewByStoreId(int storeId) {
        return reviewRepository.findByStoreId(storeId);
    }

    //리뷰 등록하기
    @Override
    public Review createReview(int memberIdx, int storeId, String comment, BigDecimal rating) {
        System.out.printf("Creating review with memberIdx: %d, storeId: %d, comment: %s, reviewRating: %s",
                memberIdx, storeId, comment, rating.toString());

        Review newReview = new Review();
        newReview.setMemberIdx(memberIdx);
        newReview.setStoreId(storeId);
        newReview.setComment(comment);
        newReview.setReviewRating(rating);
        newReview.setReviewDate(LocalDateTime.now());
        return reviewRepository.save(newReview);
    }

    //storeId 에 해당하는 리뷰 테이블의 별점 모두가져오기
    @Override
    public List<BigDecimal> getAllRatingsByStoreId(int storeId) {
        return reviewRepository.findAllRatingsByStoreId(storeId);
    }

    //리뷰 수정
    @Override
    public Review updateReview(int reviewId, String comment, BigDecimal rating) {
        System.out.printf("Update review with reviewId: %d, comment: %s, rating: %s", reviewId, comment, rating.toString());

        // 리뷰 업데이트 로직 구현
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            review.setComment(comment);
            review.setReviewRating(rating);
            Review updatedReview = reviewRepository.save(review);
            System.out.println("Updated review: " + updatedReview);
            return updatedReview;
        } else {
            throw new RuntimeException("Review not found with id: " + reviewId);
        }
    }

    //리뷰 삭제
    @Override
    public void deleteReview(int reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    @Override
    public Review getReviewById(int reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));
    }


}
