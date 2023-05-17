package com.ballis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ballis.model.Review;
import com.ballis.model.admin.DTO.AdminReviewResponseDTO;
import com.ballis.service.AdminReviewService;
import com.ballis.specification.ReviewSpecification;

@RestController
public class AdminReviewController {
	
	@Autowired
	private AdminReviewService adminReviewService;
	
	/*
	 *  리뷰리스트 - 리뷰관리페이지에 띄울 정보 모두 가져오기
	 */
	@GetMapping("/api/admin/review/getall")
	public ResponseEntity<AdminReviewResponseDTO> getReviewAll(Pageable pageable) {
		
		Sort sort = Sort.by("id").descending(); 
		pageable = PageRequest.of(pageable.getPageNumber(), 10, sort);
		
		Specification<Review> spec = (root, query, criteriaBuilder) -> null;
		
		spec = spec.and(ReviewSpecification.equalDataStatus(1));
		
		AdminReviewResponseDTO responseDto = adminReviewService.getReviewInfo(spec, pageable);
		
		return new ResponseEntity<AdminReviewResponseDTO>(responseDto, HttpStatus.OK);
	}
	
	/*
	 * 리뷰검색
	 */
	@GetMapping("/api/admin/review/search")
	public ResponseEntity<AdminReviewResponseDTO> searchReview(@RequestParam(value="id", required=false) Long id,
											@RequestParam(value="memberNumber", required=false) Long memberNumber,
											@RequestParam(value="productId", required=false) Long productId,
											@RequestParam(value="content", required=false) String content, Pageable pageable) {
		
		Sort sort = Sort.by("id").descending(); 
		pageable = PageRequest.of(pageable.getPageNumber(), 10, sort);
		
		Specification<Review> spec = (root, query, criteriaBuilder) -> null;
		
		spec = spec.and(ReviewSpecification.equalDataStatus(1));
		
		// 설정한 검색 조건에 따라 유동적으로 조건설정
		if(id != null) {
			spec = spec.and(ReviewSpecification.equalId(id));
		}
		
		if(memberNumber != null) {
			spec = spec.and(ReviewSpecification.equalMemberNumber(memberNumber));
		}
		
		if(productId != null) {
			spec = spec.and(ReviewSpecification.equalProductId(productId));
		}
		
		if(content != null && content != "") {
			spec = spec.and(ReviewSpecification.likeContent(content));
		}
		
		AdminReviewResponseDTO responseDto = adminReviewService.getReviewInfo(spec, pageable);
		
		return new ResponseEntity<AdminReviewResponseDTO>(responseDto, HttpStatus.OK);
	}
	
	/*
	 * 리뷰 삭제
	 */
	@PutMapping("/api/admin/review/delete")
	public ResponseEntity<Object> deleteReview(@RequestBody List<Long> ids) {
		
		adminReviewService.delete(ids);
		
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

}
