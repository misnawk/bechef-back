package com.example.bechef.token;

import com.example.bechef.service.member.MemberDetailServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MemberDetailServiceImpl memberDetailService;

    // 토큰 검사안하고 통과시키는 url
    private List<String> excludeURLS = Arrays.asList(
            "/bechef/member/register",
            "/bechef/member/login",
            "/bechef/search",
            "/api/info/time/",
            "/api/info/info_menu/",
            "/api/info/info_page/",
            "/api/info/info_review/",
            "/api/info/update_store_rating/",
            "/api/info/average_rating/"
    );
//    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtFilter(JwtUtil jwtUtil, MemberDetailServiceImpl memberDetailService) {
        this.jwtUtil = jwtUtil;
        this.memberDetailService = memberDetailService;
    }

    // 스프링부트에 요청하면 무조건 걸치는 입구
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        System.out.println("JwtFilter - Requested URI: " + requestURI);

        //토큰검사 안하고 통과하는로직
        if (excludeURLS.stream().anyMatch(uri -> request.getRequestURI().startsWith(uri)))  {
            System.out.println("Request URI: " + request.getRequestURI() + " is excluded from authentication.");
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization 헤더에서 토큰 추출
        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("JwtFilter - Authorization Header: " + authorizationHeader);

        boolean headerIsEmpty = authorizationHeader == null;
        boolean doNotHaveBearer = authorizationHeader != null && !authorizationHeader.startsWith("Bearer ");

        // 토큰이 없거나 형식이 올바르지 않은 경우 401 Unauthorized 응답
        if(headerIsEmpty || doNotHaveBearer){
            System.out.println("JwtFilter - Missing or invalid Authorization header");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authorization header is invalid");
            return;
        }

        // "Bearer " 접두사 제거하여 실제 토큰 추출
        String token = authorizationHeader.substring(7);

        // 토큰 유효성 검사
        if(!jwtUtil.validToken(token)){
            System.out.println("JwtFilter - Invalid Token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token is invalid");
            return;
        }

        // 토큰에서 사용자 정보와 역할 추출
        Claims claims = jwtUtil.extractToken(token);
        String username = claims.getSubject();
        String role = claims.get("role", String.class);

        // 사용자 상세 정보 로드
        UserDetails userDetails = memberDetailService.loadUserByUsername(username);
        if (userDetails == null) {
            System.out.println("JwtFilter - User not found: " + username);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("User not found");
            return;
        }

        // 사용자 역할을 기반으로 권한 생성
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));

        // 인증 객체 생성 및 SecurityContext에 설정
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 다음 필터로 요청 전달
        filterChain.doFilter(request,response);
    }
}