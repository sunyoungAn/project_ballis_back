package com.ballis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentAddDTO {
	
	private Long sellingId;
	private Long contractId;
	private Long memberNumber;
	   
	private String name;
	private String address;
	private String subAddress;
	private String zipCode;
	private String phoneNumber;
	private String message;
	private Integer paymentType;
	private Integer price;
	   
	private String impUid;
	private String merchantUid;

}
