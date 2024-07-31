package com.example.bechef.service.member;

import com.example.bechef.dto.MemberDTO;
import com.example.bechef.model.member.Member;
import com.example.bechef.model.member.Role;
import com.example.bechef.model.review.Review;
import com.example.bechef.repository.member.MemberRepository;
import com.example.bechef.repository.review.ReviewRepository;
import com.example.bechef.service.store.StoreService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private StoreService storeService;

    //회원가입
    @Override
    public void saveUser(Member member) {
        // 회원의 역할이 지정되지 않은 경우 기본 역할을 USER로 설정
        if(member.getRole() == null){
            member.setRole(Role.USER);
        }
        // 회원 정보를 저장
        memberRepository.save(member);
    }

    //아이디로 유저 데이터 검색
    @Override
    public Member findById(String id) {
        return memberRepository.findById(id);
    }



    // Member 객체를 MemberDTO로 변환하는 메서드
    private MemberDTO convertToDTO(Member member) {
        MemberDTO dto = new MemberDTO();
        dto.setMember_idx(member.getIdx());
        dto.setMember_name(member.getName());
        dto.setMember_id(member.getId());
        dto.setMember_email(member.getEmail());
        dto.setMember_phone(member.getPhone());
        dto.setMember_address(member.getAddress());
        return dto;
    }

    //관리자 페이지의 유저목록을 불러오기위한 메서드(관리자의 계정은 제외하고 불러옴)
    @Override
    public List<MemberDTO> findAll() {
        List<Member> members = memberRepository.findByRoleNot(Role.ADMIN);
        // Member 객체를 MemberDTO로 변환하여 리스트로 반환
        return members.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 특정 회원을 삭제하는 메서드
    @Override
    @Transactional
    public void delete(int member_idx) {
        memberRepository.deleteById(member_idx);
    }

    // 주어진 회원 인덱스 목록으로 회원 이름을 불러오는 메서드
    @Override
    public List<Member> getMemberNameByIdx(List<Integer>IdxList) {
        return memberRepository.findByIdxIn(IdxList);
    }

    // 회원탈퇴
    @Override
    @Transactional
    public void deleteUserById(String memberId) {
        Member member = memberRepository.findById(memberId);
        if (member != null) {
            // 회원이 작성한 리뷰 삭제
            List<Review> reviews = reviewRepository.findByMemberIdx(member.getIdx());
            reviewRepository.deleteAll(reviews);

            // 리뷰 삭제 후 매장 평점 업데이트
            Set<Integer> storeIds = reviews.stream()
                    .map(Review::getStoreId)
                    .collect(Collectors.toSet());

            for (Integer storeId : storeIds) {
                storeService.updateStoreRating(storeId);
            }

            // 회원 정보 삭제
            memberRepository.delete(member);
        }
    }
}
