package com.ballis.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ballis.model.Contract;
import com.ballis.model.DTO.ContractChartDTO;

public interface ContractRepository extends JpaRepository<Contract, Long>, JpaSpecificationExecutor<Contract> {

	@Query(value="SELECT "
			+ "new com.ballis.model.DTO.ContractChartDTO"
			+ "(con.id, con.price, con.contractDate, "
			+ "COALESCE(sell.productSize, 0) AS sellSize, "
			+ "COALESCE(buy.productSize, 0) AS buySize) "
			+ "FROM Contract con "
			+ "LEFT JOIN Product prod ON con.product = prod.id "
			+ "LEFT JOIN Selling sell ON con.selling = sell.id "
			+ "LEFT JOIN Buying buy ON con.buying = buy.id " 
			+ "WHERE prod.id = :productid " // con.product = :productid 는 오류는 안나는데 데이터 조회가 안됨
			+ "ORDER BY con.contractDate ASC, con.price DESC",  
			nativeQuery = false)
	List<ContractChartDTO> getContractChart(@Param("productid") Long productid);
	
	
	List<Contract> findBySellerNumber(Long sellerNumber);

	List<Contract> findByBuyerNumber(Long buyerNumber);

	List<Contract> findByBuyerNumberAndRegistDateBetween(Long buyerNumber, LocalDateTime startDateTime,
			LocalDateTime endDateTime);
	
	List<Contract> findBySellerNumberAndRegistDateBetween(Long sellerNumber, LocalDateTime startDateTime,
			LocalDateTime endDateTime);

}
