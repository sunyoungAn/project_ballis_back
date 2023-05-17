package com.ballis.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ballis.model.Contract;
import com.ballis.model.Member;
import com.ballis.model.Payment;
import com.ballis.model.admin.DTO.AdminContractInfoDTO;
import com.ballis.model.admin.DTO.AdminContractPaymentDTO;
import com.ballis.model.admin.DTO.AdminContractResponseDTO;
import com.ballis.model.admin.DTO.AdminContractStatusEditDTO;
import com.ballis.repository.ContractRepository;
import com.ballis.repository.MemberRepository;
import com.ballis.repository.PaymentRepository;


import jakarta.transaction.Transactional;


@Service
public class AdminContractService {
	
	@Autowired
	private ContractRepository contractRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	/*
	 * 거래정보가져오기
	 */
	public AdminContractResponseDTO getContractInfo(Specification<Contract> spec, Pageable pageable) {

		Page<Contract> targetList = contractRepository.findAll(spec, pageable);
		
		AdminContractResponseDTO resultDto = new AdminContractResponseDTO();
		
		List<AdminContractInfoDTO> dtoList = new ArrayList<>();
		// 리스트 갯수반큼 반복하면서 화면에 필요한 정보만 DTO에 설정
		for(Contract target : targetList) {
			// 구매자정보 취득
			Member buyerData = memberRepository.findById(target.getBuyerNumber()).get();
			
			AdminContractInfoDTO dto = new AdminContractInfoDTO();
			dto.setId(target.getId());
			dto.setProductId(target.getProduct().getId());
			dto.setProductEngName(target.getProduct().getProductEngName());
			dto.setProductKorName(target.getProduct().getProductKorName());
			
			if (target.getBuying() != null) {
				// 구매입찰 정보가 있는 경우
				
				dto.setTargetBuyingId(target.getBuying().getId());
				// 거래구분 - 입찰거래(2) 설정
				dto.setContractDiv(2);
				
			} else if(target.getSelling() != null) {
				// 판매입찰 정보가 있는 경우
				
				dto.setTargetSellingId(target.getSelling().getId());
				
				
				if(target.getSelling().getInventoryDiv() == 1) {
					// 보관판매인 경우
					dto.setContractDiv(1); // 거래구분 - 보관판매(1) 설정
				} else {
					dto.setContractDiv(2); // 거래구분 - 입찰거래(2) 설정
		
				}
			} else {
				// 판매입찰, 구매입찰 정보가 없는 경우 - 브랜드 배송
				dto.setContractDiv(3);	// 거래구분 - 브랜드배송(3) 설정		
			}
			
			dto.setPrice(target.getPrice());
			dto.setProductSize(target.getProductSize());
			dto.setBuyerNumber(target.getBuyerNumber());
			dto.setBuyerName(buyerData.getName());
			dto.setBuyerEmail(buyerData.getEmail());
			if(target.getSellerNumber() != null) {
				// 판매자정보 취득
				Member sellerData = memberRepository.findById(target.getSellerNumber()).get();
				dto.setSellerNumber(target.getSellerNumber());
				dto.setSellerName(sellerData.getName());
				dto.setSellerEmail(sellerData.getEmail());
			};
			dto.setContractDate(target.getContractDate());
			if(target.getSellingStatus() == null) {
				dto.setSellingStatus(0);
			} else {
				dto.setSellingStatus(target.getSellingStatus());
			}
			dto.setBuyingStatus(target.getBuyingStatus());
			dto.setPaidPrice(target.getPaidPrice());
			dto.setPaidDate(target.getPaidDate());
			
			// DTO를 DTO리스트에 추가
			dtoList.add(dto);
		}
		
		resultDto.setContractList(dtoList);
		resultDto.setTotal(targetList.getTotalElements());
		
		return resultDto;
	}

	/*
	 * 결제정보 가져오기
	 */
	public AdminContractPaymentDTO getPayment(Long targetContractId) {
		
		Payment targetPayment = paymentRepository.findByContract_Id(targetContractId);
		
		AdminContractPaymentDTO dto = new AdminContractPaymentDTO();
		dto.setPaymentType(targetPayment.getPaymentType());
		dto.setPrice(targetPayment.getPrice());
		dto.setRegistDate(targetPayment.getRegistDate());
		dto.setName(targetPayment.getName());
		dto.setAddress(targetPayment.getAddress());
		dto.setSubAddress(targetPayment.getSubAddress());
		dto.setZipCode(targetPayment.getZipCode());
		dto.setPhoneNumber(targetPayment.getPhoneNumber());
		dto.setMessage(targetPayment.getMessage());
		
		return dto;
	}

	/*
	 * 거래정보의 판매, 구매 상태변경
	 */
	@Transactional
	public void editContractStatus(List<AdminContractStatusEditDTO> dtoList) {

		for(AdminContractStatusEditDTO dto : dtoList) {
			// 변경대상을 조회
			Contract targetContract = contractRepository.findById(dto.getId()).get();
			
			if(dto.getSellingStatus() != 0) {
				// 변경될 판매상태정보가 있는 경우에만 판매상태정보 수정
				targetContract.setSellingStatus(dto.getSellingStatus());
				
				if(dto.getSellingStatus() == 50) {
					// 정산완료인 경우에는 정산금액과 정산일도 설정
					targetContract.setPaidDate(LocalDateTime.now());
					targetContract.setPaidPrice((int)(targetContract.getPrice() * 0.96));
					;
				}
			}
			
			targetContract.setBuyingStatus(dto.getBuyingStatus());
			targetContract.setModifiedDate(LocalDateTime.now());
			
			contractRepository.save(targetContract);		
			}
		
	}

}
