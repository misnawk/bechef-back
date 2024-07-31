package com.example.bechef.repository.inventory;

import com.example.bechef.dto.InventoryMenuDTO;
import com.example.bechef.model.inventory.Inventory;
import com.example.bechef.model.inventory.InventoryID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, InventoryID> {

    //해당 가게에 맞는 메뉴들을 불러오기 위한 메서드(관리자)
    @Query("SELECT new com.example.bechef.dto.InventoryMenuDTO(i.menuId, m.menuName, m.menuDescription, m.menuPrice, m.menuImageUrl, i.quantity, i.storeId, s.storeName) " +
            "FROM Inventory i JOIN Menu m ON i.menuId = m.menuId JOIN Store s ON i.storeId = s.storeId WHERE i.storeId = :storeId")
    List<InventoryMenuDTO> findInventoryMenuByStoreId(@Param("storeId") Integer storeId);

    //드롭박스
    List<Inventory> findByStoreId(int storeId);

    //메뉴등록
    Optional<Inventory> findByMenuIdAndStoreId(int menuId,int storeId);

    //info 페이지 정보 뽑기
    List<Inventory> getMenuInfoByStoreId(int storeId);
}