package com.ballis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ballis.model.Contract;
import com.ballis.model.Selling;
import com.ballis.model.Payment;
import com.ballis.model.DTO.ContractAddDTO;
import com.ballis.model.DTO.SellingAddDTO;
import com.ballis.model.DTO.ContractSellingPaymentDTO;
import com.ballis.model.DTO.PaymentAddDTO;
import com.ballis.service.ContractService;
import com.ballis.service.PaymentService;
import com.ballis.service.SellingService;

import jakarta.transaction.Transactional;

@RestController
public class PaymentController {

	@Autowired
	private PaymentService paymentService;
	@Autowired
	private ContractService contractService;
	@Autowired
	private SellingService sellingService;
	
	// 결제 완료 -> 빠른배송, 즉시구매, 보관판매
	@PostMapping("/api/add/payment")
	@Transactional
	public ResponseEntity<ContractSellingPaymentDTO> addConSellPay(@RequestBody ContractSellingPaymentDTO conSellPayDto, @RequestParam String type) {
	    try {
	    	if("fast".equals(type) || "normal".equals(type)) { // 구매-빠른배송, 즉시구매
		        ContractAddDTO contractDto = conSellPayDto.getContractDto();
		        Contract contractInfo = contractService.save(contractDto, type);        
		        sellingService.updateSelling(contractDto.getSellingId(), type);    	
	    	
		        PaymentAddDTO paymentDto = conSellPayDto.getPaymentDto(); 
		        paymentDto.setContractId(contractInfo.getId());
		        Payment paymentInfo = paymentService.addPayment(paymentDto);
		        
		        ContractSellingPaymentDTO contractAndPaymentDTO = new ContractSellingPaymentDTO();
		        contractAndPaymentDTO.setContractDto(contractDto);
		        contractAndPaymentDTO.setPaymentDto(paymentDto);
		        return new ResponseEntity<>(contractAndPaymentDTO, HttpStatus.OK);
		        
	    	} else if("keep".equals(type)) { // 판매-보관판매
		        SellingAddDTO sellingDto = conSellPayDto.getSellingDto();
		        Selling sellingInfo = sellingService.save(sellingDto, type);
	    	
		        PaymentAddDTO paymentDto = conSellPayDto.getPaymentDto(); 
		        paymentDto.setSellingId(sellingInfo.getId());
		        Payment paymentInfo = paymentService.addPayment(paymentDto);

		        ContractSellingPaymentDTO contractAndPaymentDTO = new ContractSellingPaymentDTO();
		        contractAndPaymentDTO.setSellingDto(sellingDto);
		        contractAndPaymentDTO.setPaymentDto(paymentDto);
		        return new ResponseEntity<>(contractAndPaymentDTO, HttpStatus.OK);
		        
		    } else {
		    	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		    }
	    } catch (Exception e) {
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
}