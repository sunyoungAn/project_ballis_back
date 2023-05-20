package com.ballis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductSellDTO {
	
	private Long id;
	private String productEngName;
	private String productKorName;
	private String modelNumber;
	
	private String imagePath;

	private Integer buyingStatus;
	private Integer buyProductSize;
	private Integer buyWishPrice;
	private Long buyerNumber;
	private Long buyingId;

}
