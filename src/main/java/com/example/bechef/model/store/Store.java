package com.example.bechef.model.store;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private int storeId;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "store_phone")
    private String storePhone;

    @Column(name = "store_rating")
    private BigDecimal storeRating;

    @Column(name = "store_latitude")
    private BigDecimal storeLatitude;

    @Column(name = "store_longitude")
    private BigDecimal storeLongitude;

    @Column(name = "store_address")
    private String storeAddress;
}