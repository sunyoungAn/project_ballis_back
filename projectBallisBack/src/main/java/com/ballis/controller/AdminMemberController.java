package com.ballis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ballis.model.Member;
import com.ballis.model.admin.DTO.AdminMemberDTO;
import com.ballis.model.admin.DTO.AdminMemberInfoDTO;
import com.ballis.service.AdminMemberService;
import com.ballis.specification.MemberSpecification;

@RestController
public class AdminMemberController {
	
	@Autowired
	private AdminMemberService adminMemberService;
	
	/*
	 * 회원리스트 - 회원 리스트 전체 가져오기
	 */
	@GetMapping("/api/admin/member/getall")
	public ResponseEntity<Page<Member>> getMemberAll(Pageable pageable) {
		
		Sort sort = Sort.by("memberNumber").descending(); 
		pageable = PageRequest.of(pageable.getPageNumber(), 10, sort);
		
		// 회원정보 전체 검색(페이징기능 적용)
		Page<Member> memberList = adminMemberService.getMemberAll(pageable);
		
		return new ResponseEntity<Page<Member>>(memberList, HttpStatus.OK);
	}
	
	/*
	 * 회원 검색
	 */
	@GetMapping("/api/admin/member/search")
	public ResponseEntity<Page<Member>> searchMember(@RequestParam(value="memberNumber", required=false) Long memberNumber,
									@RequestParam(value="name", required=false) String name,
									@RequestParam(value="email", required=false) String email,
									@RequestParam(value="phoneNumber", required=false) String phoneNumber, Pageable pageable) {
		
		

		Sort sort = Sort.by("memberNumber").descending(); 
		pageable = PageRequest.of(pageable.getPageNumber(), 10, sort);
		
		// 입력된 검색조건에 따라 유동적으로 조건이 설정되게 함
		Specification<Member> spec = (root, query, criteriaBuilder) -> null;
		
		if(memberNumber != null) {
			spec = spec.and(MemberSpecification.equalMemberNumber(memberNumber));
		}
		
		if(name != null) {
			spec = spec.and(MemberSpecification.likeName(name));
		}
		
		if(email != null) {
			spec = spec.and(MemberSpecification.likeEmail(email));
		}
		
		if(phoneNumber != null) {
			spec = spec.and(MemberSpecification.likePhoneNumber(phoneNumber));
		}

		Page<Member> resultList = adminMemberService.searchMember(spec, pageable);
		
		return new ResponseEntity<Page<Member>>(resultList, HttpStatus.OK);
	}
	
	
	/*
	 * 회원 상세내용 검색
	 */
	@GetMapping("/api/admin/member/getone/{memberNumber}")
	public ResponseEntity<AdminMemberInfoDTO> getmemberOne(@PathVariable("memberNumber") Long memberNumber) {
		
		// 회원 상세정보를 검색
		AdminMemberInfoDTO resultDto = adminMemberService.findById(memberNumber);
		
		return new ResponseEntity<AdminMemberInfoDTO>(resultDto, HttpStatus.OK);
	}
	
	
	/*
	 * 회원 수정 
	 */
	@PutMapping("/api/admin/member/edit/{memberNumber}")
	public ResponseEntity<Object> editMember(@PathVariable("memberNumber") Long memberNumber, @RequestBody AdminMemberDTO dto) {
		
		// 수정
		adminMemberService.edit(memberNumber, dto);
		
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	
	/*
	 * 회원 탈퇴
	 */
	@PutMapping("/api/admin/member/withdrawal")
	public ResponseEntity<Object> withdrawalMember(@RequestBody List<Long> ids) {
		
		// 탈퇴처리
		adminMemberService.withdrawal(ids);
		
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	/*
	 * 주소정보 삭제
	 */
	@DeleteMapping("/api/admin/member/delete/address/{id}")
	public ResponseEntity<Object> deleteAddress(@PathVariable Long id) {
		
		// 삭제처리
		adminMemberService.deleteAddress(id);
		
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	/*
	 * 카드정보 삭제
	 */
	@DeleteMapping("/api/admin/member/delete/card/{id}")
	public ResponseEntity<Object> deleteCard(@PathVariable Long id) {
		
		// 삭제처리
		adminMemberService.deleteCard(id);
		
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	

}
