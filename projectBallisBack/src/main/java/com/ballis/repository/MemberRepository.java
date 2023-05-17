package com.ballis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ballis.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {

	Member findByPhoneNumber(String phoneNumber);

	Member findByEmailAndPassword(String email, String password);

	Member findByMemberNumber(Long memberNumber);

	Member findByEmail(String email);

	Member findByEmailAndPhoneNumber(String email, String phoneNumber);

	Member findByMemberNumber(String memberNumber);
	
}
