package com.ballis.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ballis.model.Inquiry;
import com.ballis.model.admin.DTO.AdminInquiryDetailDTO;
import com.ballis.model.admin.DTO.AdminInquiryResponseDTO;
import com.ballis.service.AdminInquiryService;
import com.ballis.specification.InquirySpecification;


@RestController
public class AdminInquiryController {
	
	@Autowired
	private AdminInquiryService adminInquiryService;
	
	/*
	 *  문의리스트 - 문의관리페이지에 띄울 정보 모두 가져오기
	 */
	@GetMapping("/api/admin/inquiry/getall")
	public ResponseEntity<AdminInquiryResponseDTO> getInquiryAll(Pageable pageable) {
		
		Sort sort = Sort.by("id").descending(); 
		pageable = PageRequest.of(pageable.getPageNumber(), 10, sort);
		
		Specification<Inquiry> spec = (root, query, criteriaBuilder) -> null;
		
		AdminInquiryResponseDTO responseDto = adminInquiryService.getInquiryInfo(spec, pageable);
		
		return new ResponseEntity<AdminInquiryResponseDTO>(responseDto, HttpStatus.OK);
	}
	
	/*
	 * 문의검색
	 */
	@GetMapping("/api/admin/inquiry/search")
	public ResponseEntity<AdminInquiryResponseDTO> searchinquiry(@RequestParam(value="id", required=false) Long id,
											@RequestParam(value="category", required=false) Integer category,
											@RequestParam(value="memberNumber", required=false) Long memberNumber,
											@RequestParam(value="inquiryStatus", required=false) Integer inquiryStatus, Pageable pageable) {
	
		Sort sort = Sort.by("id").descending(); 
		pageable = PageRequest.of(pageable.getPageNumber(), 10, sort);
		
		Specification<Inquiry> spec = (root, query, criteriaBuilder) -> null;
		
		// 설정한 검색 조건에 따라 유동적으로 조건설정
		if(id != null) {
			spec = spec.and(InquirySpecification.equalId(id));
		}
		
		if(category != 0) {
			spec = spec.and(InquirySpecification.equalCategory(category));
		}
		
		if(memberNumber != null) {
			spec = spec.and(InquirySpecification.equalMemberNumber(memberNumber));
		}
		
		if(inquiryStatus != 0) {
			spec = spec.and(InquirySpecification.equalInquiryStatus(inquiryStatus));
		}
		
		AdminInquiryResponseDTO responseDto = adminInquiryService.getInquiryInfo(spec, pageable);
		
		return new ResponseEntity<AdminInquiryResponseDTO>(responseDto, HttpStatus.OK);
	}
	
	/*
	 * 문의 삭제
	 */
	@DeleteMapping("/api/admin/inquiry/delete/{ids}")
	public ResponseEntity<Object> deleteInquiry(@PathVariable List<Long> ids) {
		
		adminInquiryService.delete(ids);
		
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	/*
	 * 문의상세내용 가져오기
	 */
	@GetMapping("/api/admin/inquiry/getone/{id}")
	public ResponseEntity<AdminInquiryDetailDTO> getInquiryOne(@PathVariable("id") Long id) {
		
		AdminInquiryDetailDTO resultDto = adminInquiryService.getInquiryOne(id);
		
		return new ResponseEntity<AdminInquiryDetailDTO>(resultDto, HttpStatus.OK);
	}
	
	/*
	 * 첨부이미지 다운로드 생성
	 */
	@GetMapping("/api/download")
	public ResponseEntity<Resource> download(@RequestParam("pathName") String pathName) throws IOException {
		String strPath = "C:\\Temp\\imageupload\\"; // TODO 위치 설정 바꿀 것
		String folder = "";

		Path path = Paths.get(strPath + folder + pathName);
		
	    String contentType = Files.probeContentType(path);
		// header를 통해서 다운로드 되는 파일의 정보를 설정한다.
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentDisposition(ContentDisposition.builder("attachment")
	                                                    .filename(pathName, StandardCharsets.UTF_8)
	                                                    .build());
	    headers.add(HttpHeaders.CONTENT_TYPE, contentType);

	    Resource resource = new InputStreamResource(Files.newInputStream(path));
	    return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}
	
	
	/*
	 * 답변등록
	 */
	@PutMapping("/api/admin/inquiry/reply/{id}")
	public ResponseEntity<Object> replyInquiry(@PathVariable("id") Long id, @RequestBody Map<String,String> param) {
		
		adminInquiryService.reply(id, param.get("answer"));
		
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

}
