package com.example.bechef.controller.infoController;

import com.example.bechef.dto.MenuInfoMenuDTO;
import com.example.bechef.dto.MenuInfoPageDTO;
import com.example.bechef.dto.ReviewDTO;
import com.example.bechef.model.favorite.Favorite;
import com.example.bechef.model.inventory.Inventory;
import com.example.bechef.model.member.Member;
import com.example.bechef.model.menu.Menu;
import com.example.bechef.model.menuIngredient.MenuIngredient;
import com.example.bechef.model.review.Review;
import com.example.bechef.model.store.Store;
import com.example.bechef.model.storeHours.StoreHours;
import com.example.bechef.model.storeImage.StoreImage;
import com.example.bechef.service.favorite.FavoriteService;
import com.example.bechef.service.inventory.InventoryService;
import com.example.bechef.service.member.MemberService;
import com.example.bechef.service.menu.MenuService;
import com.example.bechef.service.menuIngredient.MenuIngredientService;
import com.example.bechef.service.review.ReviewService;
import com.example.bechef.service.store.StoreService;
import com.example.bechef.service.storeHour.StoreHoursService;
import com.example.bechef.service.storeImage.StoreImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/info")
public class InfoController {
    @Autowired
    StoreHoursService storeHoursService;

    @Autowired
    MenuService menuService;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    MenuIngredientService menuIngredientService;

    @Autowired
    StoreService storeService;

    @Autowired
    StoreImageService storeImageService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    MemberService memberService;

    @Autowired
    FavoriteService favoriteService;

    // 메뉴의 영업시간을 불러오는 메서드
    @GetMapping("/time/{storeId}")
    public ResponseEntity<?> getStoreHours(@PathVariable int storeId) {
        try {
            List<StoreHours> storeHours = storeHoursService.findByStoreId(storeId); // storeId에 해당하는 영업시간 조회
            return ResponseEntity.ok(storeHours); // 조회한 영업시간 반환
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("영업시간을 정상적으로 불러오지 못했습니다."); // 에러 메시지 반환
        }
    }

    // info 페이지에서 메뉴 정보를 불러오는 메서드
    @GetMapping("/info_menu/{storeId}")
    public ResponseEntity<?> getMenuInfo(@PathVariable int storeId) {
        List<Menu> menuList = menuService.getMenuInfoByStoreId(storeId); // storeId를 기반으로 메뉴 리스트 조회
        List<Inventory> inventoryList = inventoryService.getInventoryInfoByStoreId(storeId); // storeId를 기반으로 인벤토리 리스트 조회
        List<Integer> menuIds = menuList.stream()
                .map(Menu::getMenuId)
                .toList(); // 가져온 메뉴 리스트의 menuId 추출

        List<MenuIngredient> menuIngredientList = menuIngredientService.getMenuIngredientInfoByMenuId(menuIds); // menuId를 기준으로 재료 조회
        List<MenuInfoMenuDTO> menuInfoMenuDTOList = menuService.getMenuInfo(menuList, inventoryList, menuIngredientList); // DTO 생성

        return ResponseEntity.ok(menuInfoMenuDTOList); // DTO 반환
    }

    // infoPageBox 페이지 정보를 불러오는 메서드
    @GetMapping("/info_page/{storeId}")
    public ResponseEntity<?> getInfoPage(@PathVariable int storeId) {
        Store stores = storeService.infoPageByStoreId(storeId); // storeId에 해당하는 가게 정보 조회

        MenuInfoPageDTO dto = new MenuInfoPageDTO(); // DTO 생성
        dto.setStore_id(stores.getStoreId());
        dto.setStore_name(stores.getStoreName());
        dto.setStore_phone(stores.getStorePhone());
        dto.setAverageRating(stores.getStoreRating());
        dto.setStore_address(stores.getStoreAddress());

        StoreImage storeImage = storeImageService.storeImgByStoreId(storeId); // storeId에 해당하는 가게 이미지 조회
        dto.setStore_image_url(storeImage.getImageUrl());

        System.out.println("dto????" + dto + "dto????"); // dto의 정보를 확인하기 위해 출력

        return ResponseEntity.ok(dto); // DTO 반환
    }

    // infoPage에서 리뷰 정보를 불러오는 메서드
    @GetMapping("/info_review/{storeId}")
    public ResponseEntity<?> getInfoReview(@PathVariable int storeId) {
        List<Review> reviews = reviewService.findReviewByStoreId(storeId); // storeId에 해당하는 리뷰 조회

        Set<Integer> memberIdx = reviews.stream()
                .map(Review::getMemberIdx)
                .collect(Collectors.toSet()); // 모든 리뷰의 member_idx 추출

        List<Member> members = memberService.getMemberNameByIdx(new ArrayList<>(memberIdx)); // 한 번에 모든 관련 회원 정보 조회

        Map<Integer, String> memberNameMap = members.stream()
                .collect(Collectors.toMap(Member::getIdx, Member::getName)); // 회원 정보를 Map에 저장

        List<ReviewDTO> reviewDTOS = reviews.stream()
                .map(review -> {
                    ReviewDTO dto = new ReviewDTO();
                    dto.setReviewId(review.getReviewId());
                    dto.setReviewRating(review.getReviewRating());
                    dto.setComment(review.getComment());
                    dto.setReviewDate(review.getReviewDate());
                    dto.setMemberIdx(review.getMemberIdx());
                    dto.setUserName(memberNameMap.getOrDefault(review.getMemberIdx(), "Unknown"));

                    return dto;
                })
                .collect(Collectors.toList()); // ReviewDTO 생성 및 회원 이름 설정

        System.out.println("reviewDTOS" + reviewDTOS + "reviewDTOS"); // reviewDTO 리스트 정보를 확인하기 위해 출력

        return ResponseEntity.ok(reviewDTOS); // DTO 반환
    }

    // infoPage에서 가게의 별점 평균값을 가져오는 메서드
    @GetMapping("/average_rating/{storeId}")
    public ResponseEntity<?> getRating(@PathVariable int storeId) {
        Store storeByStar = storeService.infoPageByStoreId(storeId); // 해당 storeId로 가게 정보 조회
        List<BigDecimal> allRatings = reviewService.getAllRatingsByStoreId(storeByStar.getStoreId()); // 해당 storeId로 모든 별점 조회
        System.out.println("All ratings for store " + storeByStar.getStoreId() + ": " + allRatings); // 모든 별점 리스트를 확인하기 위해 출력

        BigDecimal averageRating = calculateAverageRating(allRatings); // 모든 별점으로 평균 계산
        System.out.println("Calculated average rating: " + averageRating); // 계산된 평균 별점을 확인하기 위해 출력

        return ResponseEntity.ok(storeByStar.getStoreRating()); // 평균 별점 반환
    }

    // infoPage에서 찜 정보를 불러오는 메서드
    @GetMapping("/favorites/{storeId}/{memberIdx}")
    public ResponseEntity<?> getFavorite(@PathVariable int storeId, @PathVariable int memberIdx) {
        Favorite favorite = favoriteService.getFavoriteById(storeId, memberIdx); // storeId와 memberIdx로 찜 정보 조회
        System.out.println("storeIdstoreId" + storeId); // storeId 확인을 위해 출력
        System.out.println("memberIdxmemberIdx" + memberIdx); // memberIdx 확인을 위해 출력
        System.out.println("favoritefavorite" + favorite); // favorite 정보 확인을 위해 출력

        return ResponseEntity.ok(favorite); // 찜 정보 반환
    }

    // infoPage에서 찜 정보를 업데이트 또는 생성하는 메서드 (토글 기능)
    @PostMapping("/favorites")
    public ResponseEntity<?> updateFavorite(@RequestBody Favorite request) {
        Favorite updateFavorite = favoriteService.updateOrCreateFavorite(
                request.getMemberIdx(),
                request.getStoreId(),
                request.isFavorite()
        );
        return ResponseEntity.ok(updateFavorite); // 업데이트된 찜 정보 반환
    }

    // 리뷰를 등록하는 메서드
    @PostMapping("/review_input")
    public ResponseEntity<?> createReview(@RequestBody Review request) {
        System.out.println("Received review request: " + request); // 요청된 리뷰 정보를 확인하기 위해 출력

        try {
            Review createdReview = reviewService.createReview(
                    request.getMemberIdx(),
                    request.getStoreId(),
                    request.getComment(),
                    request.getReviewRating()
            ); // 리뷰 생성
            System.out.println("Created review: " + createdReview); // 생성된 리뷰 정보를 확인하기 위해 출력

            Store store = storeService.infoPageByStoreId(createdReview.getStoreId()); // 스토어 객체 가져오기
            if (store == null) {
                throw new RuntimeException("Store not found with id: " + createdReview.getStoreId());
            }
            System.out.println("Retrieved store: " + store); // 조회된 스토어 정보를 확인하기 위해 출력

            List<BigDecimal> allRatings = reviewService.getAllRatingsByStoreId(createdReview.getStoreId()); // 해당 storeId로 모든 별점 조회
            System.out.println("All ratings for store " + createdReview.getStoreId() + ": " + allRatings); // 모든 별점 리스트를 확인하기 위해 출력



            BigDecimal averageRating = calculateAverageRating(allRatings); // 모든 별점으로 평균 계산
            System.out.println("Calculated average rating: " + averageRating); // 계산된 평균 별점을 확인하기 위해 출력

            store.setStoreRating(averageRating); // 스토어 평균 별점 업데이트
            System.out.println("store.setStoreRating(averageRating);"+store.getStoreRating()+" store.setStoreRating(averageRating);"); // 업데이트된 평균 별점을 확인하기 위해 출력
            Store updatedStore = storeService.updateStore(store); // 스토어 업데이트

            System.out.println("Updated store: " + updatedStore); // 업데이트된 스토어 정보를 확인하기 위해 출력
            return ResponseEntity.ok(updatedStore); // 업데이트된 스토어 반환

        } catch (Exception e) {
            System.out.println("Error creating review: " + e.getMessage()); // 리뷰 생성 중 발생한 오류 메시지 출력
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating review"); // 에러 메시지 반환
        }
    }

    // 리뷰를 수정하는 메서드
    @PutMapping("/review_update/{reviewId}")
    public ResponseEntity<?> updateReview(@RequestBody Review request, @PathVariable int reviewId) {
        System.out.println("  - Review ID: " + reviewId + ", Request: " + request); // 요청된 리뷰 ID와 정보를 확인하기 위해 출력

        try {
            Review updatedReview = reviewService.updateReview(
                    reviewId,
                    request.getComment(),
                    request.getReviewRating()
            ); // 리뷰 업데이트
            System.out.println("Updated review: " + updatedReview); // 업데이트된 리뷰 정보를 확인하기 위해 출력

            Store store = storeService.infoPageByStoreId(updatedReview.getStoreId()); // 스토어 객체 가져오기
            if (store == null) {
                throw new RuntimeException("Store not found with id: " + updatedReview.getStoreId());
            }
            System.out.println("Retrieved store: " + store); // 조회된 스토어 정보를 확인하기 위해 출력

            List<BigDecimal> allRatings = reviewService.getAllRatingsByStoreId(updatedReview.getStoreId()); // 해당 storeId로 모든 별점 조회
            System.out.println("All ratings for store " + updatedReview.getStoreId() + ": " + allRatings); // 모든 별점 리스트를 확인하기 위해 출력

            BigDecimal averageRating = calculateAverageRating(allRatings); // 모든 별점으로 평균 계산
            System.out.println("Calculated average rating: " + averageRating); // 계산된 평균 별점을 확인하기 위해 출력

            store.setStoreRating(averageRating); // 스토어 평균 별점 업데이트
            Store updatedStore = storeService.updateStore(store); // 스토어 업데이트
            System.out.println("Updated store: " + updatedStore); // 업데이트된 스토어 정보를 확인하기 위해 출력

            return ResponseEntity.ok(updatedStore); // 업데이트된 스토어 반환
        } catch (Exception e) {
            System.out.println("Error updating review: " + e.getMessage()); // 리뷰 수정 중 발생한 오류 메시지 출력
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating review"); // 에러 메시지 반환
        }
    }

    // 리뷰를 삭제하는 메서드
    @DeleteMapping("/review_delete/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable int reviewId) {
        System.out.println("deleteReview - Review ID: " + reviewId); // 삭제할 리뷰 ID를 확인하기 위해 출력

        try {
            Review reviewToDelete = reviewService.getReviewById(reviewId); // 리뷰 삭제 전에 해당 리뷰의 storeId를 가져옴
            int storeId = reviewToDelete.getStoreId();
            System.out.println("Review to delete: " + reviewToDelete); // 삭제할 리뷰 정보를 확인하기 위해 출력

            reviewService.deleteReview(reviewId); // 리뷰 삭제
            System.out.println("Review deleted successfully"); // 리뷰 삭제 성공 메시지 출력

            Store store = storeService.infoPageByStoreId(storeId); // 스토어 객체 가져오기
            if (store == null) {
                throw new RuntimeException("Store not found with id: " + storeId);
            }
            System.out.println("Retrieved store: " + store); // 조회된 스토어 정보를 확인하기 위해 출력

            List<BigDecimal> allRatings = reviewService.getAllRatingsByStoreId(storeId); // 해당 storeId로 남아있는 모든 별점 조회
            System.out.println("Remaining ratings for store " + storeId + ": " + allRatings); // 남아있는 별점 리스트를 확인하기 위해 출력

            BigDecimal averageRating = calculateAverageRating(allRatings); // 남아있는 별점으로 평균 계산
            System.out.println("Calculated average rating: " + averageRating); // 계산된 평균 별점을 확인하기 위해 출력

            store.setStoreRating(averageRating); // 스토어 평균 별점 업데이트
            Store updatedStore = storeService.updateStore(store); // 스토어 업데이트
            System.out.println("Updated store: " + updatedStore); // 업데이트된 스토어 정보를 확인하기 위해 출력

            return ResponseEntity.ok("리뷰가 성공적으로 삭제되었고, 스토어 평균 별점이 업데이트되었습니다."); // 성공 메시지 반환

        } catch (RuntimeException e) {
            System.out.println("Runtime error in deleteReview: " + e.getMessage()); // 런타임 오류 메시지 출력
            e.printStackTrace();
            if (e.getMessage().startsWith("Review not found")) {
                return ResponseEntity.notFound().build(); // 리뷰를 찾을 수 없는 경우 404 응답
            } else if (e.getMessage().startsWith("Store not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 스토어를 찾을 수 없습니다."); // 스토어를 찾을 수 없는 경우 404 응답
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 삭제 중 오류가 발생했습니다."); // 기타 오류 경우 500 응답
            }
        } catch (Exception e) {
            System.out.println("Unexpected error in deleteReview: " + e.getMessage()); // 예상치 못한 오류 메시지 출력
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다."); // 예상치 못한 오류 경우 500 응답
        }
    }

    // 별점 평균 계산 메서드
    private BigDecimal calculateAverageRating(List<BigDecimal> ratings) {
        if (ratings.isEmpty()) {
            System.out.println("No ratings found, returning ZERO"); // 별점이 없는 경우 메시지 출력
            return BigDecimal.ZERO; // 별점이 없는 경우 0 반환
        }
        BigDecimal sum = ratings.stream().reduce(BigDecimal.ZERO, BigDecimal::add); // 모든 별점 합산
        System.out.println("Sum of ratings: " + sum); // 합산된 별점 확인을 위해 출력
        System.out.println("Number of ratings: " + ratings.size()); // 별점의 개수를 확인하기 위해 출력
        BigDecimal average = sum.divide(BigDecimal.valueOf(ratings.size()), 1, RoundingMode.HALF_UP); // 별점 평균 계산
        System.out.println("Calculated average: " + average); // 계산된 평균 별점을 확인하기 위해 출력
        return average;
    }
}