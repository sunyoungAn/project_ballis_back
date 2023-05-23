package com.ballis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ballis.model.Review;
import com.ballis.model.DTO.ReviewDTO;

public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {
	
	@Query(value = "SELECT "
			+ "new com.ballis.model.DTO.ReviewDTO" 
			+ "(r.id, r.content, r.name, i.imagePath, i.mainImageDiv, i.id, p.id) "
			+ "FROM Review r "
			+ "INNER JOIN Image i ON r.id = i.targetId "
			+ "INNER JOIN Product p ON r.product = p.id "
			+ "WHERE r.dataStatus = 1 AND i.pageDiv = 2 "
			+ "ORDER BY r.id DESC",
			nativeQuery = false) 
	List<ReviewDTO> getReviewAll();

	@Query(value = "SELECT "
			+ "new com.ballis.model.DTO.ReviewDTO" 
			+ "(r.id, r.content, r.name, i.imagePath, i.mainImageDiv, i.id, p.id) "
			+ "FROM Review r "
			+ "INNER JOIN Image i ON r.id = i.targetId "
			+ "INNER JOIN Product p ON r.product = p.id "
			+ "WHERE r.dataStatus = 1 AND i.pageDiv = 2 AND p.id = :productid "
			+ "ORDER BY r.id DESC",
			nativeQuery = false) 
	List<ReviewDTO> getReviewOneProduct(@Param("productid") Long producid);
	
	// 변수로 받은 reviewId와 가까우면서 큰 revieId 찾기
//	Review findFirstByIdGreaterThanOrderByIdAsc(Long reviewId);
	Review findFirstByDataStatusAndIdGreaterThanOrderByIdAsc(Integer dataStatus, Long reviewId);
	
//	Review findFirstByProductIdAndIdGreaterThanOrderByIdAsc(Long productId, Long reviewId);
	Review findFirstByDataStatusAndProductIdAndIdGreaterThanOrderByIdAsc(Integer dataStatus, Long productId, Long reviewId);
	
	// 변수로 받은 reviewId와 가까우면서 작은 revieId 찾기
//	Review findFirstByIdLessThanOrderByIdDesc(Long reviewId);
	Review findFirstByDataStatusAndIdLessThanOrderByIdDesc(Integer dataStatus, Long reviewId);
	
//	Review findFirstByProductIdAndIdLessThanOrderByIdDesc(Long productid, Long reviewid);
	Review findFirstByDataStatusAndProductIdAndIdLessThanOrderByIdDesc(Integer dataStatus, Long productid, Long reviewid);

	List<Review> findByProductId(Long productId);

}
