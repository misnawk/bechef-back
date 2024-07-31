package com.example.bechef.service.menu;

import com.example.bechef.dto.MenuDTO;
import com.example.bechef.dto.MenuInfoMenuDTO;
import com.example.bechef.model.inventory.Inventory;
import com.example.bechef.model.menu.Menu;
import com.example.bechef.model.menu.MenuIds;
import com.example.bechef.model.menuIngredient.MenuIngredient;

import java.util.List;

public interface MenuService {
    //DTO -> Entity
    Menu convertToEntity(MenuDTO menuDTO);

    //변환한 Entity DB에 저장
    Menu saveMenu(MenuDTO menuDTO);

    //메뉴등록할때 저장하는 메서드
    MenuIds saveMenuAndGetIds(MenuDTO menuDTO);

    //info 메뉴 불러오기
    List<Menu> getMenuInfoByStoreId(int storeId);

    // 메뉴 정보를 DTO로 변환하는 메서드입니다.
    List<MenuInfoMenuDTO> getMenuInfo(List<Menu> menuList, List<Inventory> inventoryList, List<MenuIngredient> menuIngredientList);
}
