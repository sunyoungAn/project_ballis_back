package com.ballis.model.admin.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AdminContractPaymentDTO {
	
	private Integer paymentType;
	
	private Integer price;
	
	private LocalDateTime registDate;
	
	private String name;
	
	private String address;
	
	private String subAddress;
	
	private String zipCode;

	private String phoneNumber;
	
	private String message;

}
