package com.ballis.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;

@Service("jwtService")
public class JwtServiceImpl implements JwtService {
	
private String secretKey ="1234567890abcdefg##1234567890abcdefg##1234567890abcdefg##";
	
	@Override
	public String getToken(String key, Object value) {
		Date logTime = new Date();
		logTime.setTime(logTime.getTime()+ 1000*60*30);
		byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
		Key loginkey = new SecretKeySpec(secretByteKey, SignatureAlgorithm.HS256.getJcaName());
		
		Map<String, Object> headerMap = new HashMap<>();
		headerMap.put("typ", "JWT");
		headerMap.put("alg", "HS256");
		
		Map<String, Object> map = new HashMap<>();
		map.put(key, value);
		
		JwtBuilder builder = Jwts.builder()
								.setHeader(headerMap)
								.setClaims(map)
								.setExpiration(logTime)
								.signWith(loginkey,SignatureAlgorithm.HS256);
		
		return builder.compact();
	}

	@Override
	public Claims getCLaims(String token) {
		if(token != null && !"".equals(token)) {
			try {
				byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
				Key loginkey = new SecretKeySpec(secretByteKey, SignatureAlgorithm.HS256.getJcaName());
				return Jwts.parserBuilder()
						.setSigningKey(loginkey)
						.build()
						.parseClaimsJws(token)
						.getBody();
			}catch(ExpiredJwtException e) {
			    throw new JwtException("토큰 유효기간이 만료되었습니다.");
			}catch(JwtException e) {
				throw new JwtException("토큰 검증에 실패했습니다.");
			}
		}
		return null;
	}

	@Override
	public boolean isValid(String token) {
		return this.getCLaims(token) !=null;
	}

	@Override
	public int getmemberNumber(String token) {
		Claims claims = this.getCLaims(token);
		
		if(claims != null) {
			return Integer.parseInt(claims.get("memberNumber").toString());
		}
		return 0;
	}

}
