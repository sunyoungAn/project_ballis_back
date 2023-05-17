package com.ballis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductNewDTO {
	
	private Long id;
	private String productEngName;
	private String imagePath;
	private String brandName;
	private Integer wishPrice;
//	private Boolean hasStorageProduct;

}
