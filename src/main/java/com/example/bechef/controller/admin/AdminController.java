package com.example.bechef.controller.admin;

import com.example.bechef.dto.InventoryMenuDTO;
import com.example.bechef.dto.MemberDTO;
import com.example.bechef.dto.MenuDTO;
import com.example.bechef.model.inventory.Inventory;
import com.example.bechef.model.inventory.QuantityUpdateRequest;
import com.example.bechef.model.menu.MenuIds;
import com.example.bechef.model.store.Store;
import com.example.bechef.service.inventory.InventoryService;
import com.example.bechef.service.member.MemberService;
import com.example.bechef.service.menu.MenuService;
import com.example.bechef.service.menuIngredient.MenuIngredientService;
import com.example.bechef.service.store.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuIngredientService menuIngredientService;

    @Autowired
    private InventoryService inventoryService;

    // 관리자 페이지에서 관리자를 제외한 모든 멤버 데이터를 가져옴
    @PreAuthorize("hasRole('ADMIN')") // 관리자 권한 확인
    @GetMapping("/members")
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        logger.info("Entering getAllMembers"); // 메서드 진입 로그
        try {
            List<MemberDTO> members = memberService.findAll(); // 모든 멤버 조회
            logger.info("Retrieved {} members", members.size()); // 조회된 멤버 수 로그 기록
            return ResponseEntity.ok(members); // 멤버 목록 반환
        } catch (Exception e) {
            logger.error("Error retrieving members", e); // 에러 로그 기록
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 서버 에러 응답
        }
    }

    // 멤버 삭제
    @DeleteMapping("/members/{member_idx}")
    // 매개변수로 가져온 member_idx를 int 타입의 member_idx 객체에 담음
    public ResponseEntity<?> deleteMember(@PathVariable int member_idx) {
        logger.info("Attempting to delete member with ID: {}", member_idx); // 삭제하려는 member_idx 확인하는 로그
        try {
            memberService.delete(member_idx); // 해당 idx를 가진 멤버 삭제
            logger.info("Successfully deleted member with ID: {}", member_idx); // 삭제 성공 로그 기록
            return ResponseEntity.ok("성공적으로 삭제됨"); // 성공 메시지 반환
        } catch (Exception e) {
            logger.error("Error deleting member with ID: " + member_idx, e); // 에러 로그 기록
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("삭제 중 오류 발생: " + e.getMessage()); // 에러 메시지 반환
        }
    }

    // 상품 등록에서 드롭박스를 위한 모든 가게 조회
    @GetMapping("/stores")
    public ResponseEntity<List<Store>> getAllStores() {
        logger.info("Entering getAllStores"); // 메서드 진입 로그
        try {
            List<Store> stores = storeService.findAll(); // 모든 가게 조회
            logger.info("Retrieved {} stores", stores.size()); // 조회된 가게 수 로그 기록
            return ResponseEntity.ok(stores); // 가게 목록 반환
        } catch (Exception e) {
            logger.error("Error retrieving stores", e); // 에러 로그 기록
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 서버 에러 응답
        }
    }

    // 입력받은 상품을 DB에 저장
    @PostMapping("/menu")
    public ResponseEntity<?> createMenu(@RequestBody MenuDTO menuDTO) {
        logger.info("Received menu data: {}", menuDTO); // 입력받은 메뉴 데이터 로그 기록
        try {
            MenuIds ids = menuService.saveMenuAndGetIds(menuDTO); // 메뉴 저장 및 ID 반환
            int menuId = ids.getMenuId(); // 메뉴 ID 추출
            int storeId = ids.getStoreId(); // 가게 ID 추출
            logger.info("Saved menu with ID: {}, for store ID: {}", menuId, storeId); // 저장된 메뉴 로그 기록

            String ingredients = menuDTO.getMenuIngredients(); // 메뉴 재료 추출
            menuIngredientService.addIngredients(menuId, ingredients); // 재료 추가
            logger.info("Added ingredients for menu ID: {}", menuId); // 재료 추가 로그 기록

            inventoryService.addInventory(menuId, storeId, menuDTO.getQuantity()); // 재고 추가
            logger.info("Added inventory for menu ID: {}, store ID: {}, quantity: {}",
                    menuId, storeId, menuDTO.getQuantity()); // 재고 추가 로그 기록

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("메뉴가 성공적으로 등록되었습니다."); // 성공 메시지 반환
        } catch (Exception e) {
            logger.error("Error creating menu", e); // 에러 로그 기록
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("메뉴 등록 중 오류가 발생했습니다: " + e.getMessage()); // 에러 메시지 반환
        }
    }

    // 가게 ID로 해당 가게의 재고 정보를 조회
    @GetMapping("/inventory/{storeId}")
    public ResponseEntity<List<InventoryMenuDTO>> getInventoryByStoreId(@PathVariable int storeId) {
        try {
            List<InventoryMenuDTO> inventoryMenu = inventoryService.findInventoryMenuByStoreId(storeId); // 재고 조회
            logger.info("Retrieved inventory for store ID: {}", storeId); // 조회된 재고 로그 기록
            return ResponseEntity.ok(inventoryMenu); // 재고 목록 반환
        } catch (Exception e) {
            logger.error("Error fetching inventory for store ID: " + storeId, e); // 에러 로그 기록
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList()); // 에러 응답
        }
    }

    // 밀키트의 수량 업데이트
    @PutMapping("/inventory/{storeId}/{menuId}")
    public ResponseEntity<?> updateInventoryByQuantity(
            @PathVariable int storeId, // 매개변수로 가져온 storeId를 int 타입의 storeId 객체에 담음
            @PathVariable int menuId, // 매개변수로 가져온 menuId를 int 타입의 menuId 객체에 담음
            @RequestBody QuantityUpdateRequest request) { // 매개변수로 가져온 request를 QuantityUpdateRequest 객체에 담음
        try {
            System.out.println("storeId: " + storeId + ", menuId: " + menuId + ", quantity: " + request.getQuantity()); // 수량 업데이트 정보 출력
            Inventory updatedInventory = inventoryService.updateInventoryQuantity(menuId, storeId, request.getQuantity()); // 재고 수량 업데이트
            return ResponseEntity.ok(updatedInventory); // 업데이트된 재고 반환
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 찾을 수 없음 에러 응답
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("수량 업데이트 중 오류 발생: " + e.getMessage()); // 서버 에러 응답
        }
    }
}
