package com.example.bechef.controller.review;

import com.example.bechef.dto.ReviewDTO;
import com.example.bechef.service.review.ReviewService;
import com.example.bechef.token.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private JwtUtil jwtUtil;

    // 특정 회원의 리뷰를 가져오는 메서드
    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getReviews(@RequestParam int memberIdx) {
        return ResponseEntity.ok(reviewService.getUserReviews(memberIdx)); // memberIdx에 해당하는 리뷰 목록을 반환
    }
}
