package com.ss.utopia.controllers;

import java.net.ConnectException;
import java.security.Principal;
import java.sql.SQLException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ss.utopia.exceptions.UserNotFoundException;
import com.ss.utopia.jwk.JwtTokenProvider;
import com.ss.utopia.models.User;
import com.ss.utopia.services.UserService;

@RestController
@CrossOrigin()
@RequestMapping(value = "/auth")
public class AuthController {
	
	@Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;
	
	@GetMapping("/login")
	public ResponseEntity<Object> login(Principal principal) throws ConnectException, IllegalArgumentException, SQLException, UserNotFoundException{
		if(principal == null) {
			return ResponseEntity.ok(principal);
		}
		UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) principal;
		User user = userService.findByEmail(authenticationToken.getName());
		user.setToken(tokenProvider.generateToken(authenticationToken));
		
		return new ResponseEntity<>(user, HttpStatus.OK);
		
	}
}
