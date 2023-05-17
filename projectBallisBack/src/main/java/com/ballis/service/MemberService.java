package com.ballis.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ballis.model.Member;
import com.ballis.repository.MemberRepository;

import jakarta.transaction.Transactional;


@Service
public class MemberService {
	
	@Autowired
	private MemberRepository memberRepository;
	
	public Member save(Member joinMember) {
		return memberRepository.save(joinMember);
	}

	public String findEmailByPhoneNumber(String phoneNumber) {
		
		Member member = memberRepository.findByPhoneNumber(phoneNumber);
		
		return member.getEmail();
	}

	public Member findByEmailAndPassword(String email, String password) {
		return memberRepository.findByEmailAndPassword(email,password);
	}

	public Member findByMemberNumber(Long memberNumber) {
		return memberRepository.findByMemberNumber(memberNumber);
	}

	public Member findByEmailAndPhoneNumber(String email, String phoneNumber) {
		
		
	    return memberRepository.findByEmailAndPhoneNumber(email, phoneNumber);
	}

	@Transactional
	public void updatePassword(String newpassword, String email) {
		  // 이메일을 이용해 유저 정보 가져오기
        Member member = memberRepository.findByEmail(email);
        // 유저 정보에 새로운 비밀번호 업데이트
        member.setPassword(newpassword);
        memberRepository.save(member);
	}

	public Member findByMemberNumber(String memberNumber) {
		return memberRepository.findByMemberNumber(memberNumber);
	}

	@Transactional
	public Member update(Long memberNumber, String password, String name, String phoneNumber, String bankName,
			String accountNumber, String depositor, LocalDateTime modifiedDate) {
		Member member = memberRepository.findByMemberNumber(memberNumber);
		member.update(password,name,phoneNumber,bankName,accountNumber,depositor,modifiedDate);
		
		Member result = member.update(password,name,phoneNumber,bankName,accountNumber,depositor,modifiedDate);
		
		return result;
	}

}
