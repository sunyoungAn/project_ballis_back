package com.ballis.model.admin.DTO;

import lombok.Data;

@Data
public class AdminCardDTO {
	
	private Long id;
	
	private String cardNumber;
	
	private String expiryYear;
	
	private String expiryMonth;
	
	private String name;

}
