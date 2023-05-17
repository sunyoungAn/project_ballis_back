package com.ballis.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ballis.model.Buying;
import com.ballis.model.Member;
import com.ballis.model.DTO.BuyingAddDTO;
import com.ballis.model.DTO.BuyingChartDTO;
import com.ballis.service.BuyingService;
import com.ballis.service.MemberService;
import com.ballis.service.ProductService;


@RestController
public class BuyingController {
	
	@Autowired
	private BuyingService buyingService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/api/get/buy/chart")
	public List<BuyingChartDTO> findBuyingByProduct(@RequestParam Long productid){
		return buyingService.findBuyingByProduct_Id(productid);
	}
	
	@PostMapping("/api/post/buy")
	public ResponseEntity<Buying> addBuying(@RequestBody BuyingAddDTO buyingDto) {
		try {
			Buying buyingInfo = buyingService.save(buyingDto);
			return new ResponseEntity<Buying>(buyingInfo, HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@GetMapping("/api/buying/date")
	public List<Buying> searchDate( @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
	        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate){
		
		LocalDateTime startDateTime = startDate.atStartOfDay();
		LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
		
		return buyingService.findBuyingByRegistDateBetween(startDateTime, endDateTime);
	}
	
	@GetMapping("/api/get/buying/{memberNumber}")
	public ResponseEntity getBuying(@PathVariable Long memberNumber) {
		Member member = memberService.findByMemberNumber(memberNumber);
		
		List<Buying> lists = buyingService.finByMemberMemberNumber(member);
		
		return new ResponseEntity<>(lists, HttpStatus.OK);
	}

}
