package com.ballis.model.admin.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AdminSellingInfoDTO {
	
	private Long id;
	
	private Long productId;
	
	private String productEngName;
	
	private String productKorName;
	
	private Integer productSize;
	
	private Integer wishPrice;
	
	private Long memberNumber;
	
	private String name;
	
	private String email;
	
	private Integer sellingStatus;
	
	private LocalDateTime expiryDate;

}
