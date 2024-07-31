package com.example.bechef.repository.menu;

import com.example.bechef.model.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    List<Menu> findByStoreId(int storeId);
}
