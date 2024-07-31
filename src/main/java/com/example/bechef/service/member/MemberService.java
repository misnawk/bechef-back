package com.example.bechef.service.member;

import com.example.bechef.dto.MemberDTO;
import com.example.bechef.model.member.Member;

import java.util.List;

public interface MemberService {
    //회원가입시 사용자의 정보를 저장하기위한 메서드
    void saveUser(Member member);

    //로그인시 입력한 아이디로 사용자를 찾기위한 메서드
    Member findById(String id);

    //관리자 페이지의 유저목록을 불러오기위한 메서드
    List<MemberDTO> findAll();

    //관리자 페이지의 유저목록을 삭제하기위한 메서드
    void delete(int member_idx);

    //리뷰작성 멤버 이름 가져오기
    List<Member>getMemberNameByIdx(List<Integer> IdxList);

    //회원탈퇴
    void deleteUserById(String memberId);
}
