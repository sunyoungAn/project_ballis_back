package com.ballis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ballis.model.Address;
import com.ballis.model.Member;

public interface AddressRepository extends JpaRepository<Address, Long> {
	
	List<Address> findByMemberMemberNumber(Long member);
	
	int countByMember_MemberNumber(Long memberNumber);

	Address findByMemberMemberNumber(Member member);

}
