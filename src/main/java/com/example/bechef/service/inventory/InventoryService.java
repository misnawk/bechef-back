package com.example.bechef.service.inventory;

import com.example.bechef.dto.InventoryMenuDTO;
import com.example.bechef.model.inventory.Inventory;

import java.util.List;

public interface InventoryService {
    //관리자 페이지 수량 업데이트
    void addInventory(int menuId, int storeId, int quantity);

    //해당 가게에 맞는 밀키트 불러오기위한 메서드(관리자페이지)
    List<Inventory> findByStoreId(int storeId);

    //해당 가게에 맞는 메뉴들을 불러오기 위한 메서드(관리자)
    List<InventoryMenuDTO> findInventoryMenuByStoreId(int storeId);

    //밀키트의 수량을 업데이트하기위한 메서드
    Inventory updateInventoryQuantity(int menuId, int storeId, int quantity);


    //info 페이지 정보 가져오기
    List<Inventory> getInventoryInfoByStoreId(int storeId);
}
