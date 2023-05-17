package com.ballis.specification;

import org.springframework.data.jpa.domain.Specification;

import com.ballis.model.Inquiry;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class InquirySpecification {
	
	// 인덱스와 일치
	public static Specification<Inquiry> equalId(Long id) {
		return new Specification<Inquiry>() {
			@Override
			public Predicate toPredicate(Root<Inquiry> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("id"), id);
			}
		};
	}
	
	// 회원번호와 일치
	public static Specification<Inquiry> equalMemberNumber(Long memberNumber) {
		return new Specification<Inquiry>() {
			@Override
			public Predicate toPredicate(Root<Inquiry> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("member").get("memberNumber"), memberNumber);
			}
		};
	}
	
	// 카테고리와 일치
	public static Specification<Inquiry> equalCategory(Integer category) {
		return new Specification<Inquiry>() {
			@Override
			public Predicate toPredicate(Root<Inquiry> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("category"), category);
			}
		};
	}
	
	// 문의처리상태와 일치
	public static Specification<Inquiry> equalInquiryStatus(Integer inquiryStatus) {
		return new Specification<Inquiry>() {
			@Override
			public Predicate toPredicate(Root<Inquiry> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("inquiryStatus"), inquiryStatus);
			}
		};
	}

}
