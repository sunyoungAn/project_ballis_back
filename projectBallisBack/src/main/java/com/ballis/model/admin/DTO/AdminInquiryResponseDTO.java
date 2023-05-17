package com.ballis.model.admin.DTO;

import java.util.List;

import lombok.Data;

@Data
public class AdminInquiryResponseDTO {
	
	private List<AdminInquiryInfoDTO> inquiryList;
	
	private Long total;

}
