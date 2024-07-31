package com.example.bechef.model.inventory;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class InventoryID implements Serializable {
    private int menuId;
    private int storeId;

}
