package com.example.bechef.repository.menuIngredient;

import com.example.bechef.model.menu.Menu;
import com.example.bechef.model.menuIngredient.MenuIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuIngredientRepository extends JpaRepository<MenuIngredient, List> {
    List<MenuIngredient> findByMenuIdIn(List<Integer> menuId) ;
}
