package com.example.bechef.service.inventory;

import com.example.bechef.dto.InventoryMenuDTO;
import com.example.bechef.model.inventory.Inventory;
import com.example.bechef.repository.inventory.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private final InventoryRepository inventoryRepository; // InventoryRepository 주입

    // 재고 추가 메서드
    @Override
    public void addInventory(int menuId, int storeId, int quantity) {
        System.out.println("Adding inventory: menuId=" + menuId + ", storeId=" + storeId + ", quantity=" + quantity); // 로그 출력
        Inventory inventory = new Inventory(); // 새로운 Inventory 객체 생성
        inventory.setMenuId(menuId); // 메뉴 ID 설정
        inventory.setStoreId(storeId); // 가게 ID 설정
        inventory.setQuantity(quantity); // 수량 설정
        inventory.setLastUpdated(new Date()); // 마지막 업데이트 날짜 설정

        try {
            inventoryRepository.save(inventory); // 재고 저장
            System.out.println("Inventory saved successfully"); // 성공 메시지 출력
        } catch (Exception e) {
            System.err.println("Error saving inventory: " + e.getMessage()); // 에러 메시지 출력
            e.printStackTrace();
        }
    }

    // 가게 ID로 재고 찾기 메서드
    @Override
    public List<Inventory> findByStoreId(int storeId) {
        List<Inventory> inventories = inventoryRepository.findByStoreId(storeId); // 가게 ID로 재고 목록 조회
        return inventories.stream()
                .map(this::convertToDTO) // Inventory 객체를 DTO로 변환
                .collect(Collectors.toList()); // List로 반환
    }

    // Inventory 객체를 DTO로 변환하는 메서드
    private Inventory convertToDTO(Inventory inventory) {
        Inventory dto = new Inventory(); // 새로운 Inventory DTO 객체 생성
        dto.setMenuId(inventory.getMenuId()); // 메뉴 ID 설정
        dto.setStoreId(inventory.getStoreId()); // 가게 ID 설정
        dto.setQuantity(inventory.getQuantity()); // 수량 설정
        return dto;
    }

    // 가게 ID로 메뉴 목록 찾기 메서드
    @Override
    public List<InventoryMenuDTO> findInventoryMenuByStoreId(int storeId) {
        return inventoryRepository.findInventoryMenuByStoreId(storeId); // 가게 ID로 InventoryMenuDTO 목록 조회
    }

    // 재고 수량 업데이트 메서드
    @Override
    public Inventory updateInventoryQuantity(int menuId, int storeId, int quantity) {
        System.out.println(">>>><<<<"+menuId+"/"+storeId+"/"+quantity); // 로그 출력
        Inventory inventory = inventoryRepository.findByMenuIdAndStoreId(menuId, storeId)
                .orElseThrow(() -> new RuntimeException("해당 재고를 찾을 수 없습니다.")); // 메뉴 ID와 가게 ID로 재고 조회, 없으면 예외 발생

        inventory.setQuantity(quantity); // 수량 업데이트
        inventory.setLastUpdated(new Date()); // 마지막 업데이트 날짜 설정

        return inventoryRepository.save(inventory); // 업데이트된 재고 저장
    }

    // 가게 ID로 재고 정보 찾기 메서드
    @Override
    public List<Inventory> getInventoryInfoByStoreId(int storeId) {
        return inventoryRepository.getMenuInfoByStoreId(storeId); // 가게 ID로 재고 정보 목록 조회
    }
}
