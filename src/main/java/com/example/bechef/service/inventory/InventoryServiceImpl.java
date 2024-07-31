package com.example.bechef.service.inventory;

import com.example.bechef.dto.InventoryMenuDTO;
import com.example.bechef.model.inventory.Inventory;
import com.example.bechef.model.inventory.InventoryID;
import com.example.bechef.repository.inventory.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private final InventoryRepository inventoryRepository;


    @Override
    public void addInventory(int menuId, int storeId, int quantity) {
        System.out.println("Adding inventory: menuId=" + menuId + ", storeId=" + storeId + ", quantity=" + quantity);
        Inventory inventory = new Inventory();
        inventory.setMenuId(menuId);
        inventory.setStoreId(storeId);
        inventory.setQuantity(quantity);
        inventory.setLastUpdated(new Date());

        try {
            inventoryRepository.save(inventory);
            System.out.println("Inventory saved successfully");
        } catch (Exception e) {
            System.err.println("Error saving inventory: " + e.getMessage());
            e.printStackTrace();
        }
    }



    @Override
    public List<Inventory> findByStoreId(int storeId) {
        List<Inventory> inventories = inventoryRepository.findByStoreId(storeId);
        return inventories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    private Inventory convertToDTO(Inventory inventory) {
        Inventory dto = new Inventory();
        dto.setMenuId(inventory.getMenuId());
        dto.setStoreId(inventory.getStoreId());
        dto.setQuantity(inventory.getQuantity());
        return dto;
    }

    //해당 가게에 맞는 메뉴들을 불러오기 위한 메서드(관리자)
    @Override
    public List<InventoryMenuDTO> findInventoryMenuByStoreId(int storeId) {
        return inventoryRepository.findInventoryMenuByStoreId(storeId);
    }



    //관리자 페이지 수량업데이트
    @Override
    public Inventory updateInventoryQuantity(int menuId, int storeId, int quantity) {
        System.out.println(">>>><<<<"+menuId+"/"+storeId+"/"+quantity);
        Inventory inventory = inventoryRepository.findByMenuIdAndStoreId(menuId, storeId)
                .orElseThrow(() -> new RuntimeException("해당 재고를 찾을 수 없습니다."));

        inventory.setQuantity(quantity);
        inventory.setLastUpdated(new Date());

        return inventoryRepository.save(inventory);
    }

    @Override
    public List<Inventory> getInventoryInfoByStoreId(int storeId) {
        return inventoryRepository.getMenuInfoByStoreId(storeId);
    }


}
