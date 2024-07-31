package com.example.bechef.repository.store;

import com.example.bechef.model.storeHours.StoreHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreHoursRepository extends JpaRepository<StoreHours, Integer> {

    @Query("SELECT sh FROM StoreHours sh WHERE sh.storeId = :storeId")
    List<StoreHours> findAllByStoreId(@Param("storeId") int storeId);
}
