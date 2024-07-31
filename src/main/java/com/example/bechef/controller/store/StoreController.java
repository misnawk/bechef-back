package com.example.bechef.controller.store;

import com.example.bechef.dto.StoreDTO;
import com.example.bechef.model.store.Store;
import com.example.bechef.service.store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bechef")
public class StoreController {
    @Autowired
    private StoreService storeService;

    @GetMapping("/search")
    public List<StoreDTO> searchStore(@RequestParam String query) {
        return storeService.searchStores(query);
    }
}