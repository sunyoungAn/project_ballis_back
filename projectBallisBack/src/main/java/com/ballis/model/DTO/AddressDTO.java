package com.ballis.model.DTO;

import java.time.LocalDateTime;

import com.ballis.model.Member;

import lombok.Data;

@Data
public class AddressDTO {
	
//	private Member member;
	private String name;
	private String address;
	private String subAddress;
	private String zipCode;
	private String phoneNumber;
	private Integer defaultAddress;
	private LocalDateTime registDate = LocalDateTime.now();

}
