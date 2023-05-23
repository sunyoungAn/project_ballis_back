package com.ballis.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ballis.model.Payment;
import com.ballis.model.Selling;
import com.ballis.model.admin.DTO.AdminSellingInfoDTO;
import com.ballis.model.admin.DTO.AdminSellingPaymentDTO;
import com.ballis.model.admin.DTO.AdminSellingResponseDTO;
import com.ballis.model.admin.DTO.AdminSellingStatusEditDTO;
import com.ballis.repository.PaymentRepository;
import com.ballis.repository.SellingRepository;

import jakarta.transaction.Transactional;

@Service
public class AdminSellingService {
	
	@Autowired
	private SellingRepository sellingRepository;
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	/*
	 * 판매입찰정보가져오기
	 */
	public AdminSellingResponseDTO getSellingInfo(Specification<Selling> spec, Pageable pageable) {
		Page<Selling> targetList = sellingRepository.findAll(spec, pageable);
		
		AdminSellingResponseDTO resultDto = new AdminSellingResponseDTO();
		
		List<AdminSellingInfoDTO> dtoList = new ArrayList<>();
		for(Selling target : targetList) {
			AdminSellingInfoDTO dto = new AdminSellingInfoDTO();
			dto.setId(target.getId());
			dto.setProductId(target.getProduct().getId());
			dto.setProductEngName(target.getProduct().getProductEngName());
			dto.setProductKorName(target.getProduct().getProductKorName());
			dto.setProductSize(target.getProductSize());
			dto.setWishPrice(target.getWishPrice());
			dto.setMemberNumber(target.getMember().getMemberNumber());
			dto.setName(target.getMember().getName());
			dto.setEmail(target.getMember().getEmail());
			dto.setSellingStatus(target.getSellingStatus());
			dto.setExpiryDate(target.getExpiryDate());
			dtoList.add(dto);
		}
		
		resultDto.setSellingList(dtoList);
		resultDto.setTotal(targetList.getTotalElements());
		
		return resultDto;
	}

	/*
	 * 판매입찰상태 변경
	 */
	@Transactional
	public void editSellingStatus(List<AdminSellingStatusEditDTO> dtoList) {
		
		for(AdminSellingStatusEditDTO dto : dtoList) {
			// 변경대상을 조회
			Selling targetSelling = sellingRepository.findById(dto.getId()).get();
			
			// 변경할 상태와 변경일을 설정
			targetSelling.setSellingStatus(dto.getSellingStatus());
			targetSelling.setModifiedDate(LocalDateTime.now());
			
			sellingRepository.save(targetSelling);
		}
		
	}
	
	/*
	 * 판매입찰 삭제
	 */
	@Transactional
	public void delete(List<Long> ids) {
		
		for(Long id : ids) {
			Selling targetSelling = sellingRepository.findById(id).get();
			targetSelling.setDataStatus(2);
			targetSelling.setModifiedDate(LocalDateTime.now());
			
			sellingRepository.save(targetSelling);
		}
	}

	/*
	 * 보관판매결제정보 가져오기
	 */
	public AdminSellingPaymentDTO getPayment(Long targetSellingId) {
		
		List<Payment> targetPaymentList = paymentRepository.findBySelling_Id(targetSellingId);
		
		// 리팩토링 대상 : 보관판매타겟 아이디가 들어 있는 대상이 2개인 문제가 있어서 일단 이렇게 회피
		AdminSellingPaymentDTO dto = new AdminSellingPaymentDTO();
		for(Payment target : targetPaymentList) {
			if(target.getContract() != null) {
				// 거래 체결 아이디가 있으면 제외
				continue;
			}
			dto.setPaymentType(target.getPaymentType());
			dto.setPrice(target.getPrice());
			dto.setRegistDate(target.getRegistDate());
		}

		return dto;
	}

}
