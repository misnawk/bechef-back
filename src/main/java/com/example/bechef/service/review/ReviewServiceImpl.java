package com.example.bechef.service.review;

import com.example.bechef.dto.ReviewDTO;
import com.example.bechef.model.review.Review;
import com.example.bechef.model.store.Store;
import com.example.bechef.repository.review.ReviewRepository;
import com.example.bechef.repository.store.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service // Spring의 서비스 레이어를 나타내는 어노테이션
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository; // ReviewRepository 주입

    @Autowired
    private StoreRepository storeRepository; // StoreRepository 주입

    // 마이페이지 리뷰 불러오기
    @Override
    public List<ReviewDTO> getUserReviews(int memberIdx) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // 날짜 포맷터 설정
        return reviewRepository.findByMemberIdx(memberIdx).stream() // memberIdx로 리뷰 목록 조회
                .map(review -> {
                    ReviewDTO reviewDTO = new ReviewDTO();
                    reviewDTO.setReviewId(review.getReviewId()); // 리뷰 ID 설정
                    reviewDTO.setComment(review.getComment()); // 리뷰 내용 설정
                    reviewDTO.setReviewRating(review.getReviewRating()); // 리뷰 평점 설정
                    reviewDTO.setReviewDate(review.getReviewDate()); // 리뷰 작성 날짜 설정
                    reviewDTO.setMemberIdx(review.getMemberIdx()); // 회원 ID 설정
                    reviewDTO.setStoreId(review.getStoreId()); // 가게 ID 설정

                    Store store = storeRepository.findById(review.getStoreId()).orElse(null); // 가게 정보 가져오기
                    if (store != null) {
                        reviewDTO.setStoreName(store.getStoreName()); // 가게 이름 설정
                    }
                    return reviewDTO;
                })
                .collect(Collectors.toList()); // List로 변환하여 반환
    }

    // 가게에 해당하는 리뷰 찾기
    @Override
    public List<Review> findReviewByStoreId(int storeId) {
        return reviewRepository.findByStoreId(storeId); // storeId로 리뷰 목록 조회
    }

    // 리뷰 등록하기
    @Override
    public Review createReview(int memberIdx, int storeId, String comment, BigDecimal rating) {
        System.out.printf("Creating review with memberIdx: %d, storeId: %d, comment: %s, reviewRating: %s",
                memberIdx, storeId, comment, rating.toString()); // 로그 출력

        Review newReview = new Review();
        newReview.setMemberIdx(memberIdx); // 회원 ID 설정
        newReview.setStoreId(storeId); // 가게 ID 설정
        newReview.setComment(comment); // 리뷰 내용 설정
        newReview.setReviewRating(rating); // 리뷰 평점 설정
        newReview.setReviewDate(LocalDateTime.now()); // 현재 날짜 및 시간 설정
        return reviewRepository.save(newReview); // 리뷰 저장
    }

    // storeId에 해당하는 모든 리뷰의 별점을 가져오는 로직
    @Override
    public List<BigDecimal> getAllRatingsByStoreId(int storeId) {
        return reviewRepository.findAllRatingsByStoreId(storeId); // storeId로 모든 리뷰의 별점 목록 조회
    }

    // 리뷰 수정
    @Override
    public Review updateReview(int reviewId, String comment, BigDecimal rating) {
        System.out.printf("Update review with reviewId: %d, comment: %s, rating: %s", reviewId, comment, rating.toString()); // 로그 출력

        // 리뷰 업데이트 로직 구현
        Optional<Review> optionalReview = reviewRepository.findById(reviewId); // 리뷰 ID로 리뷰 조회
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            review.setComment(comment); // 리뷰 내용 업데이트
            review.setReviewRating(rating); // 리뷰 평점 업데이트
            Review updatedReview = reviewRepository.save(review); // 업데이트된 리뷰 저장
            System.out.println("Updated review: " + updatedReview); // 로그 출력
            return updatedReview;
        } else {
            throw new RuntimeException("Review not found with id: " + reviewId); // 리뷰를 찾을 수 없는 경우 예외 발생
        }
    }

    // 리뷰 삭제
    @Override
    public void deleteReview(int reviewId) {
        reviewRepository.deleteById(reviewId); // 리뷰 ID로 리뷰 삭제
    }

    // reviewId에 맞는 리뷰 테이블에서 데이터 가져오기
    @Override
    public Review getReviewById(int reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId)); // 리뷰 ID로 리뷰 조회, 없으면 예외 발생
    }
}
