package com.ballis.model.admin.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AdminProductRegisterDTO {
	
	private String productEngName;
	
	private String productKorName;
	
	private Long brandId;
	
	private Integer category;
	
	private String modelNumber;
	
	private LocalDate launchingDate;
	
	private String color;
	
	private Integer launchingPrice;
	
	private Integer sizeMin;
	
	private Integer sizeMax;
	
	private Integer sizeUnit;
	
	private Integer gender;
	
	private Integer resellTarget;
	
	private String explanation;

}
