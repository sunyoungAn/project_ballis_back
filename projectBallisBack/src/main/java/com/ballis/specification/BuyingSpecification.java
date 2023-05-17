package com.ballis.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.ballis.model.Buying;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class BuyingSpecification {
	
	// 데이터상태여부와 일치
	public static Specification<Buying> equalDataStatus(Integer status) {
		return new Specification<Buying>() {
			@Override
			public Predicate toPredicate(Root<Buying> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("dataStatus"), status);
			}
		};
	}
	
	// 인덱스와 일치
	public static Specification<Buying> equalId(Long id) {
		return new Specification<Buying>() {
			@Override
			public Predicate toPredicate(Root<Buying> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("id"), id);
			}
		};
	}
	
	// 상품아이디와 일치
	public static Specification<Buying> equalProductId(Long productId) {
		return new Specification<Buying>() {
			@Override
			public Predicate toPredicate(Root<Buying> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("product").get("id"), productId);
			}
		};
	}
	
	// 회원번호와 일치
	public static Specification<Buying> equalMemberNumber(Long memberNumber) {
		return new Specification<Buying>() {
			@Override
			public Predicate toPredicate(Root<Buying> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("member").get("memberNumber"), memberNumber);
			}
		};
	}
	
	// 구매상태와 일치
	public static Specification<Buying> equalBuyingStatus(Integer buyingStatus) {
		return new Specification<Buying>() {
			@Override
			public Predicate toPredicate(Root<Buying> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("buyingStatus"), buyingStatus);
			}
		};
	}
	
	// 만료일(검색기준시작)보다 크거나 같다
	public static Specification<Buying> greaterThanOrEqualToExpiryDateStart(LocalDateTime expiryDateStart) {
		return new Specification<Buying>() {
			@Override
			public Predicate toPredicate(Root<Buying> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.greaterThanOrEqualTo(root.get("expiryDate"), expiryDateStart);
			}
		};
	}
	
	// 만료일(검색기준끝)보다 작거나 같다
	public static Specification<Buying> lessThanOrEqualToExpiryDateEnd(LocalDateTime expiryDateEnd) {
		return new Specification<Buying>() {
			@Override
			public Predicate toPredicate(Root<Buying> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.lessThanOrEqualTo(root.get("expiryDate"), expiryDateEnd);
			}
		};
	}

}
