package com.ballis.specification;

import org.springframework.data.jpa.domain.Specification;

import com.ballis.model.Review;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ReviewSpecification {
	
	// 인덱스와 일치
	public static Specification<Review> equalId(Long id) {
		return new Specification<Review>() {
			@Override
			public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("id"), id);
			}
		};
	}
	
	// 상품아이디와 일치
	public static Specification<Review> equalProductId(Long productId) {
		return new Specification<Review>() {
			@Override
			public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("product").get("id"), productId);
			}
		};
	}
	
	// 회원번호와 일치
	public static Specification<Review> equalMemberNumber(Long memberNumber) {
		return new Specification<Review>() {
			@Override
			public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("member").get("memberNumber"), memberNumber);
			}
		};
	}
	
	// like 리뷰내용
	public static Specification<Review> likeContent(String content) {
		return new Specification<Review>() {
			@Override
			public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.like(root.get("content"), "%" + content + "%");
			}
		};
	}
	
	// 데이터상태여부와 일치
	public static Specification<Review> equalDataStatus(Integer status) {
		return new Specification<Review>() {
			@Override
			public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("dataStatus"), status);
			}
		};
	}

}
