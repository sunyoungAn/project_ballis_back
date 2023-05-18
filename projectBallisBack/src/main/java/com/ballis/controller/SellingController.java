package com.ballis.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ballis.model.Contract;
import com.ballis.model.Member;
import com.ballis.model.Product;
import com.ballis.model.Selling;
import com.ballis.model.DTO.SellingAddDTO;
import com.ballis.model.DTO.SellingChartDTO;
import com.ballis.service.ContractService;
import com.ballis.service.MemberService;
import com.ballis.service.SellingService;



@RestController
public class SellingController {
	
	@Autowired
	private SellingService sellingService;
	
	@Autowired
	private  MemberService memberService;
	
	@Autowired
	private ContractService contractService;
	
	@GetMapping("/api/get/sell/chart")
	public List<SellingChartDTO> findSellingByProduct(@RequestParam Long productid) {
		return sellingService.findByProduct_IdOrderByRegistDateDesc(productid);
	}
	
	@PostMapping("/api/post/sell")
	public ResponseEntity<Selling> addSelling (@RequestBody SellingAddDTO sellingDto, @RequestParam String type) {
		try {
			Selling sellingInfo = sellingService.save(sellingDto, type);
			return new ResponseEntity<Selling>(sellingInfo, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	//판매입찰 전체리스트
	@GetMapping("/api/get/sellingall/{memberNumber}")
	public ResponseEntity getSellingAll(@PathVariable Long memberNumber) {
		Member member = memberService.findByMemberNumber(memberNumber);
		List<Map<String, Object>> sellingList = new ArrayList<>();
		
		List<Selling> lists = sellingService.findByMemberMemberNumber(member.getMemberNumber());
		for(Selling selling : lists) {
			Map<String, Object> sellingMap = new HashMap<>();
		    sellingMap.put("selling", selling);
		    
		    Product product = selling.getProduct();
		    String productName = product.getProductKorName();
		    sellingMap.put("productName", productName);
		        
		    sellingList.add(sellingMap);
		}
		return new ResponseEntity<>(sellingList, HttpStatus.OK);
	}
	
	//진행중 전체 리스트
	@GetMapping("/api/get/sellinging/{sellerNumber}")
	public ResponseEntity<List<Map<String, Object>>> getSellinging(@PathVariable("sellerNumber") Long sellerNumber) {
	    List<Map<String, Object>> contractList = new ArrayList<>();

	    List<Contract> lists = contractService.findBySellerNumber(sellerNumber);
	    List<Contract> filteredList = lists.stream()
	            .filter(contract -> contract.getSellingStatus() <= 49)
	            .collect(Collectors.toList());

	    for (Contract contract : filteredList) {
	        Map<String, Object> contractMap = new HashMap<>();
	        contractMap.put("contract", contract);

	        Product product = contract.getProduct();
	        String productName = product.getProductKorName();
	        contractMap.put("productName", productName);

	        contractList.add(contractMap);
	    }

	    return new ResponseEntity<>(contractList, HttpStatus.OK);
	}
	
	//거래완료 전체 리스트
	@GetMapping("/api/get/sellingend/{sellerNumber}")
	public ResponseEntity getSellingend(@PathVariable("sellerNumber") Long sellerNumber) {
		List<Map<String, Object>> contractList = new ArrayList<>();
		
		List<Contract> lists = contractService.findBySellerNumber(sellerNumber);
		List<Contract> filterdList = lists.stream().filter(contract -> contract.getSellingStatus() >=50)
											.collect(Collectors.toList());
		
		for(Contract contract : filterdList) {
			Map<String, Object> contractMap = new HashMap<>();
			contractMap.put("contract", contract);
			
			Product product = contract.getProduct();
			String productName = product.getProductKorName();
			contractMap.put("productName", productName);
			
			contractList.add(contractMap);
		}
		return new ResponseEntity<>(contractList, HttpStatus.OK);
	}
	
	
	
	
	
//	@GetMapping("/api/get/sell/test")
//	public List<Selling> findSellingProduct(@RequestParam Long productid) {
//		return sellingService.findByOneProduct(1, 11, productid);
//				
//				
//	}

}
