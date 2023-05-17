package com.ballis.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.ballis.model.Selling;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class SellingSpecification {
	
	// 데이터상태여부와 일치
	public static Specification<Selling> equalDataStatus(Integer status) {
		return new Specification<Selling>() {
			@Override
			public Predicate toPredicate(Root<Selling> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("dataStatus"), status);
			}
		};
	}
	
	// 보관판매구분과 일치
	public static Specification<Selling> equalInventoryDiv(Integer inventoryDiv) {
		return new Specification<Selling>() {
			@Override
			public Predicate toPredicate(Root<Selling> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("inventoryDiv"), inventoryDiv);
			}
		};
	}
	
	// 인덱스와 일치
	public static Specification<Selling> equalId(Long id) {
		return new Specification<Selling>() {
			@Override
			public Predicate toPredicate(Root<Selling> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("id"), id);
			}
		};
	}
	
	// 상품아이디와 일치
	public static Specification<Selling> equalProductId(Long productId) {
		return new Specification<Selling>() {
			@Override
			public Predicate toPredicate(Root<Selling> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("product").get("id"), productId);
			}
		};
	}
	
	// 회원번호와 일치
	public static Specification<Selling> equalMemberNumber(Long memberNumber) {
		return new Specification<Selling>() {
			@Override
			public Predicate toPredicate(Root<Selling> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("member").get("memberNumber"), memberNumber);
			}
		};
	}
	
	// 판매상태와 일치
	public static Specification<Selling> equalSellingStatus(Integer sellingStatus) {
		return new Specification<Selling>() {
			@Override
			public Predicate toPredicate(Root<Selling> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("sellingStatus"), sellingStatus);
			}
		};
	}
	
	// 만료일(검색기준시작)보다 크거나 같다
	public static Specification<Selling> greaterThanOrEqualToExpiryDateStart(LocalDateTime expiryDateStart) {
		return new Specification<Selling>() {
			@Override
			public Predicate toPredicate(Root<Selling> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.greaterThanOrEqualTo(root.get("expiryDate"), expiryDateStart);
			}
		};
	}
	
	// 만료일(검색기준끝)보다 작거나 같다
	public static Specification<Selling> lessThanOrEqualToExpiryDateEnd(LocalDateTime expiryDateEnd) {
		return new Specification<Selling>() {
			@Override
			public Predicate toPredicate(Root<Selling> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.lessThanOrEqualTo(root.get("expiryDate"), expiryDateEnd);
			}
		};
	}

}
