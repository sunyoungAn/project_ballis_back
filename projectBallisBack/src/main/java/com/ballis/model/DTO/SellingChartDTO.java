package com.ballis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellingChartDTO {
	
	private Long id;
	private Integer productSize;
	private Integer wishPrice;
	private Integer cnt;

}
