package com.ballis.model.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductBuyListDTO {
	
	private List<ProductBuyDTO> fast;
	private List<ProductBuyDTO> normal;
	private List<ProductBuyDTO> both;
	private List<ProductBuyDTO> cheaper;

}
