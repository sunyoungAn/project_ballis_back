package com.ballis.model.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOneDTO {
	
	private Long id;
	private String productEngName;
	private String productKorName;
	private String modelNumber;
	private String color;
	private LocalDate launchingDate;
	private Integer launchingPrice;
	private Integer sizeMin;
	private Integer sizeMax;
	private Integer sizeUnit;
	
	private String imagePath;
	
	private String brandName;

	private Integer buyWishPrice;
	
	private Integer sellWishPrice;
	private Integer inventoryDiv;
	
	private Integer price;

}
