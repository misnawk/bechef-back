package com.example.bechef.model.favorite;

import com.example.bechef.model.member.Member;
import com.example.bechef.model.store.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favorites")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "member_idx")
    private int memberIdx;

    @Column(name = "store_id")
    private int storeId;

    @Column(name = "is_favorite")
    private boolean favorite;
}
