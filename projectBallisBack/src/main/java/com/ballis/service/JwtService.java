package com.ballis.service;

import io.jsonwebtoken.Claims;

public interface JwtService {

	String getToken(String key, Object value);
	
	Claims getCLaims(String token);
	
	boolean isValid(String token);
	
	int getmemberNumber(String token);
}
