package com.example.bechef.model.inventory;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @Column(name = "menu_id")
    private int menuId;

    @Column(name = "store_id")
    private int storeId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "last_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
}
