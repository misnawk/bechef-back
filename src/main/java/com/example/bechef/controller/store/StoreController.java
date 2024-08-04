package com.example.bechef.controller.store;

import com.example.bechef.dto.StoreDTO;
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

    // 가게 검색 메서드
    @GetMapping("/search")
    // 검색어를 매개변수로 받아 검색된 가게 목록을 반환
    public List<StoreDTO> searchStore(@RequestParam String query) {
        return storeService.searchStores(query); // 검색어(query)에 해당하는 가게 목록을 반환
    }
}
