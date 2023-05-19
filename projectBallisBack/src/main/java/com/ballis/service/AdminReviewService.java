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
import com.ballis.model.Review;
import com.ballis.model.admin.DTO.AdminReviewInfoDTO;
import com.ballis.model.admin.DTO.AdminReviewResponseDTO;
import com.ballis.repository.ImageRepository;
import com.ballis.repository.ReviewRepository;

import jakarta.transaction.Transactional;


@Service
public class AdminReviewService {
	
	@Value("${projectBallisBack.review.path}")
	private String uploadPath;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private ImageRepository imageRepository;
	
	/*
	 * 리뷰정보가져오기
	 */
	public AdminReviewResponseDTO getReviewInfo(Specification<Review> spec, Pageable pageable) {
		
		Page<Review> targetList = reviewRepository.findAll(spec, pageable);
		
		AdminReviewResponseDTO resultDto = new AdminReviewResponseDTO();
		
		List<AdminReviewInfoDTO> dtoList = new ArrayList<>();
		for(Review target : targetList) {
			AdminReviewInfoDTO dto = new AdminReviewInfoDTO();
			dto.setId(target.getId());
			dto.setProductId(target.getProduct().getId());
			dto.setProductEngName(target.getProduct().getProductEngName());
			dto.setProductKorName(target.getProduct().getProductKorName());
			dto.setMemberNumber(target.getMember().getMemberNumber());
			dto.setName(target.getName());
			dto.setContent(target.getContent());
			dto.setRegistDate(target.getRegistDate());
			dtoList.add(dto);
		}
		
		resultDto.setReviewList(dtoList);
		resultDto.setTotal(targetList.getTotalElements());
		
		return resultDto;
	}

	/*
	 * 리뷰 삭제
	 */
	@Transactional
	public void delete(List<Long> ids) {
		
		for(Long id : ids) {
			Review targetReview = reviewRepository.findById(id).get();
			targetReview.setDataStatus(2);
			targetReview.setModifiedDate(LocalDateTime.now());
			
			reviewRepository.save(targetReview);
			
			// 이미지 조회
			List<Image> targetImageList = imageRepository.findByTargetIdAndPageDiv(id, 2);
			
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

}
