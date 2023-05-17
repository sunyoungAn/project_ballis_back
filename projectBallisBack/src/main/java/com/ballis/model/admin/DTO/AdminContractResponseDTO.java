package com.ballis.model.admin.DTO;

import java.util.List;

import lombok.Data;

@Data
public class AdminContractResponseDTO {
	
	private List<AdminContractInfoDTO> contractList;
	
	private Long total;

}
