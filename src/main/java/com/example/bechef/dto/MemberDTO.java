package com.example.bechef.dto;

import lombok.Data;

@Data
public class MemberDTO {

    private int member_idx;
    private String member_name;
    private String member_id;
    private String member_email;
    private String member_phone;
    private String member_address;
}
