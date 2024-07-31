package com.example.bechef.repository.member;

import com.example.bechef.model.member.Member;
import com.example.bechef.model.member.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

 //관리자 계정 제외하고 멤버 데이터 불러오기
 List<Member> findByRoleNot(Role role);

 //로그인했을때 입력한 아이디로 사용자 정보를 가져오기위한 메서드
 Member findById(String memberId);

 //리뷰 불러오기
 List<Member> findByIdxIn(List<Integer> IdxList);
}
