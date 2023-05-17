package com.ballis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ballis.model.Image;
import com.ballis.model.DTO.ImageDTO;
import com.ballis.repository.ImageRepository;


@Service
public class ImageService {
	
	@Autowired
	private ImageRepository imageRepository;

	public List<Image> findByTargetIdAndPageDiv(int productid, int i) {
		return imageRepository.findByTargetIdAndPageDiv(productid,i);
	}
	
	public Image save(Image addImage) {
		return imageRepository.save(addImage);
	}

	public String save(MultipartFile image) {
		return imageRepository.save(image);
	}

	public List<Image> findByTargetId(Long targetId) {
		return imageRepository.findByTargetId(targetId);
	}

	public String findByImagePath(String imagePath) {
		return imageRepository.findByImagePath(imagePath);
	}

	public void save(ImageDTO dto) {
		imageRepository.save(dto);	
	}
	
	public void deleteByTargetId(Long targetId) {
	    imageRepository.deleteByTargetId(targetId);
	}

}
