package com.example.bechef.service.menuIngredient;

import com.example.bechef.model.menuIngredient.MenuIngredient;

import java.util.List;

public interface MenuIngredientService {

    // 주어진 메뉴 ID와 재료 목록을 사용하여 재료를 추가하는 메서드.
    void addIngredients(int menuId, String ingredients);

    // 주어진 메뉴 ID 목록에 해당하는 모든 재료 정보를 가져오는 메서드.
    List<MenuIngredient> getMenuIngredientInfoByMenuId(List<Integer> menuId);
}
