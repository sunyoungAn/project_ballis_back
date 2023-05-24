package com.ballis.model.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyingAddDTO {
	
	private Long memberNumber; 
	private Long productId;
	private Integer productSize;
	private Integer wishPrice;
	private LocalDateTime expiryDate;

}
