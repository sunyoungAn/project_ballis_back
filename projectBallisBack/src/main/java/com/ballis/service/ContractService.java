package com.ballis.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ballis.model.Buying;
import com.ballis.model.Contract;
import com.ballis.model.Product;
import com.ballis.model.Selling;
import com.ballis.model.DTO.ContractAddDTO;
import com.ballis.model.DTO.ContractChartDTO;
import com.ballis.repository.BuyingRepository;
import com.ballis.repository.ContractRepository;
import com.ballis.repository.ProductRepository;
import com.ballis.repository.SellingRepository;


import jakarta.transaction.Transactional;

@Service
public class ContractService {
	
	@Autowired
	private ContractRepository contractRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private BuyingRepository buyingRepository;
	@Autowired
	private SellingRepository sellingRepository;
	
	public List<ContractChartDTO> getContractChart(Long productid) {
		return contractRepository.getContractChart(productid);
	}
	
	// 거래 체결
	@Transactional
	public Contract save(ContractAddDTO contractDto, String type) {
		Contract contract = new Contract();
		// product 객체 설정
		Product product = productRepository.findById(contractDto.getProductId())
				.orElseThrow(()->new IllegalArgumentException("상품 아이디를 찾을 수 없습니다."));
		contract.setProduct(product);
		// buying 객체 설정, nullable
		if(contractDto.getBuyingId() != null) {
			Optional<Buying> buying = buyingRepository.findById(contractDto.getBuyingId());
			if(buying.isPresent()) {
				contract.setBuying(buying.get());	
			} else {
				contract.setBuying(null);	
			}
		}
		// selling 객체 설정, nullable
		if(contractDto.getSellingId() != null) {
			Optional<Selling> selling = sellingRepository.findById(contractDto.getSellingId());
			if(selling.isPresent()) {
				contract.setSelling(selling.get());		
			} else {
				contract.setSelling(null);
			}
		}
		// 나머지
		contract.setBuyerNumber(contractDto.getBuyerNumber());
		contract.setSellerNumber(contractDto.getSellerNumber());
		contract.setContractDate(LocalDateTime.now());
		contract.setPrice(contractDto.getPrice());		
		contract.setProductSize(contractDto.getProductSize());
		contract.setRegistDate(LocalDateTime.now());
		
		// 빠른배송
		if("fast".equals(type)) {
			contract.setSellingStatus(null);
			contract.setBuyingStatus(38);
		}
		// 즉시구매, 즉시판매
		if("normal".equals(type)) {
			contract.setSellingStatus(21);
			contract.setBuyingStatus(31);
		}
		return contractRepository.save(contract);
	}

	
	public List<Contract> findBySellerNumber(Long sellerNumber) {
		return contractRepository.findBySellerNumber(sellerNumber);
	}

	public List<Contract> findByBuyerNumber(Long buyerNumber) {
		return contractRepository.findByBuyerNumber(buyerNumber);
	}

	public List<Contract> findByBuyerNumberAndRegistDateBetween(Long buyerNumber, LocalDateTime startDateTime,
			LocalDateTime endDateTime) {
		return contractRepository.findByBuyerNumberAndRegistDateBetween(buyerNumber, startDateTime, endDateTime);
	}
	
	public List<Contract> findBySellerNumberAndRegistDateBetween(Long sellerNumber, LocalDateTime startDateTime,
			LocalDateTime endDateTime) {
		return contractRepository.findBySellerNumberAndRegistDateBetween(sellerNumber, startDateTime, endDateTime);
	}
}
