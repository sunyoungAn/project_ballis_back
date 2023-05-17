package com.ballis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ballis.model.Inquiry;
import com.ballis.repository.InquiryRepository;

import jakarta.transaction.Transactional;


@Service
public class InquiryService {
	
	@Autowired
	private InquiryRepository inquiryRepository;
	
	public Inquiry save(Inquiry addInquiry) {
		return inquiryRepository.save(addInquiry);
	}

	public List<Inquiry> findByMemberMemberNumber(Long memberNumber) {
		return inquiryRepository.findByMemberMemberNumber(memberNumber);
	}

	public Inquiry findById(Long id) {
		return inquiryRepository.findById(id).get();
	}
	
	@Transactional
	public Inquiry update(Long id, String title, Integer category, String content) {
		Inquiry inquiry = inquiryRepository.findById(id).get();
		Inquiry rinquiry = inquiry.update(title, category, content);
		return rinquiry;
	}

	public void delete(Long id) {
		inquiryRepository.deleteById(id);		
	}

}
