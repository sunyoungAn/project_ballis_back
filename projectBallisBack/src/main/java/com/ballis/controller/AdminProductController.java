package com.ballis.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ballis.model.Product;
import com.ballis.model.admin.DTO.AdminBrandDTO;
import com.ballis.model.admin.DTO.AdminProductDetailDTO;
import com.ballis.model.admin.DTO.AdminProductListPageDTO;
import com.ballis.model.admin.DTO.AdminProductRegisterDTO;
import com.ballis.model.admin.DTO.AdminProductSearchResultDTO;
import com.ballis.service.AdminProductService;
import com.ballis.specification.ProductSpecification;

@RestController
public class AdminProductController {
	
	@Autowired
	private AdminProductService adminProductService;
	
	/*
	 * 상품리스트 - 상품관리리스트페이지에 띄울 정보 모두 가져오기
	 */
	@GetMapping("/api/admin/product/getall")
	public ResponseEntity<AdminProductListPageDTO> getProductPageInfo(Pageable pageable) {
		
		Sort sort = Sort.by("id").descending(); 
		pageable = PageRequest.of(pageable.getPageNumber(), 10, sort);
		
		Specification<Product> spec = (root, query, criteriaBuilder) -> null;
		
		spec = spec.and(ProductSpecification.equalDataStatus(1));
		
		AdminProductListPageDTO resultDto = adminProductService.getPageInfo(spec, pageable);
		
		return new ResponseEntity<AdminProductListPageDTO> (resultDto, HttpStatus.OK);
	}
	
	/*
	 * 상품검색
	 */
	@GetMapping("/api/admin/product/search")
	public ResponseEntity<AdminProductSearchResultDTO> searchProduct(@RequestParam(value="id", required=false) Long id,
													@RequestParam(value="name", required=false) String name,
													@RequestParam(value="brandId", required=false) Long brandId,
													@RequestParam(value="category", required=false) Integer category, Pageable pageable) {
		
		Sort sort = Sort.by("id").descending(); 
		pageable = PageRequest.of(pageable.getPageNumber(), 10, sort);
		
		Specification<Product> spec = (root, query, criteriaBuilder) -> null;
		
		spec = spec.and(ProductSpecification.equalDataStatus(1));
		
		// 설정한 검색 조건에 따라 유동적으로 조건설정
		if(id != null) {
			spec = spec.and(ProductSpecification.equalId(id));
		}
		
		if(name != null) {
			spec = spec.and(ProductSpecification.orLikeName(name));
		}
		
		if(brandId != null && brandId != 0) {
			spec = spec.and(ProductSpecification.equalBrandId(brandId));
		}
		
		if(category != null && category != 0) {
			spec = spec.and(ProductSpecification.equalCategory(category));
		}
		
		AdminProductSearchResultDTO resultDto = adminProductService.searchProduct(spec, pageable);
		
		return new ResponseEntity<AdminProductSearchResultDTO> (resultDto, HttpStatus.OK);
	}
	
	
	/*
	 * 상품삭제
	 */
	@PutMapping("/api/admin/product/delete")
	public ResponseEntity<Object> deleteProduct(@RequestBody List<Long> ids) {
		
		adminProductService.delete(ids);
		
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	/*
	 * 브랜드 카테고리정보 얻기
	 */
	@GetMapping("/api/admin/product/getbrand")
	public ResponseEntity<List<AdminBrandDTO>> getBrand() {
		
		List<AdminBrandDTO> dtoList = adminProductService.getBrand();
		
		return new ResponseEntity<List<AdminBrandDTO>> (dtoList, HttpStatus.OK);
	}
	
	/*
	 * 상품등록
	 */
	@PostMapping("/api/admin/product/register")
	public ResponseEntity<Object> registerProduct(@RequestPart("data") AdminProductRegisterDTO dto, @RequestPart("mainImage") MultipartFile mainImageFile, @RequestPart(value="subImage",required = false) List<MultipartFile> subImageFile) {
		
	
		// 상품등록
		Product resultProduct = adminProductService.register(dto);
		
		System.out.println("등록된 아이디 : " +resultProduct.getId());
		
		// 메인이미지등록
		adminProductService.registerImage(mainImageFile, resultProduct.getId(), 1);
				
		// 서브이미지 존재시 서브이미지 등록
		if(subImageFile != null) {
			for(MultipartFile subImage : subImageFile) {
				adminProductService.registerImage(subImage, resultProduct.getId(), 2);
			}
		}
		
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	/*
	 * 상품상세정보 가져오기
	 */
	@GetMapping("/api/admin/product/getone/{id}")
	public ResponseEntity<AdminProductDetailDTO> getProductOne(@PathVariable("id") Long id) {
		
		AdminProductDetailDTO resultDto = adminProductService.findProductOne(id);
		
		return new ResponseEntity<AdminProductDetailDTO> (resultDto, HttpStatus.OK);
	}
	
	/*
	 * 이미지 불러오기
	 */
	@GetMapping("/api/admin/product/display")
	public ResponseEntity<Resource> displayImage(@RequestParam("name") String pathName) {
		String path = "C:\\Temp\\productimage\\";
		String folder = "";
		Resource resource = new FileSystemResource(path + folder + pathName);
		if(!resource.exists()) 
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		HttpHeaders header = new HttpHeaders();
		Path filePath = null;
		try{
			filePath = Paths.get(path + folder + pathName);
			header.add("Content-type", Files.probeContentType(filePath));
		}catch(IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}
	
	/*
	 *  이미지 삭제하기
	 */
	@DeleteMapping("/api/admin/product/deleteimage/{id}")
	public ResponseEntity<Object> deleteImage(@PathVariable("id") Long id) {
		
		adminProductService.deleteImage(id);
		
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	
	/*
	 * 상품수정
	 */
	@PostMapping("/api/admin/product/edit/{id}")
	public ResponseEntity<Object> editProduct(@PathVariable("id") Long id, 
											@RequestPart("data") AdminProductRegisterDTO dto, 
											@RequestPart(value="mainImage", required = false) MultipartFile mainImageFile, 
											@RequestPart(value="subImage", required = false) List<MultipartFile> subImageFile) {
		
		// 상품수정
		adminProductService.editProduct(id, dto);
		
		// 메인이미지 존재시 메인이미지등록
		if(mainImageFile != null) {
			adminProductService.registerImage(mainImageFile, id, 1);
		}
		
		// 서브이미지 존재시 서브이미지 등록
		if(subImageFile != null) {
			for(MultipartFile subImage : subImageFile) {
				adminProductService.registerImage(subImage, id, 2);
			}
		}
		
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

}
