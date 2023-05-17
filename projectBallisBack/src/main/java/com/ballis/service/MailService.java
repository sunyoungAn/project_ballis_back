package com.ballis.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MailService {
	
	private final JavaMailSender javaMailSender;
	
	@Autowired
	private MemberService memberService;
	
	public String mailCheck(String email) {
		Random random = new Random();
		String key = "";
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setFrom("zzz@naver.com"); // TODO 메일 넣을 것
		
		for(int i=0; i<3; i++) {
			int index = random.nextInt(25)+65;
			key+=(char)index;
		}
		int numIndex = random.nextInt(9999)+1000;
		key+=numIndex;
		
		
		
		message.setSubject("회원가입 이메일 인증을 위한 메일 전송");
		message.setText("인증 코드:"+key);
		
		
		javaMailSender.send(message);
		
		return key;
	}

	public String findPasswordMail(String email) {
		Random random = new Random();
		String newpassword = "";
		
		for(int i=0; i<3; i++) {
			int index = random.nextInt(25)+65;
			newpassword+=(char)index;
		}
		int numIndex = random.nextInt(9999)+1000;
		newpassword+=numIndex;
		
		memberService.updatePassword(newpassword, email);
		

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setFrom("zzz@naver.com"); // TODO 메일 넣을 것
		message.setSubject("임시 비밀번호 발급입니다.");
		message.setText("인증 코드:"+newpassword);
		
		javaMailSender.send(message);
		
		return newpassword;
	}

}
