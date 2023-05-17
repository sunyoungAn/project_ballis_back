package com.ballis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

import com.ballis.model.Image;
import com.ballis.model.DTO.ImageDTO;

public interface ImageRepository  extends JpaRepository<Image, Long> {
	
	// 관리자기능 - 대상id와 구분으로 검색하기
	List<Image> findByTargetIdAndPageDiv(Long id, Integer i);

	// 관리자기능 - 인덱스와 구분으로 검색하기
	Image findByIdAndPageDiv(Long id, Integer i);
	
	// TODO 언니에게 productid가 int 여도 되는지 확인
	List<Image> findByTargetIdAndPageDiv(int productid, int i);
	
	String save(MultipartFile image);

	List<Image> findByTargetId(Long targetId);

	String findByImagePath(String imagePath);

	void save(ImageDTO dto);

	void deleteByTargetId(Long targetId);

}
