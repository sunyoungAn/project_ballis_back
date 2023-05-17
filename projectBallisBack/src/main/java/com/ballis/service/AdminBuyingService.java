package com.ballis.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ballis.model.Buying;
import com.ballis.model.admin.DTO.AdminBuyingInfoDTO;
import com.ballis.model.admin.DTO.AdminBuyingResponseDTO;
import com.ballis.model.admin.DTO.AdminBuyingStatusEditDTO;
import com.ballis.repository.BuyingRepository;

import jakarta.transaction.Transactional;

@Service
public class AdminBuyingService {
	
	@Autowired
	private BuyingRepository buyingRepository;
	
	/*
	 * 구매입찰정보가져오기
	 */
	public AdminBuyingResponseDTO getBuyingInfo(Specification<Buying> spec, Pageable pageable) {
		Page<Buying> targetList = buyingRepository.findAll(spec, pageable);
		
		AdminBuyingResponseDTO resultDto = new AdminBuyingResponseDTO();
		
		List<AdminBuyingInfoDTO> dtoList = new ArrayList<>();
		for(Buying target : targetList) {
			AdminBuyingInfoDTO dto = new AdminBuyingInfoDTO();
			dto.setId(target.getId());
			dto.setProductId(target.getProduct().getId());
			dto.setProductEngName(target.getProduct().getProductEngName());
			dto.setProductKorName(target.getProduct().getProductKorName());
			dto.setProductSize(target.getProductSize());
			dto.setWishPrice(target.getWishPrice());
			dto.setMemberNumber(target.getMember().getMemberNumber());
			dto.setName(target.getMember().getName());
			dto.setEmail(target.getMember().getEmail());
			dto.setBuyingStatus(target.getBuyingStatus());
			dto.setExpiryDate(target.getExpiryDate());
			dtoList.add(dto);
		}
		
		resultDto.setBuyingList(dtoList);
		resultDto.setTotal(targetList.getTotalElements());
		
		return resultDto;
	}
	
	/*
	 * 구매입찰상태 변경
	 */
	@Transactional
	public void editBuyingStatus(List<AdminBuyingStatusEditDTO> dtoList) {
		
		for(AdminBuyingStatusEditDTO dto : dtoList) {
			// 변경대상을 조회
			Buying targetBuying = buyingRepository.findById(dto.getId()).get();
			
			// 변경할 상태와 변경일을 설정
			targetBuying.setBuyingStatus(dto.getBuyingStatus());
			targetBuying.setModifiedDate(LocalDateTime.now());
			
			buyingRepository.save(targetBuying);
		}
	}
	
	/*
	 * 구매입찰 삭제
	 */
	@Transactional
	public void delete(List<Long> ids) {
		
		for(Long id : ids) {
			Buying targetBuying = buyingRepository.findById(id).get();
			targetBuying.setDataStatus(2);
			targetBuying.setModifiedDate(LocalDateTime.now());
			
			buyingRepository.save(targetBuying);
		}
	}

}
