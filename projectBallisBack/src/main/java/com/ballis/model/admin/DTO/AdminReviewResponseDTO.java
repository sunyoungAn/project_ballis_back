package com.ballis.model.admin.DTO;

import java.util.List;

import lombok.Data;

@Data
public class AdminReviewResponseDTO {
	
	private List<AdminReviewInfoDTO> reviewList;
	
	private Long total;

}
