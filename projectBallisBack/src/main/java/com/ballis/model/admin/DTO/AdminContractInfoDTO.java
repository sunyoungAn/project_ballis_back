package com.ballis.model.admin.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AdminContractInfoDTO {
	
	private Long id;
	
	private Long productId;
	
	private String productEngName;
	
	private String productKorName;
	
	private Long targetBuyingId;
	
	private Long targetSellingId;
	
	private Integer productSize;
	
	private Integer price;
	
	private Long buyerNumber;
	
	private String buyerName;
	
	private String buyerEmail;
	
	private Long sellerNumber;
	
	private String sellerName;
	
	private String sellerEmail;
	
	private LocalDateTime contractDate;
	
	private Integer sellingStatus;
	
	private Integer buyingStatus;
	
	private LocalDateTime paidDate;
	
	private Integer paidPrice;
	
	private Integer contractDiv;

}
