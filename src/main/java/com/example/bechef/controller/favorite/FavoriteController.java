package com.example.bechef.controller.favorite;

import com.example.bechef.dto.FavoriteDTO;
import com.example.bechef.model.favorite.Favorite;
import com.example.bechef.service.favorite.FavoriteService;
import com.example.bechef.token.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<FavoriteDTO>> getFavorites(@RequestParam int memberIdx) {
        return ResponseEntity.ok(favoriteService.getFavoriteStores(memberIdx));
    }
}
