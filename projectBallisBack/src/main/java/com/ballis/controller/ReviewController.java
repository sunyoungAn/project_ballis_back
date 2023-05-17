package com.ballis.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ballis.model.Image;
import com.ballis.model.Product;
import com.ballis.model.Review;
import com.ballis.model.DTO.ReviewDTO;
import com.ballis.service.ImageService;
import com.ballis.service.ReviewService;


@RestController
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired 
	private ImageService imageService;
	
	// 메인 - 전체 리뷰 출력
		@GetMapping("/api/get/review/all")
		public ResponseEntity<List<ReviewDTO>> getReviewAll() {
		    try {
		        List<ReviewDTO> reviews = reviewService.getReviewAll();
		        return new ResponseEntity<>(reviews, HttpStatus.OK);
		    } catch (Exception e) {
		        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		    }
		}
		
		// 상세 - 한 상품에 해당하는 리뷰 출력
		@GetMapping("/api/get/review/product")
		public ResponseEntity<List<ReviewDTO>> getReviewOneProduct(@RequestParam Long productid) {
		    try {
		        List<ReviewDTO> reviews = reviewService.getReviewOneProduct(productid);
		        return new ResponseEntity<>(reviews, HttpStatus.OK);
		    } catch (Exception e) {
		        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		    }
		}
		
		//리뷰 1개 데이터 수신
		@GetMapping("/api/get/review/one")
		public ResponseEntity<List<ReviewDTO>> getReviewOne(@RequestParam int reviewid) {
			
			List<Review> reviews = reviewService.findById(reviewid);
			List<Image> images = imageService.findByTargetIdAndPageDiv(reviewid,2);
			
			List<ReviewDTO> reviewDTOs = new ArrayList<>();

			for (Review review : reviews) {
				Product product = review.getProduct();
			    List<Image> matchedImages = images.stream()
			        .filter(i -> i.getTargetId() == review.getId())
			        .collect(Collectors.toList());
			    
			    for (Image image : matchedImages) {
			        ReviewDTO reviewDTO = new ReviewDTO();
			        reviewDTO.setReviewId(review.getId());
			        reviewDTO.setContent(review.getContent());
			        reviewDTO.setName(review.getName());
			        reviewDTO.setImagePath(image.getImagePath());
			        reviewDTO.setMainImageDiv(image.getMainImageDiv());
			        reviewDTO.setImageId(image.getId());
			        reviewDTO.setProductId(product.getId());	        
			        reviewDTOs.add(reviewDTO);
			    }
			}
			return new ResponseEntity<List<ReviewDTO>>(reviewDTOs,HttpStatus.OK);
		}

}
