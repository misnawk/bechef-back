package com.example.bechef.model.menu;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name="menus")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="menu_id")
    private int menuId;

    @Column(name="store_id")
    private int storeId;

    @Column(name="menu_name")
    private String menuName;

    @Column(name="menu_description")
    private String menuDescription;

    @Column(name="menu_price")
    private BigDecimal menuPrice;

    @Column(name="menu_image_url")
    private String menuImageUrl;

    @Column(name="menu_cooking_time")
    private int menuCookingTime;

    @Column(name="menu_difficulty")
    @Enumerated(EnumType.STRING)
    private MenuRole menuDifficulty;

    @Column(name="menu_calories")
    private int menuCalories;



}
