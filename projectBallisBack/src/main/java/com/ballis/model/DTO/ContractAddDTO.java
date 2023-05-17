package com.ballis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractAddDTO {
	
	private Long productId; // fk
	private Long buyingId; // nullable fk 판매시
	private Long sellingId; // nullable fk 구매시
	private Long buyerNumber; // 구매시(나) 판매시(상대방)
	private Long sellerNumber; // 구매시(상대방) 판매시(나)
	private Integer price;
	private Integer productSize;

}
