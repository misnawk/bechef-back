package com.example.bechef.controller.member;


import com.example.bechef.dto.ApiResponse;
import com.example.bechef.model.member.Member;
import com.example.bechef.service.member.MemberDetailServiceImpl;
import com.example.bechef.service.member.MemberService;
import com.example.bechef.status.ResponseStatus;
import com.example.bechef.status.ResultStatus;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.bechef.token.JwtUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bechef/member")
public class MemberController {

    @Autowired
    MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    MemberDetailServiceImpl memberDetailService;

    @Autowired
    JwtUtil jwtUtil;

    // API 응답을 생성하는 메서드
    public ApiResponse<?> validateApiResponse(ResultStatus status){
        ResponseStatus resultStatus =ResultStatus.FAIL.equals(status) ? ResponseStatus.FAIL : ResponseStatus.SUCCESS;
        String message = ResultStatus.FAIL.equals(status) ? "실패" : "성공";
        return new ApiResponse(resultStatus, message,null);
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerMember(@RequestBody Member member) {
        // 비밀번호를 암호화하여 설정
        member.setPwd(passwordEncoder.encode(member.getPwd()));

        // 회원 정보 저장
        memberService.saveUser(member);
        ApiResponse  apiResponse = new ApiResponse(ResponseStatus.SUCCESS, "회원가입 성공", null);
        return ResponseEntity.ok(apiResponse);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@RequestBody Member member){

        try {
            // 사용자 정보 로드
            UserDetails userDetails = memberDetailService.loadUserByUsername(member.getId());

            // 비밀번호 검증
            if(userDetails == null || !passwordEncoder.matches(member.getPwd(), userDetails.getPassword())){
                throw new BadCredentialsException("아이디와 비밀번호가 일치하지 않습니다.");
            }
            // 인증된 사용자 정보 조회
            Member authenticatedMember = memberService.findById(member.getId());
            System.out.println("로그인한 사용자의 데이터 >>>>>>>>>>>>>>>>>>>>" + authenticatedMember);

            // JWT 토큰 생성 (역할 정보 포함)
            String token = JwtUtil.generateToken(authenticatedMember);
            System.out.println("생성된 JWT 토큰 >>>>>>>>>>>>>>>>>>>>" + token);

            // HTTP 헤더 설정
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "Bearer " + token);

            // 인증 객체 생성 및 보안 컨텍스트 설정
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + authenticatedMember.getRole().name()));
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 응답 데이터 구성
            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("token", token);
            System.out.println("Response Headers: " + httpHeaders);


            ApiResponse<Map<String, String>> apiResponse = new ApiResponse<>(ResponseStatus.SUCCESS, "로그인 성공", tokenMap);
            return ResponseEntity.ok().headers(httpHeaders).body(apiResponse);

        } catch (UsernameNotFoundException | BadCredentialsException e) {
            System.out.println("로그인 실패: " + e.getMessage());
            ApiResponse<Map<String, String>> apiResponse = new ApiResponse<>(ResponseStatus.UNAUTHORIZED, "로그인 실패", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
        }
    }

    // 회원탈퇴
    @DeleteMapping
    public ResponseEntity<?> deleteMember(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Authorization header is invalid");
        }

        String token = authorizationHeader.substring(7);
        if (!jwtUtil.validToken(token)) {
            return ResponseEntity.status(401).body("Token is invalid");
        }

        Claims claims = jwtUtil.extractToken(token);
        String memberId = claims.get("id", String.class);

        memberService.deleteUserById(memberId);

        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }
}
