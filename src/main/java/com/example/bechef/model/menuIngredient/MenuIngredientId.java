package com.example.bechef.model.menuIngredient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuIngredientId implements Serializable {
    private int menuId;
    private String ingredient;
}
