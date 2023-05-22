package com.ballis.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ballis.model.Member;
import com.ballis.model.DTO.LoginInfoDTO;
import com.ballis.model.DTO.MemberDTO;
import com.ballis.service.JwtService;
import com.ballis.service.MailService;
import com.ballis.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class MemberController {
	
	@Autowired
	private MemberService memberService;

	@Autowired
	private MailService mailService;
	
	@Autowired
	private JwtService jwtService;
	
	// 회원가입
	@PostMapping("/api/join/member")
	public ResponseEntity joinMember(@RequestBody Map<String, String> params) {

		String email = params.get("email");
		String password = params.get("password");
		String name = params.get("name");
		String phoneNumber = params.get("phoneNumber");
		int gender = Integer.parseInt(params.get("gender"));
		String memberStatus = params.get("memberStatus");

		// TODO 여기만 수정함 05/15
//		Member joinMember = Member.builder().email(email).password(password).phoneNumber(phoneNumber).gender(gender)
//				.memberStatus(2).name(name).registDate(LocalDateTime.now()).build();
		
		Member joinMember = new Member();
		joinMember.setEmail(email);
		joinMember.setPassword(password);
		joinMember.setPhoneNumber(phoneNumber);
		joinMember.setGender(gender);
		joinMember.setMemberStatus(2);
		joinMember.setName(name);
		joinMember.setRegistDate(LocalDateTime.now());
		
		Member result = memberService.save(joinMember);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// 인증 메일 보내기
	@PostMapping("/api/email/member")
	public ResponseEntity sendMail(HttpSession session, @RequestParam("email") String email) {
		String joinCode = mailService.mailCheck(email);
		session.setAttribute("joinCode", joinCode);

		String subject = "회원가입 인증 코드 발급 안내입니다.";
		StringBuilder sb = new StringBuilder();
		sb.append("가입 인증 코드는 " + joinCode + "입니다.");

		return new ResponseEntity<>(joinCode, HttpStatus.OK);
	}

	// 인증 코드 확인
	@PostMapping("/api/email/verify")
	public ResponseEntity verifyCode(HttpSession session, @RequestParam("joinCode") String joinCode) {
		String storedCode = (String) session.getAttribute("joinCode");
		if (storedCode == null) {
			return new ResponseEntity<>("인증 오류", HttpStatus.BAD_REQUEST);
		}
		if (storedCode.equals(joinCode)) {
			session.removeAttribute("joinCode");
			// 인증 성공 처리
			return new ResponseEntity<>("인증이 완료되었습니다.", HttpStatus.OK);
		} else {
			// 인증 실패 처리
			return new ResponseEntity<>("인증코드가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
		}
	}

	// 이메일 찾기
	@GetMapping("/api/find/email/{phoneNumber}")
	public ResponseEntity getEmailPhoneNumber(@PathVariable("phoneNumber") String phoneNumber) {
		String email = memberService.findEmailByPhoneNumber(phoneNumber);
		if (email == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("이메일을 찾을 수 없습니다");
		}
		return new ResponseEntity<>(email, HttpStatus.OK);

	}

	// 비밀번호 찾기
	@GetMapping("/api/find/password/{email}/{phoneNumber}")
	public Member findByEmailAndPhoneNumber(String email, String phoneNumber) {
		Member member = null;
		try {
			member = memberService.findByEmailAndPhoneNumber(email, phoneNumber);
		} catch (Exception e) {
			// 예외 처리 로직 작성
			e.printStackTrace();
		}
		return member;
	}

	// 임시 비밀번호 발송
	@PostMapping("/api/send/password")
	public ResponseEntity sendPassword(@RequestParam("email") String email) {
		String findCode = mailService.findPasswordMail(email);

		return new ResponseEntity<>(findCode, HttpStatus.OK);

	}

	// 로그인
//	@PostMapping("/api/login/member")
//	public ResponseEntity loginMember(@RequestBody Map<String, String> params, HttpServletResponse res) {
//
//		Member member = memberService.findByEmailAndPassword(params.get("email"), params.get("password"));
//
//		if (member != null) {
//			Long memberNumber = member.getMemberNumber();
//
//			String token = jwtService.getToken("memberNumber", memberNumber);
//
//			Cookie cookie = new Cookie("token", token);
//			cookie.setHttpOnly(true);
//			cookie.setPath("/");
//			res.addCookie(cookie);
//
//			return new ResponseEntity<>(memberNumber, HttpStatus.OK);
//
//		}
//		throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//	}
	
	@PostMapping("/api/login/member")
	public ResponseEntity<LoginInfoDTO> loginMember(@RequestBody Map<String, String> params, HttpServletResponse res) {

		Member member = memberService.findByEmailAndPassword(params.get("email"), params.get("password"));

		if (member != null) {
			Long memberNumber = member.getMemberNumber();

			String token = jwtService.getToken("memberNumber", memberNumber);

			Cookie cookie = new Cookie("token", token);
			cookie.setHttpOnly(true);
			cookie.setPath("/");
			res.addCookie(cookie);
			
			LoginInfoDTO dto = new LoginInfoDTO();
			dto.setMemberNumber(memberNumber);
			dto.setMemberStatus(member.getMemberStatus());

			return new ResponseEntity<LoginInfoDTO>(dto, HttpStatus.OK);

		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	}

	// 로그아웃
	@PostMapping("/api/logout/member")
	public ResponseEntity logoutMember(HttpServletResponse res) {
		Cookie cookie = new Cookie("token", null);
		cookie.setPath("/");
		cookie.setMaxAge(0);

		res.addCookie(cookie);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 마이페이지 회원 정보 불러오기
	@GetMapping("/api/get/member")
	public ResponseEntity getMemberOne(@RequestParam("memberNumber") Long memberNumber) {
		Member member = memberService.findByMemberNumber(memberNumber);

		if (member == null) {
			return ResponseEntity.notFound().build();
		}

		return new ResponseEntity<>(member, HttpStatus.OK);
	}

	@PutMapping("/api/update/member/{memberNumber}")
	public ResponseEntity updateMember(@PathVariable Long memberNumber, @RequestBody MemberDTO dto) {
		Member member = memberService.update(memberNumber, dto.getPassword(), dto.getName(), dto.getPhoneNumber(),
				dto.getBankName(), dto.getAccountNumber(), dto.getDepositor(), dto.getModifiedDate());

		return new ResponseEntity<>(member, HttpStatus.OK);
	}
	

}
