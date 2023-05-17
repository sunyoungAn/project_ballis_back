package com.ballis.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ballis.model.Image;
import com.ballis.model.Inquiry;
import com.ballis.model.Member;
import com.ballis.model.DTO.InquiryDTO;
import com.ballis.service.ImageService;
import com.ballis.service.InquiryService;
import com.ballis.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class InquiryController {
	
	@Autowired
	private InquiryService inquiryService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private ImageService imageService;
	
	@PostMapping("/api/add/inquiry")
	public ResponseEntity addInquiry(HttpServletRequest req, @RequestParam("imagePath") MultipartFile imagePath, @RequestParam("memberNumber") Long memberNumber,
									@RequestParam("title") String title, @RequestParam("category") Integer category, @RequestParam("inquiryStatus") Integer inquiryStatus,
									@RequestParam("content") String content, @RequestParam("pageDiv") Integer pageDiv,
									@RequestParam("targetId")Long targetId, @RequestParam("mainImageDiv") Integer mainImageDiv) throws IllegalStateException, IOException{
		
		String uuid = UUID.randomUUID().toString();
		String uploadLocation = "c:\\Temp\\upload";
		String filename = uuid + imagePath.getOriginalFilename();
		File file = new File(uploadLocation + "\\" + filename);
		imagePath.transferTo(file);

		Member member = memberService.findByMemberNumber(memberNumber);
		
		
		Inquiry inquirys = new Inquiry();
		inquirys.setTitle(title);
		inquirys.setCategory(category);
		inquirys.setContent(content);
		inquirys.setInquiryStatus(inquiryStatus);
		inquirys.setInquiryRegistDate(LocalDateTime.now());
		inquirys.setMember(member);
		
		Inquiry result = inquiryService.save(inquirys);
		
		Long targetId1 = result.getId();
		
		Image image = new Image();
		image.setImagePath(filename);
		image.setPageDiv(pageDiv);
		image.setTargetId(targetId1);
		image.setMainImageDiv(mainImageDiv);
		image.setRegistDate(LocalDateTime.now());
		
		Image result1 = imageService.save(image);

		return new ResponseEntity<>(Map.of("inquiry", result, "image", result1), HttpStatus.OK);

		}
	
	//이미지 불러오기
	@GetMapping("/api/inquiry/display/image")
	public ResponseEntity<Resource> displayImage(@ModelAttribute Image image){
		String path = "C:\\Temp\\upload\\";
		String folder = "";
//		Resource resource = new FileSystemResource(path + folder + dto.getUuid()+"_"+dto.getFileName());
		Resource resource = new FileSystemResource(path + folder + image.getImagePath());
		if(!resource.exists()) {
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		}
		HttpHeaders header = new HttpHeaders();
		Path imagePath = null;
		try {
//			imagePath = Paths.get(path + folder + dto.getUuid()+"_"+dto.getFileName());
			imagePath = Paths.get(path + folder + image.getImagePath());
			header.add("Content-type", Files.probeContentType(imagePath));
		}catch(IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}

	
	//해당 회원의 문의글 리스트
	@GetMapping("/api/get/inquirys/{memberNumber}")
	public ResponseEntity getAllInquiry(@PathVariable Long memberNumber) {
		Member member = memberService.findByMemberNumber(memberNumber);
		
		List<Inquiry> lists = inquiryService.findByMemberMemberNumber(member.getMemberNumber());
		
		return new ResponseEntity<> (lists, HttpStatus.OK);
	}
	
	//문의글 상세 조회
	@GetMapping("/api/get/inquiry/{id}/{targetId}")
	public ResponseEntity getInquiry(@PathVariable Long id, @PathVariable Long targetId) {

		Inquiry inquiry = inquiryService.findById(id);
		
		if(inquiry == null ) {
			return ResponseEntity.notFound().build();
		}
		
		List<Image> image = imageService.findByTargetId(targetId);
		
		return new ResponseEntity<>(Map.of("inquiry", inquiry, "image", image), HttpStatus.OK);
		
	}
	
	@PutMapping("/api/edit/inquiry/{id}")
	public ResponseEntity editInquiry(@PathVariable Long id, @RequestBody InquiryDTO dto) {
		Inquiry inquiry = inquiryService.findById(id);
	    if(inquiry.getInquiryStatus() != 1) {
	        return new ResponseEntity<>("답변이 완료된 문의는 수정이 불가능합니다.", HttpStatus.FORBIDDEN);
	    }
	    Inquiry updateInquiry = inquiryService.update(id, dto.getTitle(), dto.getCategory(), dto.getContent());
	    return new ResponseEntity<>(updateInquiry, HttpStatus.OK);
	}
	
	@DeleteMapping("/api/delete/inquiry/{id}")
	public ResponseEntity deleteInquiry(@PathVariable Long id) {
		
//		// 해당 id 값을 가진 Image 데이터 삭제
//	    imageService.deleteByTargetId(id);
	    
		inquiryService.delete(id);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
