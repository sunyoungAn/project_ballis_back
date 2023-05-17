package com.ballis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ballis.model.Review;
import com.ballis.model.DTO.ReviewDTO;
import com.ballis.repository.ReviewRepository;


@Service
public class ReviewService {
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	public List<ReviewDTO> getReviewAll() {
		return reviewRepository.getReviewAll();
	}
	
	public List<ReviewDTO> getReviewOneProduct (Long productid) {
		return reviewRepository.getReviewOneProduct(productid);
	}

	public List<Review> findById(int reviewid) {
		return reviewRepository.findById(reviewid);
	}

}
