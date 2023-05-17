package com.ballis.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ballis.model.Member;
import com.ballis.model.Product;
import com.ballis.model.Selling;
import com.ballis.model.DTO.SellingAddDTO;
import com.ballis.model.DTO.SellingChartDTO;
import com.ballis.service.MemberService;
import com.ballis.service.SellingService;


@RestController
public class SellingController {
	
	@Autowired
	private SellingService sellingService;
	
	@Autowired
	private  MemberService memberService;
	
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
	
	//거래구분 리스트
	@GetMapping("/api/get/selling/{sellingStatus}")
	public ResponseEntity getSelling(@PathVariable("sellingStatus") int sellingStatus) {
		 List<Selling> sellings = sellingService.findBySellingStatus(sellingStatus);
		 List<Map<String, Object>> sellingList = new ArrayList<>();
		    
		 for (Selling selling : sellings) {
		     Map<String, Object> sellingMap = new HashMap<>();
		     sellingMap.put("selling", selling);
		        
		     Product product = selling.getProduct();
		     String productName = product.getProductKorName();
		     sellingMap.put("productName", productName);
		        
		     sellingList.add(sellingMap);
		    }
		    
		    return new ResponseEntity<>(sellingList, HttpStatus.OK);
	}
	
	
//	@GetMapping("/api/get/sell/test")
//	public List<Selling> findSellingProduct(@RequestParam Long productid) {
//		return sellingService.findByOneProduct(1, 11, productid);
//				
//				
//	}

}
