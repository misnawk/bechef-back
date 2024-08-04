package com.example.bechef.dto;

import lombok.Data;

@Data
public class MemberDTO {

    private int member_idx; // 회원의 고유 식별자
    private String member_name; // 회원의 이름
    private String member_id; // 회원의 아이디
    private String member_email; // 회원의 이메일
    private String member_phone; // 회원의 전화번호
    private String member_address; // 회원의 주소
}
