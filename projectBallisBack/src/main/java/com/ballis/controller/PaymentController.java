package com.ballis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ballis.model.Contract;
import com.ballis.model.Payment;
import com.ballis.model.DTO.ContractAddDTO;
import com.ballis.model.DTO.ContractAndPaymentDTO;
import com.ballis.model.DTO.PaymentAddDTO;
import com.ballis.service.BuyingService;
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
	@Autowired
	private BuyingService buyingService;
	
	// 결제 완료
		@PostMapping("/api/add/payment")
	    public ResponseEntity<Payment> addPayment(@RequestBody PaymentAddDTO paymentDto) {
	        try {
	            Payment paymentInfo = paymentService.addPayment(paymentDto);
	            return new ResponseEntity<>(paymentInfo, HttpStatus.OK);
	        } catch (Exception e) {
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
		
		// 거래 체결 후 결제 완료
		@PostMapping("/api/add/payment/test")
		@Transactional
		public ResponseEntity<ContractAndPaymentDTO> addConAndPay(@RequestBody ContractAndPaymentDTO conAndPayDto, @RequestParam String type) {
		    try {
		        ContractAddDTO contractDto = conAndPayDto.getContractDto();
		        Contract contractInfo = contractService.save(contractDto, type);        
		        sellingService.updateSelling(contractDto.getSellingId(), type); 
		        
		        PaymentAddDTO paymentDto = conAndPayDto.getPaymentDto(); 
		        paymentDto.setContractId(contractInfo.getId());
		        Payment paymentInfo = paymentService.addPayment(paymentDto);
		        
		        ContractAndPaymentDTO contractAndPaymentDTO = new ContractAndPaymentDTO();
		        contractAndPaymentDTO.setContractDto(contractDto);
		        contractAndPaymentDTO.setPaymentDto(paymentDto);

		        return new ResponseEntity<>(contractAndPaymentDTO, HttpStatus.OK);		
		    } catch (Exception e) {
		        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		    }
		}

}
