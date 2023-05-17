package com.ballis.model.admin.DTO;

import java.util.HashMap;

import org.springframework.data.domain.Page;

import com.ballis.model.Product;

import lombok.Data;

@Data
public class AdminProductSearchResultDTO {
	
	private Page<Product> productList;
	
	private HashMap<Long, String> productBrandMap;

}
