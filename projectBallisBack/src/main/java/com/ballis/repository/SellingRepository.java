package com.ballis.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

import com.ballis.model.Selling;

public interface SellingRepository extends JpaRepository<Selling, Long>, JpaSpecificationExecutor<Selling> {

	List<Selling> findByProduct_IdOrderByRegistDateDesc(@Param("productid") Long productid);
	
	// 빠른 배송 상품 존재 하는지
	List<Selling> findByInventoryDivAndSellingStatusAndProductId(Integer inventoryDiv, Integer sellingStatus, Long productid);

	List<Selling> findBySellingStatus(int sellingStatus);

	List<Selling> findByMemberMemberNumber(Long memberNumber);
	
	List<Selling> findByMemberMemberNumberAndInventoryDiv(Long memberNumber, int inventoryDiv);

	List<Selling> findSellingByMemberMemberNumberAndRegistDateBetween(Long memberNumber, LocalDateTime startDateTime,
			LocalDateTime endDateTime);

	
}
