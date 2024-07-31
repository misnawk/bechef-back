package com.example.bechef.model.menuIngredient;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menu_ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(MenuIngredientId.class)
public class MenuIngredient {
    @Id
    @Column(name = "menu_id")
    private int menuId;

    @Id
    @Column(name = "ingredient")
    private String ingredient;
}
