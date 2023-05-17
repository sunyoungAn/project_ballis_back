package com.ballis.model.admin.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AdminSellingPaymentDTO {
	
	private Integer paymentType;
	
	private Integer price;
	
	private LocalDateTime registDate;

}
