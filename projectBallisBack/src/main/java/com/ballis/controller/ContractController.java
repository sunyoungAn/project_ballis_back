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

import com.ballis.model.Contract;
import com.ballis.model.DTO.ContractAddDTO;
import com.ballis.model.DTO.ContractChartDTO;
import com.ballis.service.BuyingService;
import com.ballis.service.ContractService;
import com.ballis.service.SellingService;


@RestController
public class ContractController {
	
	@Autowired
	private ContractService contractService;
	@Autowired
	private SellingService sellingService;
	@Autowired
	private BuyingService buyingService;
	
	// 체결거래 차트
		@GetMapping("/api/get/contract/chart")
		public ResponseEntity<List<ContractChartDTO>> getProductOne(@RequestParam Long productid) {
		    try {
		        List<ContractChartDTO> lists = contractService.getContractChart(productid);
		        return new ResponseEntity<>(lists, HttpStatus.OK);
		    } catch (Exception e) {
		        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		    }
		}
		
		// 판매입찰(빠른배송, 즉시구매) 거래 체결
		@PostMapping("/api/post/contract/sell")
		public ResponseEntity<?> addContractSell (@RequestBody ContractAddDTO contractDto, @RequestParam String type) {
			try {
				Contract contractInfo = contractService.save(contractDto, type);
				sellingService.updateSelling(contractDto.getSellingId(), type);
				return new ResponseEntity<Contract>(contractInfo, HttpStatus.OK);		
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// 구매입찰(즉시판매) 거래 체결
		@PostMapping("/api/post/contract/buy")
		public ResponseEntity<?> addContractBuy (@RequestBody ContractAddDTO contractDto, @RequestParam String type) {
			try {
				Contract contractInfo = contractService.save(contractDto, type);
				buyingService.updateBuying(contractDto.getBuyingId());
				return new ResponseEntity<Contract>(contractInfo, HttpStatus.OK);		
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

}
