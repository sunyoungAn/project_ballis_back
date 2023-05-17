package com.ballis.model.admin.DTO;

import lombok.Data;

@Data
public class AdminAddressDTO {

	private Long id;
	
	private String name;
	
	private String address;
	
	private String subAddress;
	
	private String zipCode;
	
	private String phoneNumber;
	
	private Integer defaultAddress;
	
}
