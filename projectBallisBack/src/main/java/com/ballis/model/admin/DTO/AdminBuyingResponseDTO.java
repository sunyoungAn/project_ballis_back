package com.ballis.model.admin.DTO;

import java.util.List;

import lombok.Data;

@Data
public class AdminBuyingResponseDTO {
	
	// 구매입찰정보
	private List<AdminBuyingInfoDTO> buyingList;
	
	// 총갯수
	private Long total;

}
