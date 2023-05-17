package com.ballis.model.DTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractChartDTO {
	
	private Long contractId;
	private Integer price; 
	private String contractDate; 
	private Integer sellSize; 
	private Integer buySize;


    public ContractChartDTO(Long contractId, Integer price, LocalDateTime contractDate, Integer sellSize, Integer buySize) {
        this.contractId = contractId;
        this.price = price;
        this.contractDate = contractDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        this.sellSize = sellSize;
        this.buySize = buySize;
    }

}
