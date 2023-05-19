package com.ballis.service;

import java.io.File;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ballis.model.Image;
import com.ballis.model.Inquiry;
import com.ballis.model.admin.DTO.AdminInquiryDetailDTO;
import com.ballis.model.admin.DTO.AdminInquiryInfoDTO;
import com.ballis.model.admin.DTO.AdminInquiryResponseDTO;
import com.ballis.repository.ImageRepository;
import com.ballis.repository.InquiryRepository;


import jakarta.transaction.Transactional;


@Service
public class AdminInquiryService {
	
	@Value("${projectBallisBack.inquiry.path}")
	private String uploadPath;
	
	@Autowired
	private InquiryRepository inquiryRepository;
	
	@Autowired
	private ImageRepository imageRepository;
	
	/*
	 * 문의정보가져오기
	 */
	public AdminInquiryResponseDTO getInquiryInfo(Specification<Inquiry> spec, Pageable pageable) {

		Page<Inquiry> targetList = inquiryRepository.findAll(spec, pageable);
		
		AdminInquiryResponseDTO resultDto = new AdminInquiryResponseDTO();
		
		List<AdminInquiryInfoDTO> dtoList = new ArrayList<>();
		for(Inquiry target : targetList) {
			AdminInquiryInfoDTO dto = new AdminInquiryInfoDTO();
			dto.setId(target.getId());
			dto.setMemberNumber(target.getMember().getMemberNumber());
			dto.setName(target.getMember().getName());
			dto.setEmail(target.getMember().getEmail());
			if(target.getCategory() == 1) {
				dto.setCategory("구매관련");
			} else if (target.getCategory() == 2) {
				dto.setCategory("판매관련");
			} else {
				dto.setCategory("기타");
			}
			dto.setTitle(target.getTitle());
			dto.setInquiryRegistDate(target.getInquiryRegistDate());
			dto.setInquiryStatus(target.getInquiryStatus());
			if(target.getInquiryStatus() == 1) {
				dto.setInquiryStatusName("처리중");
			} else {
				dto.setInquiryStatusName("처리완료");
			}
			dto.setAnswerRegistDate(target.getAnswerRegistDate());
			dtoList.add(dto);
		}
		
		resultDto.setInquiryList(dtoList);
		resultDto.setTotal(targetList.getTotalElements());
		
		return resultDto;
	}


	/*
	 * 문의 삭제
	 */
	@Transactional
	public void delete(List<Long> ids) {
		
		for(Long id : ids) {
			// 문의 삭제
			inquiryRepository.deleteById(id);
			
			// 이미지 조회
			List<Image> targetImageList = imageRepository.findByTargetIdAndPageDiv(id, 3);
						
			if(targetImageList.isEmpty() == false) {
				for(Image targetImage : targetImageList) {
					// 이미지 삭제
					imageRepository.delete(targetImage);
								
					// 로컬데이터도 삭제
					try {
									
						File file = new File(uploadPath + URLDecoder.decode(targetImage.getImagePath(), "UTF-8"));
						file.delete();
									
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}


	/*
	 * 문의상세내용 가져오기
	 */
	public AdminInquiryDetailDTO getInquiryOne(Long id) {
		
		// 문의 조회
		Inquiry targetInquiry = inquiryRepository.findById(id).get();
		
		// 이미지 조회
		List<Image> targetImageList = imageRepository.findByTargetIdAndPageDiv(id, 3);
		
		AdminInquiryDetailDTO resultDto = new AdminInquiryDetailDTO();
		resultDto.setInquiryRegistDate(targetInquiry.getInquiryRegistDate());
		if(targetInquiry.getInquiryStatus() == 1) {
			resultDto.setInquiryStatusName("처리중");
		} else {
			resultDto.setInquiryStatusName("처리완료");
		}
		resultDto.setName(targetInquiry.getMember().getName());
		resultDto.setEmail(targetInquiry.getMember().getEmail());
		resultDto.setTitle(targetInquiry.getTitle());
		resultDto.setContent(targetInquiry.getContent());
		resultDto.setAnswer(targetInquiry.getAnswer());
		
		if(targetImageList.isEmpty() == false) {
			List<String> pathList = new ArrayList<>();
			for(Image image : targetImageList) {
				pathList.add(image.getImagePath());
			}
			resultDto.setPathList(pathList);
		}
		
		return resultDto;
	}


	/*
	 * 답변등록
	 */
	@Transactional
	public void reply(Long id, String answer) {

		Inquiry targetInquiry = inquiryRepository.findById(id).get();
		targetInquiry.setAnswer(answer);
		if(targetInquiry.getAnswerRegistDate() == null) {
			targetInquiry.setAnswerRegistDate(LocalDateTime.now());
		} else {
			targetInquiry.setAnswerModifiedDate(LocalDateTime.now());	
		}
		targetInquiry.setInquiryStatus(2);
		
		inquiryRepository.save(targetInquiry);
	}

}
