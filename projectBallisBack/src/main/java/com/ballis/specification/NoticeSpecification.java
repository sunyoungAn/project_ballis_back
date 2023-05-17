package com.ballis.specification;

import org.springframework.data.jpa.domain.Specification;

import com.ballis.model.Notice;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class NoticeSpecification {
	
	// like 제목
	public static Specification<Notice> likeTitle(String title) {
		return new Specification<Notice>() {
			@Override
			public Predicate toPredicate(Root<Notice> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.like(root.get("title"), "%" + title + "%");
			}
		};
	}
	
	// like 내용
	public static Specification<Notice> likeContent(String content) {
		return new Specification<Notice>() {
			@Override
			public Predicate toPredicate(Root<Notice> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.like(root.get("content"), "%" + content + "%");
			}
		};
	}
	
	// 데이터상태여부와 일치
	public static Specification<Notice> equalDataStatus(Integer status) {
		return new Specification<Notice>() {
			@Override
			public Predicate toPredicate(Root<Notice> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("dataStatus"), status);
			}
		};
	}

}
