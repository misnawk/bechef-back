package com.example.bechef.controller.favorite;

import com.example.bechef.dto.FavoriteDTO;
import com.example.bechef.service.favorite.FavoriteService;
import com.example.bechef.token.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService; // FavoriteService 주입

    @Autowired
    private JwtUtil jwtUtil; // JwtUtil 주입

    // 즐겨찾기 목록을 가져옴
    @GetMapping
    // @RequestParam을 사용하여 요청 파라미터로 받은 memberIdx를 int 타입의 memberIdx 변수에 담음
    public ResponseEntity<List<FavoriteDTO>> getFavorites(@RequestParam int memberIdx) {
        // memberIdx에 해당하는 즐겨찾기 목록을 FavoriteService를 통해 조회하여 반환
        return ResponseEntity.ok(favoriteService.getFavoriteStores(memberIdx));
    }
}
