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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ballis.model.Buying;
import com.ballis.model.Contract;
import com.ballis.model.Image;
import com.ballis.model.Member;
import com.ballis.model.Product;
import com.ballis.model.Review;
import com.ballis.model.DTO.BuyingAddDTO;
import com.ballis.model.DTO.BuyingChartDTO;
import com.ballis.service.BuyingService;
import com.ballis.service.ContractService;
import com.ballis.service.ImageService;
import com.ballis.service.MemberService;
import com.ballis.service.ProductService;
import com.ballis.service.ReviewService;



@RestController
public class BuyingController {
	
	@Autowired
	private BuyingService buyingService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private ReviewService reviewService;
	
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
	
	//구매입찰 날짜 검색
	@GetMapping("/api/buying/date/{memberNumber}")
	public ResponseEntity searchDate(@PathVariable Long memberNumber, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
	        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate){
		Member member = memberService.findByMemberNumber(memberNumber);
		List<Map<String, Object>> buyingList = new ArrayList<>();
		
	    LocalDateTime startDateTime = startDate.atStartOfDay();
	    LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
	    
	    List<Buying> lists = buyingService.findBuyingByMemberMemberNumberAndDataStatusAndRegistDateBetween(member.getMemberNumber(), 1, startDateTime, endDateTime);
	    for(Buying buying : lists) {
	    	
	    	// 구매입찰 취소, 거래종료 데이터는 제외
	    	if(buying.getBuyingStatus() == 61 || buying.getBuyingStatus() == 99) {
	    		continue;
	    	}
	    	
			Map<String, Object> buyingMap = new HashMap<>();
			buyingMap.put("buying", buying);
		    
		    Product product = buying.getProduct();
		    Long productId = product.getId();
		    String productName = product.getProductKorName();
		    buyingMap.put("productName", productName);
		    
		    List<Image> imagelist = imageService.findByTargetIdAndPageDiv(productId, 1);
		    buyingMap.put("imagelist", imagelist);        
		    buyingList.add(buyingMap);
		}
	    
	    return new ResponseEntity<>(buyingList, HttpStatus.OK);
	}
	
	//진행중 날짜 검색
	@GetMapping("/api/buyinging/date/{buyerNumber}")
	public ResponseEntity<List<Map<String, Object>>> searchDate2(@PathVariable("buyerNumber") Long buyerNumber, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
	        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate){
		List<Map<String, Object>> contractList = new ArrayList<>();
		
	    LocalDateTime startDateTime = startDate.atStartOfDay();
	    LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
	    
	    List<Contract> lists = contractService.findByBuyerNumberAndRegistDateBetween(buyerNumber, startDateTime, endDateTime);
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

	//완료 날짜 검색
	@GetMapping("/api/buyingend/date/{buyerNumber}")
	public ResponseEntity<List<Map<String, Object>>> searchDate3(@PathVariable("buyerNumber") Long buyerNumber, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
		        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate){
		List<Map<String, Object>> contractList = new ArrayList<>();
			
		LocalDateTime startDateTime = startDate.atStartOfDay();
		LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
		    
		List<Contract> lists = contractService.findByBuyerNumberAndRegistDateBetween(buyerNumber, startDateTime, endDateTime);
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
		    
		    List<Review> reviewlist = reviewService.findByMemberMemberNumberAndProductIdAndDataStatus(buyerNumber, productId, 1);
		    contractMap.put("reviewlist", reviewlist);
		    
		    contractList.add(contractMap);
			}

		return new ResponseEntity<>(contractList, HttpStatus.OK);
	}
	
	//구매입찰 전체 리스트
	@GetMapping("/api/get/buying/{memberNumber}")
	public ResponseEntity getBuying(@PathVariable Long memberNumber) {
		Member member = memberService.findByMemberNumber(memberNumber);
		List<Map<String, Object>> buyingList = new ArrayList<>();
		
		List<Buying> lists = buyingService.finByMemberMemberNumberAndDataStatus(member.getMemberNumber(), 1);
		for(Buying buying : lists) {
			
			// 구매입찰 취소, 거래종료 데이터는 제외
	    	if(buying.getBuyingStatus() == 61 || buying.getBuyingStatus() == 99) {
	    		continue;
	    	}
			
			Map<String, Object> buyinggMap = new HashMap<>();
			buyinggMap.put("buying", buying);
		    
		    Product product = buying.getProduct();
		    Long productId = product.getId();
		    String productName = product.getProductKorName();
		    buyinggMap.put("productName", productName);
		    
		    List<Image> imagelist = imageService.findByTargetIdAndPageDiv(productId, 1);
		    buyinggMap.put("imagelist", imagelist);
		        
		    buyingList.add(buyinggMap);
		}
		
		return new ResponseEntity<>(buyingList, HttpStatus.OK);
	}
	
	//진행중 전체 리스트
	@GetMapping("/api/get/buyinging/{buyerNumber}")
	public ResponseEntity<List<Map<String, Object>>> getbuyingIng(@PathVariable("buyerNumber") Long buyerNumber) {
		List<Map<String, Object>> contractList = new ArrayList<>();
		
		 List<Contract> lists = contractService.findByBuyerNumber(buyerNumber);
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
	
	//완료 전체 리스트
	@GetMapping("api/get/buyingend/{buyerNumber}")
	public ResponseEntity getBuyingend(@PathVariable("buyerNumber") Long buyerNumber) {
		List<Map<String, Object>> contractList = new ArrayList<>();
		
		 List<Contract> lists = contractService.findByBuyerNumber(buyerNumber);
		 List<Contract> filteredList = lists.stream()
		            .filter(contract -> contract.getBuyingStatus() >= 60)
		            .collect(Collectors.toList());

		    for (Contract contract : filteredList) {
		        Map<String, Object> contractMap = new HashMap<>();
		        contractMap.put("contract", contract);

		        Product product = contract.getProduct();
		        Long productId = product.getId();
		        contractMap.put("productId", productId);
		        String productName = product.getProductKorName();
		        contractMap.put("productName", productName);
		        
		        List<Image> imagelist = imageService.findByTargetIdAndPageDiv(productId, 1);
		        contractMap.put("imagelist", imagelist);
		        
		        List<Review> reviewlist = reviewService.findByMemberMemberNumberAndProductIdAndDataStatus(buyerNumber, productId, 1);
			    contractMap.put("reviewlist", reviewlist);

		        contractList.add(contractMap);
		    }

		    return new ResponseEntity<>(contractList, HttpStatus.OK);
		
	}
	
	//구매 입찰 취소
	@PutMapping("/api/cancel/buying/{id}")
	public ResponseEntity cancelBuying(@PathVariable Long id) {
		
		buyingService.cancel(id);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	

}
