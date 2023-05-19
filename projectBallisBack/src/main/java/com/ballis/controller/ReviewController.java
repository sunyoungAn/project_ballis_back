package com.ballis.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ballis.model.Image;
import com.ballis.model.Member;
import com.ballis.model.Product;
import com.ballis.model.Review;
import com.ballis.model.DTO.ReviewDTO;
import com.ballis.service.ImageService;
import com.ballis.service.MemberService;
import com.ballis.service.ProductService;
import com.ballis.service.ReviewService;


@RestController
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired 
	private ImageService imageService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private ProductService productService;
	
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
		
		//리뷰작성
		@PostMapping("/api/add/review/{productId}")
		public ResponseEntity addReview(@RequestParam("imagePath") MultipartFile imagePath, @RequestParam("memberNumber") Long memberNumber,
										@RequestParam("productId") Long productId, @RequestParam("content") String content,
										@RequestParam("dataStatus") Integer dataStatus, @RequestParam("pageDiv") Integer pageDiv,
										@RequestParam("targetId")Long targetId, @RequestParam("mainImageDiv") Integer mainImageDiv) throws IllegalStateException, IOException{
			
			String uuid = UUID.randomUUID().toString();
			String uploadLocation = "c:\\Temp\\upload";
			String filename = uuid + imagePath.getOriginalFilename();
			File file = new File(uploadLocation + "\\" + filename);
			imagePath.transferTo(file);

			Member member = memberService.findByMemberNumber(memberNumber);
			Product product = productService.findById(productId);
			
			Review review = new Review();
			review.setMember(member);
			review.setName(member.getName());
			review.setProduct(product);
			review.setContent(content);
			review.setDataStatus(1);
			review.setRegistDate(LocalDateTime.now());
			
			Review result = reviewService.save(review);
			
			Long targetId1 = result.getId();
			
			Image image = new Image();
			image.setImagePath(filename);
			image.setPageDiv(pageDiv);
			image.setTargetId(targetId1);
			image.setMainImageDiv(mainImageDiv);
			image.setRegistDate(LocalDateTime.now());
			
			Image result1 = imageService.save(image);
			
			return new ResponseEntity<>(Map.of("Review", result, "image", result1), HttpStatus.OK);
		}

}
