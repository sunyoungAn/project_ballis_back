package com.ballis.model.admin.DTO;

import lombok.Data;

@Data
public class AdminImageDTO {
	
	private Long id;
	
	private Integer pageDiv;
	
	private Long targetId;
	
	private String imagePath;
	
	private Integer mainImageDiv;

}
