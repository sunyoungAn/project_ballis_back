package com.ballis.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

import com.ballis.model.Buying;
import com.ballis.model.Member;

public interface BuyingRepository extends JpaRepository<Buying, Long>,JpaSpecificationExecutor<Buying> {

	List<Buying> findByProduct_Id(@Param("productid") Long productid);
	
	List<Buying> findByRegistDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

	List<Buying> findByMemberMemberNumber(Member member);
	
}
