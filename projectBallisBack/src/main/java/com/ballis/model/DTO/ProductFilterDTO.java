package com.ballis.model.DTO;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterDTO {

	// 필터 조건
	private List<Integer> categoryList;
	private List<Integer> genderList;
	private List<Long> brandIdList;

	private List<Integer> inventoryDivList;
	private List<Integer> sizeList;
	private List<Integer> wishPriceList;

}
