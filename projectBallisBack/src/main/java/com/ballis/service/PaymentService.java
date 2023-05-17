package com.ballis.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ballis.model.Contract;
import com.ballis.model.Member;
import com.ballis.model.Payment;
import com.ballis.model.Selling;
import com.ballis.model.DTO.PaymentAddDTO;
import com.ballis.repository.ContractRepository;
import com.ballis.repository.MemberRepository;
import com.ballis.repository.PaymentRepository;
import com.ballis.repository.SellingRepository;

import jakarta.transaction.Transactional;



@Service
public class PaymentService {
	
	@Autowired
	private PaymentRepository paymentRepository;
	@Autowired
	private SellingRepository sellingRepository;
	@Autowired
	private ContractRepository contractRepository;
	@Autowired
	private MemberRepository memberRepository;
	
	// 결제 완료
		@Transactional
	    public Payment addPayment(PaymentAddDTO paymentDto) {
	        Payment payment = new Payment();
	        
	        if(paymentDto.getSellingId() != null) {
	        	Optional<Selling> selling = sellingRepository.findById(paymentDto.getSellingId());
	        	if(selling.isPresent()) {
	        		payment.setSelling(selling.get());          		
	        	}   	
	        }
	        		
	        if(paymentDto.getContractId() != null) {
	        	Optional<Contract> contract = contractRepository.findById(paymentDto.getContractId());
	        	if(contract.isPresent()) {
	        		payment.setContract(contract.get());	
	        	}        	
	        }
	        
	        Member member = memberRepository.findById(paymentDto.getMemberNumber())
	        		.orElseThrow(()->new IllegalArgumentException("memeber number가 없습니다."));
	        payment.setMember(member);
	        
	        payment.setName(paymentDto.getName());
	        payment.setAddress(paymentDto.getAddress());
	        payment.setSubAddress(paymentDto.getSubAddress());
	        payment.setZipCode(paymentDto.getZipCode());
	        payment.setPhoneNumber(paymentDto.getPhoneNumber());
	        payment.setMessage(paymentDto.getMessage());
	        payment.setPaymentType(paymentDto.getPaymentType());
	        payment.setPrice(paymentDto.getPrice());
	        payment.setRegistDate(LocalDateTime.now());
	        payment.setImpUid(paymentDto.getImpUid());
	        payment.setMerchantUid(paymentDto.getMerchantUid());       
	        
	        return paymentRepository.save(payment);
	    }

}
