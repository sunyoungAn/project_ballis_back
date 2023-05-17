package com.ballis.model.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SellingAddDTO {
	
	private Long memberNumber; 
	private Long productId;
	private Integer productSize;
	private Integer wishPrice;
	private LocalDateTime expiryDate;

}
