package com.example.bechef.model.review;

import com.example.bechef.model.member.Member;
import com.example.bechef.model.store.Store;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private int reviewId;

    @Column(name = "member_idx")
    private int memberIdx;

    @Column(name = "store_id")
    private int storeId;

    @Column(name = "review_rating",precision =3, scale = 1)
    private BigDecimal reviewRating;

    @Column(name = "comment")
    private String comment;

    @Column(name = "review_date")
    private LocalDateTime reviewDate;

//    @Transient
//    private String reviewDateFormatted;
}
