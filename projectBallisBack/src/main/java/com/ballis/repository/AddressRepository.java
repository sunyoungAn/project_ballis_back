package com.ballis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ballis.model.Address;
import com.ballis.model.Member;

public interface AddressRepository extends JpaRepository<Address, Long> {
	
	@Query("SELECT a FROM Address a WHERE a.member.memberNumber = :memberNumber ORDER BY a.defaultAddress ASC")
	List<Address> findByMemberMemberNumber(@Param("memberNumber") Long memberNumber);
	
	int countByMember_MemberNumber(Long memberNumber);

	Address findByMemberMemberNumber(Member member);

	Address findByMemberMemberNumberAndDefaultAddress(Long id, int i);

}
