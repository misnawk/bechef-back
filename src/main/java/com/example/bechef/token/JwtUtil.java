package com.example.bechef.token;

import com.example.bechef.model.member.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    //jwt 서명에 사용되는 비밀키
    private static final String SECRET_KEY = "bechefbechefbechefbechefbechefbechefbechefbechef";
    //비밀키를 바이트 배열로 변환하여 SecretKey 객체 생성
    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    // Member 객체를 기반으로 JWT 토큰을 생성함
    public static String generateToken(Member member) {
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis())) //토큰 발행시간 설정
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 )) //1시간
                .setSubject(member.getId()) // 토큰의 제목을 사용자의 ID로 설정함
                .claim("id", member.getId()) //사용자의 ID를 클레임으로 추가
                .claim("role", member.getRole().name()) //사용자 role 클레임으로 추가
                .claim("idx",member.getIdx())
                .signWith(key)// 비밀키로 서명
                .compact(); // 토큰 생성 및 직렬화
    }

    //토큰의 유효성을 검사하는 메서드
    public static boolean validToken(String token) {
        try {
            //토큰을 파싱하여 검사하는 메서드
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            //파싱에 성공하면 유효한 토큰
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //토큰에서 클레임(내용)을 추출하는 메서드
    public static Claims extractToken(String token) {
        //토큰을 파싱하고 본문(클레임)을 반환함
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
