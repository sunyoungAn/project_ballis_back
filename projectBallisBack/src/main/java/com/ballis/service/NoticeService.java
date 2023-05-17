package com.ballis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ballis.model.Notice;
import com.ballis.repository.NoticeRepository;


@Service
public class NoticeService {
	
	@Autowired
	private NoticeRepository noticeRepository;

	public List<Notice> findAll() {
		return noticeRepository.findAll();
	}

	public Notice findById(Long id) {
		return noticeRepository.findById(id).get();
	}

}
