package com.example.bechef.model.member;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_idx")
    private int idx;

    @Column(name ="member_name")
    private String name;

    @Column(name ="member_id")
    private String id;

    @Column(name = "member_pwd")
    private String pwd;

    @Column(name = "member_email")
    private String email;

    @Column(name = "member_phone")
    private String phone;

    @Column(name = "member_address")
    private String address;

    @Column(name = "role")
    @ColumnDefault("USER")
    @Enumerated(EnumType.STRING)
    private Role role;

}
