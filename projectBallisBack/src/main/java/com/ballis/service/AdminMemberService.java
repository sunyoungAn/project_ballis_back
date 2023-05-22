package com.ballis.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ballis.model.Address;
import com.ballis.model.Card;
import com.ballis.model.Member;
import com.ballis.model.admin.DTO.AdminAddressDTO;
import com.ballis.model.admin.DTO.AdminCardDTO;
import com.ballis.model.admin.DTO.AdminMemberDTO;
import com.ballis.model.admin.DTO.AdminMemberInfoDTO;
import com.ballis.repository.AddressRepository;
import com.ballis.repository.CardRepository;
import com.ballis.repository.MemberRepository;


import jakarta.transaction.Transactional;

@Service
public class AdminMemberService {
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private CardRepository cardRepository;
	
	/*
	 * 멤버리스트 - 멤버 리스트 전체 가져오기
	 */
	public Page<Member> getMemberAll(Pageable pageable) {
		return memberRepository.findAll(pageable);
	}

	/*
	 * 멤버 검색
	 */
	public Page<Member> searchMember(Specification<Member> spec, Pageable pageable) {
		return memberRepository.findAll(spec, pageable);
	}

	/*
	 * 회원 상세내용 검색
	 */
	public AdminMemberInfoDTO findById(Long memberNumber) {
		
		Member targetMember = memberRepository.findById(memberNumber).get();
		
		AdminMemberInfoDTO resultDto = new AdminMemberInfoDTO();
		resultDto.setMemberNumber(targetMember.getMemberNumber());
		resultDto.setName(targetMember.getName());
		resultDto.setEmail(targetMember.getEmail());
		resultDto.setPhoneNumber(targetMember.getPhoneNumber());
		resultDto.setGender(targetMember.getGender());
		resultDto.setBirthDate(targetMember.getBirthDate());
		resultDto.setMemberStatus(targetMember.getMemberStatus());
		resultDto.setRegistDate(targetMember.getRegistDate());
		resultDto.setBankName(targetMember.getBankName());
		resultDto.setAccountNumber(targetMember.getAccountNumber());
		resultDto.setDepositor(targetMember.getDepositor());
		
		// 주소정보
		List<AdminAddressDTO> addressDtoList = new ArrayList<>();
		List<Address> targetAddressList = targetMember.getAddresss();
		
		// 주소정보가 존재하는 경우 정렬
		if(targetAddressList.isEmpty() == false) {
			targetAddressList = targetAddressList.stream().sorted(Comparator.comparing(Address::getDefaultAddress)).collect(Collectors.toList());
		}
		
		for(Address address : targetAddressList) {
			AdminAddressDTO dto = new AdminAddressDTO();
			dto.setId(address.getId());
			dto.setName(address.getName());
			dto.setAddress(address.getAddress());
			dto.setSubAddress(address.getSubAddress());
			dto.setZipCode(address.getZipCode());
			dto.setPhoneNumber(address.getPhoneNumber());
			dto.setDefaultAddress(address.getDefaultAddress());
			addressDtoList.add(dto);
		}
		resultDto.setAddressDtoList(addressDtoList);
		
		// 카드정보
		List<AdminCardDTO> cardDtoList = new ArrayList<>();
		List<Card> targetCardList = targetMember.getCards();
		for(Card card : targetCardList) {
			if(card.getDataStatus() == 2) {
				continue;
			}
			AdminCardDTO dto = new AdminCardDTO();
			dto.setId(card.getId());
			dto.setCardNumber(card.getCardNumber());
			dto.setExpiryYear(card.getExpiryYear());
			dto.setExpiryMonth(card.getExpiryMonth());
			dto.setName(card.getName());
			cardDtoList.add(dto);
		}
		resultDto.setCardDtoList(cardDtoList);
		
		return resultDto;
	}

	/*
	 * 회원 수정 
	 */
	@Transactional
	public void edit(Long memberNumber, AdminMemberDTO dto) {
		
		// 수정할 회원 검색
		Member targetMember = memberRepository.findById(memberNumber).get();
		
		// 수정할 값 설정
		targetMember.setName(dto.getName());
		// 비밀번호는 값이 설정된 경우에만 갱신된다
		if(dto.getPassword() != null && dto.getPassword() != "") {
			targetMember.setPassword(dto.getPassword());;
		}
		targetMember.setPhoneNumber(dto.getPhoneNumber());
		targetMember.setGender(dto.getGender());
		targetMember.setBirthDate(dto.getBirthDate());
		targetMember.setMemberStatus(dto.getMemberStatus());
		targetMember.setBankName(dto.getBankName());
		targetMember.setAccountNumber(dto.getAccountNumber());
		targetMember.setDepositor(dto.getDepositor());
		targetMember.setModifiedDate(LocalDateTime.now());
		
		memberRepository.save(targetMember);
	}

	/*
	 * 주소정보 삭제
	 */
	@Transactional
	public void deleteAddress(Long id) {
		addressRepository.deleteById(id);
	}

	/*
	 * 카드정보 삭제
	 */
	@Transactional
	public void deleteCard(Long id) {
		
		Card targetCard = cardRepository.findById(id).get();
		// 데이터상태여부를 삭제(2)로 변경
		targetCard.setDataStatus(2);
		targetCard.setModifiedDate(LocalDateTime.now());
		cardRepository.save(targetCard);
	}

	/*
	 * 회원 탈퇴
	 */
	@Transactional
	public void withdrawal(List<Long> ids) {
		
		// id 갯수만큼 반복하기
		for(Long id : ids) {
			Member targetmember = memberRepository.findById(id).get();
			targetmember.setMemberStatus(3);
			targetmember.setModifiedDate(LocalDateTime.now());
			
			memberRepository.save(targetmember);
		}
	}

}
