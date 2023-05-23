package com.ballis.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ballis.model.Contract;
import com.ballis.model.Image;
import com.ballis.model.Member;
import com.ballis.model.Product;
import com.ballis.model.Selling;
import com.ballis.model.DTO.SellingAddDTO;
import com.ballis.model.DTO.SellingChartDTO;
import com.ballis.service.ContractService;
import com.ballis.service.ImageService;
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
	
	@Autowired
	private ImageService imageService;
	
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
	
	//판매입찰 날짜 조회
	@GetMapping("/api/selling/date/{memberNumber}")
	public ResponseEntity searchDate(@PathVariable Long memberNumber, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
	        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate){
		Member member = memberService.findByMemberNumber(memberNumber);
		List<Map<String, Object>> sellingList = new ArrayList<>();
		
	    LocalDateTime startDateTime = startDate.atStartOfDay();
	    LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
	    
	    List<Selling> lists = sellingService.findSellingByMemberMemberNumberAndRegistDateBetween(member.getMemberNumber(), startDateTime, endDateTime);
	    List<Selling> filteredList = lists.stream()
				.filter(selling -> selling.getSellingStatus() <= 10)
				.collect(Collectors.toList());	
	    
	    for(Selling selling : filteredList) {
			Map<String, Object> sellingMap = new HashMap<>();
			sellingMap.put("selling", selling);
		    
		    Product product = selling.getProduct();
		    Long productId = product.getId();
		    String productName = product.getProductKorName();
		    sellingMap.put("productName", productName);
		    
		    List<Image> imagelist = imageService.findByTargetIdAndPageDiv(productId, 1);
		    sellingMap.put("imagelist", imagelist);     
		        
		    sellingList.add(sellingMap);
		}
	    
	    return new ResponseEntity<>(sellingList, HttpStatus.OK);
	}
	
	//진행중 날짜 조회
	@GetMapping("/api/sellinging/date/{sellerNumber}")
	public ResponseEntity searchDate2(@PathVariable Long sellerNumber, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
	        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate){
		List<Map<String, Object>> contractList = new ArrayList<>();
		
	    LocalDateTime startDateTime = startDate.atStartOfDay();
	    LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
	    
	    List<Contract> lists = contractService.findBySellerNumberAndRegistDateBetween(sellerNumber, startDateTime, endDateTime);
		List<Contract> filteredList = lists.stream()
		            .filter(contract -> contract.getBuyingStatus() <= 49)
		            .collect(Collectors.toList());

		for (Contract contract : filteredList) {
		     Map<String, Object> contractMap = new HashMap<>();
		     contractMap.put("contract", contract);

		     Product product = contract.getProduct();
		     Long productId = product.getId();
		     String productName = product.getProductKorName();
		     contractMap.put("productName", productName);
		     
		     List<Image> imagelist = imageService.findByTargetIdAndPageDiv(productId, 1);
		     contractMap.put("imagelist", imagelist);   

		     contractList.add(contractMap);
		    }

		return new ResponseEntity<>(contractList, HttpStatus.OK);
	}
	
	//완료 날짜 조회
	@GetMapping("/api/sellingend/date/{sellerNumber}")
	public ResponseEntity searchDate3(@PathVariable Long sellerNumber, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
	        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate){
		List<Map<String, Object>> contractList = new ArrayList<>();
		
	    LocalDateTime startDateTime = startDate.atStartOfDay();
	    LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
	    
	    List<Contract> lists = contractService.findBySellerNumberAndRegistDateBetween(sellerNumber, startDateTime, endDateTime);
		List<Contract> filteredList = lists.stream()
		            .filter(contract -> contract.getBuyingStatus() >= 60)
		            .collect(Collectors.toList());

		for (Contract contract : filteredList) {
		     Map<String, Object> contractMap = new HashMap<>();
		     contractMap.put("contract", contract);

		     Product product = contract.getProduct();
		     Long productId = product.getId();
		     String productName = product.getProductKorName();
		     contractMap.put("productName", productName);
		     
		     List<Image> imagelist = imageService.findByTargetIdAndPageDiv(productId, 1);
			 contractMap.put("imagelist", imagelist);  


		     contractList.add(contractMap);
		    }

		return new ResponseEntity<>(contractList, HttpStatus.OK);
	}
	

	//판매입찰 전체리스트(보관판매도 조회)
	@GetMapping("/api/get/sellingall/{memberNumber}/{inventoryDiv}")
	public ResponseEntity getSellingAll(@PathVariable Long memberNumber, @PathVariable int inventoryDiv) {
		Member member = memberService.findByMemberNumber(memberNumber);
		List<Map<String, Object>> sellingList = new ArrayList<>();
		
		List<Selling> lists = sellingService.findByMemberMemberNumberAndInventoryDiv(member.getMemberNumber(), inventoryDiv);
		List<Selling> filteredList;

		if (inventoryDiv == 1) {
		    filteredList = lists.stream()
		        .filter(selling -> selling.getSellingStatus() >= 11 && selling.getSellingStatus() <= 18)
		        .collect(Collectors.toList());
		    } else if (inventoryDiv == 2) {
		    filteredList = lists.stream()
		        .filter(selling -> selling.getSellingStatus() < 3)
		        .collect(Collectors.toList());
		} else {
		    filteredList = new ArrayList<>();
		}
				
		for(Selling selling : filteredList) {
			Map<String, Object> sellingMap = new HashMap<>();
		    sellingMap.put("selling", selling);
		    
		    Product product = selling.getProduct();
		    Long productId = product.getId();
		    String productName = product.getProductKorName();
		    sellingMap.put("productName", productName);
		    
		    List<Image> imagelist = imageService.findByTargetIdAndPageDiv(productId, 1);
		    sellingMap.put("imagelist", imagelist);
		   
		    
		    List<Contract> contractlist = contractService.findBySellerNumberAndProductIdAndSellingStatus(memberNumber, productId, 50);
	        if (!contractlist.isEmpty()) {
	            Contract contract = contractlist.get(0);
	            sellingMap.put("sellingStatus", contract.getSellingStatus());
	        } else {
	            sellingMap.put("sellingStatus", selling.getSellingStatus());
	        }

	        sellingList.add(sellingMap);
	    }

	    return new ResponseEntity<>(sellingList, HttpStatus.OK);
	}
	//입찰 삭제
	@DeleteMapping("/api/delete/selling/{id}")
	public ResponseEntity deleteSelling(@PathVariable Long id) {
		sellingService.delete(id);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//진행중 전체 리스트
	@GetMapping("/api/get/sellinging/{sellerNumber}")
	public ResponseEntity<List<Map<String, Object>>> getSellinging(@PathVariable("sellerNumber") Long sellerNumber) {
	    List<Map<String, Object>> contractList = new ArrayList<>();

	    List<Contract> lists = contractService.findBySellerNumber(sellerNumber);
	    List<Contract> filteredList = lists.stream()
	    		.filter(contract -> contract.getSellingStatus() == null || contract.getSellingStatus() <= 49)
	            .collect(Collectors.toList());

	    for (Contract contract : filteredList) {
	        Map<String, Object> contractMap = new HashMap<>();
	        contractMap.put("contract", contract);

	        Product product = contract.getProduct();
	        Long productId = product.getId();
	        String productName = product.getProductKorName();
	        contractMap.put("productName", productName);
	        
	        List<Image> imagelist = imageService.findByTargetIdAndPageDiv(productId, 1);
	        contractMap.put("imagelist", imagelist);

	        contractList.add(contractMap);
	    }

	    return new ResponseEntity<>(contractList, HttpStatus.OK);
	}
	
	//거래완료 전체 리스트
	@GetMapping("/api/get/sellingend/{sellerNumber}")
	public ResponseEntity getSellingend(@PathVariable("sellerNumber") Long sellerNumber) {
		List<Map<String, Object>> contractList = new ArrayList<>();
		
		List<Contract> lists = contractService.findBySellerNumber(sellerNumber);
		List<Contract> filterdList = lists.stream()
				.filter(contract -> contract.getSellingStatus() == null || contract.getSellingStatus() >= 50)
			    .collect(Collectors.toList());
		for(Contract contract : filterdList) {
			Map<String, Object> contractMap = new HashMap<>();
			contractMap.put("contract", contract);
			
			Product product = contract.getProduct();
			Long productId = product.getId();
			String productName = product.getProductKorName();
			contractMap.put("productName", productName);
			
			 List<Image> imagelist = imageService.findByTargetIdAndPageDiv(productId, 1);
		     contractMap.put("imagelist", imagelist);
		        
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
