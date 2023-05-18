package com.ballis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractSellingPaymentDTO {
	
	private ContractAddDTO contractDto;
	private SellingAddDTO sellingDto;
	private PaymentAddDTO paymentDto;

}
