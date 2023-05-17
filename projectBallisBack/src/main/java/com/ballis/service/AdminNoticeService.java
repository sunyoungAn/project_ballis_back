package com.ballis.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RestController;

import com.ballis.model.Notice;
import com.ballis.model.admin.DTO.AdminNoticeDTO;
import com.ballis.repository.NoticeRepository;

import jakarta.transaction.Transactional;

@RestController
public class AdminNoticeService {
	
	@Autowired
	private NoticeRepository noticeRepository;
	
	/*
	 * 공지사항등록
	 */
	public void register(String title, String content) {
		
		Notice newNotice = new Notice();
		newNotice.setTitle(title);
		newNotice.setContent(content);
		newNotice.setDataStatus(1);
		newNotice.setRegistDate(LocalDateTime.now());
		
		noticeRepository.save(newNotice);
	}

	/*
	 * 공지사항리스트 - 공지사항 리스트 전체 가져오기
	 */
	public Page<Notice> getNoticeALL(Pageable pageable) {
		return noticeRepository.findAll(pageable);
	}

	/*
	 * 공지사항 상세내용 검색
	 */
	public AdminNoticeDTO findById(Long id) {
		
		Notice targetNotice = noticeRepository.findById(id).get();
		
		AdminNoticeDTO dto = new AdminNoticeDTO();
		dto.setTitle(targetNotice.getTitle());
		dto.setContent(targetNotice.getContent());
		
		return dto;
	}

	/*
	 * 공지사항 수정
	 */
	@Transactional
	public void edit(Long id, String title, String content) {
		
		// 수정할 공지사항을 검색
		Notice targetNotice = noticeRepository.findById(id).get();
		
		// 수정할 값 설정
		targetNotice.setTitle(title);
		targetNotice.setContent(content);
		targetNotice.setModifiedDate(LocalDateTime.now());
		
		// 수정실행
		noticeRepository.save(targetNotice);
	}

	/*
	 * 공지사항 검색
	 */
	public Page<Notice> searchNotice(Specification<Notice> spec, Pageable pageable) {
		return noticeRepository.findAll(spec, pageable);
	}

	/*
	 * 공지사항 삭제
	 */
	@Transactional
	public void delete(List<Long> ids) {
		
		for(Long id : ids) {
			
			// 수정할 공지사항을 검색
			Notice targetNotice = noticeRepository.findById(id).get();
			
			// 데이터 상태여부를 2:삭제 상태로 수정
			targetNotice.setDataStatus(2);
			
			// 수정실행
			noticeRepository.save(targetNotice);
		}
	}

}
