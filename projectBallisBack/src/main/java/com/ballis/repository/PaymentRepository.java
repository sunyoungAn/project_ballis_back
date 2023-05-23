package com.ballis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ballis.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	// 보관판매타겟아이디로 검색
//	Payment findBySelling_Id(Long targetSellingId);
	List<Payment> findBySelling_Id(Long targetSellingId);

	// 거래체결타겟아이디로 검색
	Payment findByContract_Id(Long targetContractId);

	
	
}
