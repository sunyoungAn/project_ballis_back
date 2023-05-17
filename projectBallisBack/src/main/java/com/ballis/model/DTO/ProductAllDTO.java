package com.ballis.model.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAllDTO {
	
	// 출력 내용
	private Long id; // p
	private String productEngName; // p
	private String productKorName; // p
	private String imagePath; // i
	private String brandName; // b
	private Integer wishPrice; // s
	private Integer inventoryDiv; // s
	private Long wishCnt; // w
	private Long reviewCnt; // r

	// 필터 조건
	private Integer category; // p
	private Integer gender; // p
	private Long brandId; // p
//	private Integer inventoryDiv; //s
	private Integer size; // s
//	private Integer wishPrice; //s

	// 정렬 조건
	private LocalDate launchingDate; // p
	private Long contractCnt; // c
//	private Long wishCnt; // w
//	private Long reviewCnt; // r

}
