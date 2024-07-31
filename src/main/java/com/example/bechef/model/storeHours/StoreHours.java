package com.example.bechef.model.storeHours;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.DayOfWeek;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "store_hours")
public class StoreHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_hours_id")
    private int storeHoursId;

    @Column(name = "store_id")
    private Integer storeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_day_of_week")
    private Day storeDayOfWeek;

    @Column(name = "opentime")
    private Time openTime;

    @Column(name = "closetime")
    private Time closeTime;

    @Column(name = "is_closed")
    private Boolean isClosed;
}