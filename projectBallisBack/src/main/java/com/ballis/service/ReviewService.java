package com.ballis.service;

import java.util.List;
import java.util.Optional;

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

	public Optional<Review> findById(Long reviewid) {
		return reviewRepository.findById(reviewid);
	}

	public Review save(Review review) {
		return reviewRepository.save(review);
	}
	
	// 왼쪽 리뷰 선택
	public Review getLeftReview(Long reviewid) {
	  return reviewRepository.findFirstByDataStatusAndIdGreaterThanOrderByIdAsc(1, reviewid);
	}
	
    public Review getLeftReviewProduct(Long reviewid, Long productid) {
    	return reviewRepository.findFirstByDataStatusAndProductIdAndIdGreaterThanOrderByIdAsc(1, productid, reviewid);
    }
    
    // 오른쪽 리뷰 선택
    public Review getRightReview(Long reviewid) {
        return reviewRepository.findFirstByDataStatusAndIdLessThanOrderByIdDesc(1, reviewid);
    }
    
    public Review getRightReviewProduct(Long reviewid, Long productid) {
    	return reviewRepository.findFirstByDataStatusAndProductIdAndIdLessThanOrderByIdDesc(1, productid, reviewid);
    }

    
	public List<Review> findByProductId(Long productId) {
		return reviewRepository.findByProductId(productId);
	}

	public void delete(Long id) {
		reviewRepository.deleteById(id);
		
	}

}
