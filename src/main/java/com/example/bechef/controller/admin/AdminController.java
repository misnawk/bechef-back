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


    //관리자 페이지에 모든 멤버를 불러옴
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/members")
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        logger.info("Entering getAllMembers");
        try {
            List<MemberDTO> members = memberService.findAll();
            logger.info("Retrieved {} members", members.size());
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            logger.error("Error retrieving members", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //멤버 삭제
    @DeleteMapping("/members/{member_idx}")
    public ResponseEntity<?> deleteMember(@PathVariable int member_idx) {
        logger.info("Attempting to delete member with ID: {}", member_idx);
        try {
            memberService.delete(member_idx);
            logger.info("Successfully deleted member with ID: {}", member_idx);
            return ResponseEntity.ok("성공적으로 삭제됨");
        } catch (Exception e) {
            logger.error("Error deleting member with ID: " + member_idx, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("삭제중 오류발생: " + e.getMessage());
        }
    }

    //상품등록에서 드롭박스
    @GetMapping("/stores")
    public ResponseEntity<List<Store>> getAllStores() {
        logger.info("Entering getAllStores");
        try {
            List<Store> stores = storeService.findAll();
            logger.info("Retrieved {} stores", stores.size());
            return ResponseEntity.ok(stores);
        } catch (Exception e) {
            logger.error("Error retrieving stores", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //입력받은 상품 input DB로 넣기
    @PostMapping("/menu")
    public ResponseEntity<?> createMenu(@RequestBody MenuDTO menuDTO) {
        logger.info("Received menu data: {}", menuDTO);
        try {
            MenuIds ids = menuService.saveMenuAndGetIds(menuDTO);
            int menuId = ids.getMenuId();
            int storeId = ids.getStoreId();
            logger.info("Saved menu with ID: {}, for store ID: {}", menuId, storeId);

            String ingredients = menuDTO.getMenuIngredients();
            menuIngredientService.addIngredients(menuId, ingredients);
            logger.info("Added ingredients for menu ID: {}", menuId);

            inventoryService.addInventory(menuId, storeId, menuDTO.getQuantity());
            logger.info("Added inventory for menu ID: {}, store ID: {}, quantity: {}",
                    menuId, storeId, menuDTO.getQuantity());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("메뉴가 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            logger.error("Error creating menu", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("메뉴 등록 중 오류가 발생했습니다: " + e.getMessage());
        }
    }


    // 가게의 Id로 가게들의 정보를 가져옴
    @GetMapping("/inventory/{storeId}")
    public ResponseEntity<List<InventoryMenuDTO>> getInventoryByStoreId(@PathVariable int storeId) {
        try {
            List<InventoryMenuDTO> inventoryMenu = inventoryService.findInventoryMenuByStoreId(storeId);
            logger.info("Retrieved inventory for store ID: {}", storeId);
            return ResponseEntity.ok(inventoryMenu);
        } catch (Exception e) {
            logger.error("Error fetching inventory for store ID: " + storeId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    // 밀키트의 수량을 업데이트함
    @PutMapping("/inventory/{storeId}/{menuId}")
    public ResponseEntity<?> updateInventoryByQuantity(
            @PathVariable int storeId,
            @PathVariable int menuId,
            @RequestBody QuantityUpdateRequest request) {
        try {

            System.out.println("storeId: " + storeId + ", menuId: " + menuId + ", quantity: " + request.getQuantity());
            Inventory updatedInventory = inventoryService.updateInventoryQuantity(menuId, storeId, request.getQuantity());
            return ResponseEntity.ok(updatedInventory);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("수량 업데이트 중 오류 발생: " + e.getMessage());
        }
    }
}
