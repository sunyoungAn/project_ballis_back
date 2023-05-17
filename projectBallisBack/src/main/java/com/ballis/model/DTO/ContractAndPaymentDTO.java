package com.ballis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractAndPaymentDTO {
	
	private ContractAddDTO contractDto;
	private PaymentAddDTO paymentDto;

}
