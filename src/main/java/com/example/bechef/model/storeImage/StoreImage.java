package com.example.bechef.model.storeImage;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "store_images")
@AllArgsConstructor
@NoArgsConstructor
public class StoreImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private int imageId;

    @Column(name = "store_id")
    private int storeId;

    @Column(name = "image_url")
    private String imageUrl;
}
