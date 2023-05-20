package com.ballis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductBuyDTO {
	
	private Long id;
	private String productEngName;
	private String productKorName;
	private String modelNumber;
	
	private String imagePath;
	
	private Integer sellingStatus;
	private Integer sellProductSize;
	private Integer sellWishPrice;
	private Integer inventoryDiv;
	private Long sellerNumber;
	private Long sellingId;

}
