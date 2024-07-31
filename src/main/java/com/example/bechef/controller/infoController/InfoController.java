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


    //메뉴의 시간을 불러오는곳
    @GetMapping("/time/{storeId}")
    public ResponseEntity<?> getStoreHours(@PathVariable int storeId) {

        try {
            List<StoreHours> storeHours = storeHoursService.findByStoreId(storeId);
            return ResponseEntity.ok(storeHours);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("영업시간을 정상적으로 불러오지 못했습니다.");
        }
    }


    //info 페이지 메뉴 불러오는곳
    @GetMapping("/info_menu/{storeId}")
    public ResponseEntity<?> getMenuInfo(@PathVariable int storeId) {

        // storeId를 기반으로 메뉴 리스트를 가져옴
        List<Menu> menuList = menuService.getMenuInfoByStoreId(storeId);

        // storeId를 기반으로 인벤토리 리스트를 가져옴
        List<Inventory> inventoryList = inventoryService.getInventoryInfoByStoreId(storeId);

        // 가져온 메뉴 리스트들의 menuId를 가져옴
        List<Integer> menuIds = menuList.stream()
                .map(Menu::getMenuId)
                .toList();

        // menuId 기준으로 재료 가져옴
        List<MenuIngredient> menuIngredientList = menuIngredientService.getMenuIngredientInfoByMenuId(menuIds);

        List<MenuInfoMenuDTO> menuInfoMenuDTOList = menuService.getMenuInfo(menuList, inventoryList, menuIngredientList);

        return ResponseEntity.ok(menuInfoMenuDTOList);
    }


    //infoPageBox 페이지
    @GetMapping("/info_page/{storeId}")
    public ResponseEntity<?> getInfoPage(@PathVariable int storeId) {
        Store stores = storeService.infoPageByStoreId(storeId);

        MenuInfoPageDTO dto = new MenuInfoPageDTO();

        dto.setStore_id(stores.getStoreId());
        dto.setStore_name(stores.getStoreName());
        dto.setStore_phone(stores.getStorePhone());
        dto.setAverageRating(stores.getStoreRating());
        dto.setStore_address(stores.getStoreAddress());

        StoreImage storeImage = storeImageService.storeImgByStoreId(storeId);
        dto.setStore_image_url(storeImage.getImageUrl());

        System.out.println("dto????" + dto + "dto????");

        return ResponseEntity.ok(dto);
    }

    //infoPage 리뷰
    @GetMapping("/info_review/{storeId}")
    public ResponseEntity<?> getInfoReview(@PathVariable int storeId) {
        List<Review> reviews = reviewService.findReviewByStoreId(storeId);

        //1. 모든 리뷰의 member_idx 반환
        Set<Integer> memberIdx = reviews.stream()
                .map(Review::getMemberIdx)
                .collect(Collectors.toSet());

        //2. 한 번에 모든 관련 회원 정보 조회
        List<Member> members = memberService.getMemberNameByIdx(new ArrayList<>(memberIdx));

        //3. 회원 정보를 Map 에 저장
        Map<Integer, String> memberNameMap = members.stream()
                .collect(Collectors.toMap(Member::getIdx, Member::getName));

        //4. ReviewDTO 생성 및 회원 이름 설정
        List<ReviewDTO> reviewDTOS = reviews.stream()
                .map(review -> {
                    ReviewDTO dto = new ReviewDTO();
                    dto.setReviewId(review.getReviewId());
                    dto.setReviewRating(review.getReviewRating());
                    dto.setComment(review.getComment());
                    dto.setReviewDate(review.getReviewDate());
                    dto.setMemberIdx(review.getMemberIdx());
                    dto.setUserName(memberNameMap.getOrDefault(review.getMemberIdx(), "Unknown"));

                    System.out.println("min1" + dto + "min1");

                    return dto;
                })
                .collect(Collectors.toList());


        System.out.println("reviewDTOS" + reviewDTOS + "reviewDTOS");

        return ResponseEntity.ok(reviewDTOS);
    }


    //infoPage store 테이블에서 별점 평균값 가져오기
    @GetMapping("/average_rating/{storeId}")
    public ResponseEntity<?> getRating(@PathVariable int storeId) {
        Store storeByStar = storeService.infoPageByStoreId(storeId);
        // 해당 storeId로 모든 별점 가져오기
        List<BigDecimal> allRatings = reviewService.getAllRatingsByStoreId(storeByStar.getStoreId());
        System.out.println("All ratings for store " + storeByStar.getStoreId() + ": " + allRatings);

        // 모든 별점으로 평균 계산
        BigDecimal averageRating = calculateAverageRating(allRatings);
        System.out.println("Calculated average rating: " + averageRating);


        return ResponseEntity.ok(storeByStar.getStoreRating());
    }

//    //infoPage 별점 평균 보내기(리뷰작성시 사용자가 입력한 별점 반영해서) store 테이블
//    @PostMapping("/update_store_rating/{storeId}")
//    public ResponseEntity<?> updateRating(@RequestBody Review request, @PathVariable int storeId) {
//
//        //스토어의 현재별점 가져오기
//        Store store = storeService.infoPageByStoreId(storeId);
//
//        //현재 해당 store 의 별점
//        BigDecimal storeStar = store.getStoreRating();
//        System.out.println("storeStar" + storeStar + "storeStar");
//
//        //사용자가 입력한 store 의 별점
//        BigDecimal userStar = request.getReviewRating();
//
//        // 두 값의 평균 계산
//        BigDecimal averageStar = storeStar.add(userStar)
//                .divide(BigDecimal.valueOf(2), 1, RoundingMode.HALF_UP);
//
//        //평균값 해당 가게에 업데이트하기
//
//        store.setStoreRating(averageStar);
//
//
//        // 업데이트된 Store 객체 저장
//        Store updatedStore = storeService.updateStore(store);
//
//
//        System.out.println("averageStar" + averageStar + "averageStar");
//        System.out.println("update_store_rating" + request.getReviewRating() + "update_store_rating");
//        System.out.println("update_store_storeId" + storeId + "update_store_storeId");
//
//        return ResponseEntity.ok(updatedStore);
//    }


    //infoPage 찜불러오기
    @GetMapping("/favorites/{storeId}/{memberIdx}")
    public ResponseEntity<?> getFavorite(@PathVariable int storeId, @PathVariable int memberIdx) {

        Favorite favorite = favoriteService.getFavoriteById(storeId, memberIdx);
        System.out.println("storeIdstoreId" + storeId);
        System.out.println("memberIdxmemberIdx" + memberIdx);
        System.out.println("favoritefavorite" + favorite);

        return ResponseEntity.ok(favorite);
    }

    //infoPage 찜보내기 및 삭제(토글)
    @PostMapping("/favorites")
    public ResponseEntity<?> updateFavorite(@RequestBody Favorite request) {

        Favorite updateFavorite = favoriteService.updateOrCreateFavorite(
                request.getMemberIdx(),
                request.getStoreId(),
                request.isFavorite()
        );
        return ResponseEntity.ok(updateFavorite);
    }

    //리뷰 등록
    @PostMapping("/review_input")
    public ResponseEntity<?> createReview(@RequestBody Review request) {
        System.out.println("Received review request: " + request);

        try {
            // 리뷰 생성
            Review createdReview = reviewService.createReview(
                    request.getMemberIdx(),
                    request.getStoreId(),
                    request.getComment(),
                    request.getReviewRating()
            );
            System.out.println("Created review: " + createdReview);

            // 스토어 객체 가져오기
            Store store = storeService.infoPageByStoreId(createdReview.getStoreId());
            if (store == null) {
                throw new RuntimeException("Store not found with id: " + createdReview.getStoreId());
            }
            System.out.println("Retrieved store: " + store);

            // 해당 storeId로 모든 별점 가져오기
            List<BigDecimal> allRatings = reviewService.getAllRatingsByStoreId(createdReview.getStoreId());
            System.out.println("All ratings for store " + createdReview.getStoreId() + ": " + allRatings);

            // 모든 별점으로 평균 계산
            BigDecimal averageRating = calculateAverageRating(allRatings);
            System.out.println("Calculated average rating: " + averageRating);

            // 스토어 평균 별점 업데이트
            store.setStoreRating(averageRating);
            System.out.println("store.setStoreRating(averageRating);"+store.getStoreRating()+" store.setStoreRating(averageRating);");
            Store updatedStore = storeService.updateStore(store);

            System.out.println("Updated store: " + updatedStore);
            return ResponseEntity.ok(updatedStore);

        } catch (Exception e) {
            System.out.println("Error creating review: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating review");
        }
    }

    //리뷰 수정
    @PutMapping("/review_update/{reviewId}")
    public ResponseEntity<?> updateReview(@RequestBody Review request, @PathVariable int reviewId) {
        System.out.println("  - Review ID: " + reviewId + ", Request: " + request);

        try {
            // 1. 수정한 해당 유저의 별점을 업데이트
            Review updatedReview = reviewService.updateReview(
                    reviewId,
                    request.getComment(),
                    request.getReviewRating()
            );
            System.out.println("Updated review: " + updatedReview);

            // 스토어 객체 가져오기
            Store store = storeService.infoPageByStoreId(updatedReview.getStoreId());
            if (store == null) {
                throw new RuntimeException("Store not found with id: " + updatedReview.getStoreId());
            }
            System.out.println("Retrieved store: " + store);

            // 2. 해당 storeId로 별점 모두 가져오기
            List<BigDecimal> allRatings = reviewService.getAllRatingsByStoreId(updatedReview.getStoreId());
            System.out.println("All ratings for store " + updatedReview.getStoreId() + ": " + allRatings);

            // 3. 모두 가져온 별점으로 평균 계산
            BigDecimal averageRating = calculateAverageRating(allRatings);
            System.out.println("Calculated average rating: " + averageRating);

            // 4. 스토어 평균 별점 업데이트
            store.setStoreRating(averageRating);
            Store updatedStore = storeService.updateStore(store);
            System.out.println("Updated store: " + updatedStore);

            return ResponseEntity.ok(updatedStore);
        } catch (Exception e) {
            System.out.println("Error updating review: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating review");
        }
    }

    //리뷰 삭제
    @DeleteMapping("/review_delete/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable int reviewId) {
        System.out.println("deleteReview - Review ID: " + reviewId);

        try {
            // 리뷰 삭제 전에 해당 리뷰의 storeId를 가져옵니다.
            Review reviewToDelete = reviewService.getReviewById(reviewId);
            int storeId = reviewToDelete.getStoreId();
            System.out.println("Review to delete: " + reviewToDelete);

            // 리뷰 삭제
            reviewService.deleteReview(reviewId);
            System.out.println("Review deleted successfully");

            // 스토어 객체 가져오기
            Store store = storeService.infoPageByStoreId(storeId);
            if (store == null) {
                throw new RuntimeException("Store not found with id: " + storeId);
            }
            System.out.println("Retrieved store: " + store);

            // 해당 storeId로 남아있는 모든 별점 가져오기
            List<BigDecimal> allRatings = reviewService.getAllRatingsByStoreId(storeId);
            System.out.println("Remaining ratings for store " + storeId + ": " + allRatings);

            // 남아있는 별점으로 평균 계산
            BigDecimal averageRating = calculateAverageRating(allRatings);
            System.out.println("Calculated average rating: " + averageRating);

            // 스토어 평균 별점 업데이트
            store.setStoreRating(averageRating);
            Store updatedStore = storeService.updateStore(store);
            System.out.println("Updated store: " + updatedStore);

            return ResponseEntity.ok("리뷰가 성공적으로 삭제되었고, 스토어 평균 별점이 업데이트되었습니다.");

        } catch (RuntimeException e) {
            System.out.println("Runtime error in deleteReview: " + e.getMessage());
            e.printStackTrace();
            if (e.getMessage().startsWith("Review not found")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().startsWith("Store not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 스토어를 찾을 수 없습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 삭제 중 오류가 발생했습니다.");
            }
        } catch (Exception e) {
            System.out.println("Unexpected error in deleteReview: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다.");
        }
    }

    private BigDecimal calculateAverageRating(List<BigDecimal> ratings) {
        if (ratings.isEmpty()) {
            System.out.println("No ratings found, returning ZERO");
            return BigDecimal.ZERO;
        }
        BigDecimal sum = ratings.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Sum of ratings: " + sum);
        System.out.println("Number of ratings: " + ratings.size());
        BigDecimal average = sum.divide(BigDecimal.valueOf(ratings.size()), 1, RoundingMode.HALF_UP);
        System.out.println("Calculated average: " + average);
        return average;
    }
}