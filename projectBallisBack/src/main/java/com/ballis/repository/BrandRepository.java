package com.ballis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ballis.model.Brand;
import com.ballis.model.DTO.BrandListDTO;
import com.ballis.model.admin.DTO.AdminBrandDTO;

public interface BrandRepository extends JpaRepository<Brand, Long> {
	
	// 관리자기능 - 브랜드 아이디와 브랜드명 가져오기 
	@Query(value = "select " +
			"new com.ballis.model.admin.DTO.AdminBrandDTO(br.brandId, br.brandName) " +
			"from Brand br " 
			)
	List<AdminBrandDTO> findBrandCategory();
	
	@Query("SELECT new com.ballis.model.DTO.BrandListDTO(b.brandId, b.brandName) FROM Brand b")
	List<BrandListDTO> findAllBrandListDTO();

}
