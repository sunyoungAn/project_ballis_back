package com.ballis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ballis.model.DTO.BrandListDTO;
import com.ballis.repository.BrandRepository;


@Service
public class BrandService {
	
	@Autowired
	private BrandRepository brandRepository;
	
	public List<BrandListDTO> findAllBrandListDTOs() {
		return brandRepository.findAllBrandListDTO();
	}

}
