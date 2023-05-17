package com.ballis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ballis.model.Selling;
import com.ballis.model.DTO.SellingAddDTO;
import com.ballis.model.DTO.SellingChartDTO;
import com.ballis.service.SellingService;

@RestController
public class SellingController {
	
	@Autowired
	private SellingService sellingService;
	
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
	
	
//	@GetMapping("/api/get/sell/test")
//	public List<Selling> findSellingProduct(@RequestParam Long productid) {
//		return sellingService.findByOneProduct(1, 11, productid);
//				
//				
//	}

}
