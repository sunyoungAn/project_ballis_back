package com.ballis.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.ballis.model.Address;
import com.ballis.model.Member;
import com.ballis.model.DTO.AddressDTO;
import com.ballis.repository.MemberRepository;
import com.ballis.service.AddressService;
import com.ballis.service.MemberService;

import jakarta.transaction.Transactional;


@RestController
public class AddressController {
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private MemberService memberService;
	
	// 주소 조회
	@GetMapping("/api/get/address")
	public ResponseEntity<List<Address>> getAddress(@RequestParam Long member) {
		try {
			List<Address> lists = addressService.getAddress(member);
			return new ResponseEntity<>(lists, HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/api/add/address/{memberNumber}")
	public ResponseEntity addAddress(@PathVariable Long memberNumber, @RequestBody AddressDTO dto) {
		Member member = memberService.findByMemberNumber(memberNumber);
		
		int count = addressService.countByMemberNumber(memberNumber);
		
		if(count >=3) {
			 return new ResponseEntity<>("주소는 3개까지만 등록 가능합니다.", HttpStatus.BAD_REQUEST);
		};
		
		
		String name = dto.getName();
		String address = dto.getAddress();
		String subAddress = dto.getSubAddress();
		String zipCode = dto.getZipCode();
		String phoneNumber = dto.getPhoneNumber();
		Integer defaultAddress = dto.getDefaultAddress();
		LocalDateTime registDate = dto.getRegistDate();
		
		Address newAddress = Address.builder()
						.member(member)
						.name(name)
						.address(address)
						.subAddress(subAddress)
						.zipCode(zipCode)
						.phoneNumber(phoneNumber)
						.defaultAddress(defaultAddress)
						.registDate(registDate)
						.build();
		
		Address result = addressService.save(newAddress);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping("/api/get/address/{memberNumber}") // TODO 위의 메소드와 하나로 합치기
	public ResponseEntity getMyAddress(@PathVariable("memberNumber") Long memberNumber) {
		Member member = memberService.findByMemberNumber(memberNumber);
		List<Address> lists = addressService.findByMemberMemberNumber(member.getMemberNumber());
		
		return new ResponseEntity<>(lists, HttpStatus.OK);
	}
	
	//기본배송지변경
	@PutMapping("/api/update/address/{memberNumber}/{id}")
	public ResponseEntity updateAddress(@PathVariable("memberNumber") Long memberNumber, @PathVariable("id") Long id) {
		
		Member member =  memberService.findByMemberNumber(memberNumber);
	    Address address = addressService.findById(id);

	    if (member == null || address == null) {
	        return ResponseEntity.notFound().build();
	    }

	    // 현재 기본 배송지를 해제
	    Address currentDefaultAddress = addressService.findByMemberMemberNumberAndDefaultAddress(member.getMemberNumber(), 1);
	    if (currentDefaultAddress != null) {
	        currentDefaultAddress.setDefaultAddress(2);
	        addressService.save(currentDefaultAddress);
	    }

	    // 선택한 주소를 기본 배송지로 설정
	    address.setDefaultAddress(1);
	    addressService.save(address);

	    return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//주소 삭제
	@DeleteMapping("/api/delete/address/{id}")
	public ResponseEntity deleteAddress(@PathVariable Long id) {
		addressService.delete(id);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
