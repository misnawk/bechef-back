package com.example.bechef.service.menu;

import com.example.bechef.dto.MenuDTO;
import com.example.bechef.dto.MenuInfoMenuDTO;
import com.example.bechef.model.inventory.Inventory;
import com.example.bechef.model.menu.Menu;
import com.example.bechef.model.menu.MenuIds;
import com.example.bechef.model.menu.MenuRole;
import com.example.bechef.model.menuIngredient.MenuIngredient;
import com.example.bechef.repository.inventory.InventoryRepository;
import com.example.bechef.repository.menu.MenuRepository;
import com.example.bechef.repository.menuIngredient.MenuIngredientRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {
    private static final Logger log = LoggerFactory.getLogger(MenuServiceImpl.class);

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuIngredientRepository menuIngredientRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    // MenuDTO를 Menu 엔티티로 변환하는 메서드
    @Override
    public Menu convertToEntity(MenuDTO menuDTO) {
        Menu menu = new Menu();
        menu.setStoreId(menuDTO.getStoreId());
        menu.setMenuName(menuDTO.getMenuName());
        menu.setMenuDescription(menuDTO.getMenuDescription());
        menu.setMenuPrice(menuDTO.getMenuPrice());
        menu.setMenuImageUrl(menuDTO.getMenuImageUrl());
        menu.setMenuCookingTime(menuDTO.getMenuCookingTime());
        menu.setMenuDifficulty(MenuRole.valueOf(menuDTO.getMenuDifficulty()));
        menu.setMenuCalories(menuDTO.getMenuCalories());
        return menu;
    }

    // MenuDTO를 받아서 Menu 엔티티를 저장하는 메서드
    @Override
    @Transactional
    public Menu saveMenu(MenuDTO menuDTO) {
        Menu menu = convertToEntity(menuDTO);
        return menuRepository.save(menu);
    }

    // MenuDTO를 저장하고 저장된 Menu의 ID와 Store ID를 반환하는 메서드
    @Override
    @Transactional
    public MenuIds saveMenuAndGetIds(MenuDTO menuDTO) {
        Menu savedMenu = saveMenu(menuDTO);
        return new MenuIds(savedMenu.getMenuId(), savedMenu.getStoreId());
    }

    // 특정 가게의 메뉴 정보를 가져오는 메서드 (관리자용)
    @Override
    public List<Menu> getMenuInfoByStoreId(int storeId) {
        return menuRepository.findByStoreId(storeId);
    }

    // Menu 리스트와 Inventory 리스트, MenuIngredient 리스트를 받아서 MenuInfoMenuDTO로 변환하는 메서드
    @Override
    public List<MenuInfoMenuDTO> getMenuInfo(List<Menu> menuList, List<Inventory> inventoryList, List<MenuIngredient> menuIngredientList) {

        // 각 메뉴를 MenuInfoDTO로 변환
        return menuList.stream().map(menu -> {
            MenuInfoMenuDTO dto = new MenuInfoMenuDTO();

            // Menu 엔티티의 정보를 DTO에 설정
            dto.setMenuId(menu.getMenuId());
            dto.setKitName(menu.getMenuName());
            dto.setImageUrl(menu.getMenuImageUrl());
            dto.setCookingTime(menu.getMenuCookingTime());
            dto.setDifficulty(String.valueOf(menu.getMenuDifficulty()));
            dto.setCalories(menu.getMenuCalories());
            dto.setDescription(menu.getMenuDescription());

            // 각 메뉴에 해당하는 수량을 가져오기
            int targetMenuId = dto.getMenuId(); // 또는 필요한 메뉴 ID
            int quantity = inventoryList.stream()
                    .filter(inventory -> inventory.getMenuId() == targetMenuId) // 수정된 부분
                    .findFirst()
                    .map(Inventory::getQuantity)
                    .orElse(0); // 기본값 설정, 필요에 따라 변경 가능
            dto.setKitCount(quantity);

            // 각 메뉴에 해당하는 재료들을 가져오기
            List<String> ingredients = menuIngredientList.stream()
                    .filter(menuIngredient -> menuIngredient.getMenuId() == targetMenuId)
                    .map(MenuIngredient::getIngredient)
                    .collect(Collectors.toList());

            // 재료 목록을 쉼표로 구분된 문자열로 변환
            String ingredientString = String.join(", ", ingredients);

            dto.setKitIngredient(ingredientString);
            return dto;
        }).collect(Collectors.toList());
    }
}