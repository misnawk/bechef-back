package com.example.bechef.service.member;

import com.example.bechef.model.member.Member;
import com.example.bechef.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


// UserDetailsService 의 인터페이스를 상속받음
@Service
public class MemberDetailServiceImpl implements UserDetailsService {


    @Autowired
    private MemberRepository memberRepository;


    @Autowired
    public MemberDetailServiceImpl(MemberRepository memberRepository){ this.memberRepository= memberRepository;}


    // UserDetails 는 spring Security 에서 기본적으로 제공하는 인터페이스
    // loadUserByUsername 은 UserDetailsService 인터페이스에 있는 메서드
    // 즉 로그인에서 입력한 사용자의 아이디를 기준으로 DB에서 사용자를 검색
    //없으면 예외 처리를한다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findById(username);

        //username없을때
        if(member == null) {
            throw new UsernameNotFoundException("유저가 없습니다.");
        }

        //사용자의 권한을 처리하는 객체
        //즉 로그인한 사용자가 USER 인지 ADMIN 인지 authorities 리스트에 저장한다.
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(member.getRole().name()));

        // UserDetails 객체 생성 및 반환
        return new User(member.getId(), member.getPwd(), Collections.emptyList());

    }
}
