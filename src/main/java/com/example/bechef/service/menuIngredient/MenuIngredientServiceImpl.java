package com.example.bechef.service.menuIngredient;

import com.example.bechef.model.menuIngredient.MenuIngredient;
import com.example.bechef.repository.menuIngredient.MenuIngredientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuIngredientServiceImpl implements MenuIngredientService{

    @Autowired
    private MenuIngredientRepository menuIngredientRepository;

    @Transactional
    @Override
    public void addIngredients(int menuId, String ingredients) {
        String[] ingredientArray = ingredients.split(",");

        for (String ingredient : ingredientArray) {
            String trimmedIngredient = ingredient.trim();
            MenuIngredient menuIngredient = new MenuIngredient();
            menuIngredient.setMenuId(menuId);
            menuIngredient.setIngredient(trimmedIngredient);
            menuIngredientRepository.save(menuIngredient);
        }
    }

    @Override
    public List<MenuIngredient> getMenuIngredientInfoByMenuId(List<Integer> menuId) {
            return menuIngredientRepository.findByMenuIdIn(menuId);
    }
}
