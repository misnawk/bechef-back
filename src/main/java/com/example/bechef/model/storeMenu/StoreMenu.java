package com.example.bechef.model.storeMenu;

import com.example.bechef.model.menu.Menu;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "store_menus")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "store_id")
    private Integer storeId;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
}
