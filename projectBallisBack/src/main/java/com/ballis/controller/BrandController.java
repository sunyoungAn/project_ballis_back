package com.ballis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ballis.model.DTO.BrandListDTO;
import com.ballis.service.BrandService;


@RestController
public class BrandController {
	
	@Autowired
	private BrandService brandService;
	
	@GetMapping("/api/get/brand/list")
	public ResponseEntity<List<BrandListDTO>> findAllBrandList() {
		try{ 
			List<BrandListDTO> lists = brandService.findAllBrandListDTOs();
			return new ResponseEntity<>(lists, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
