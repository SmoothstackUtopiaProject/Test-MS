package com.ss.utopia.controllers;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.exceptions.UserAlreadyExistsException;
import com.ss.utopia.exceptions.UserNotFoundException;
import com.ss.utopia.exceptions.UserRoleNotFoundException;
import com.ss.utopia.models.User;
import com.ss.utopia.services.UserRoleService;
import com.ss.utopia.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	UserRoleService userRoleService;

	@GetMapping()
	public ResponseEntity<Object> findAll() 
	throws ConnectException, SQLException {

		List<User> userList = userService.findAll();
		return !userList.isEmpty() 
		? new ResponseEntity<>(userList, HttpStatus.OK)
		: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

	@GetMapping("{path}")
	public ResponseEntity<Object> findById(@PathVariable String path)
	throws ConnectException, SQLException {

		try {
			Integer userId = Integer.parseInt(path);
			User user = userService.findById(userId);
			return new ResponseEntity<>(user, HttpStatus.OK);

		} catch(IllegalArgumentException | NullPointerException err) {
			return new ResponseEntity<>("Cannot process ID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()), HttpStatus.BAD_REQUEST);
			
		} catch(UserNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<Object> findByEmail(@PathVariable String email)
	throws ConnectException, SQLException {

		try {
			User user = userService.findByEmail(email);
			return new ResponseEntity<>(user, HttpStatus.OK);

		} catch( IllegalArgumentException | NullPointerException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(UserNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/phone/{phone}")
	public ResponseEntity<Object> findByPhone(@PathVariable String phone)
	throws ConnectException, SQLException {

		try {
			User user = userService.findByPhone(phone);
			return new ResponseEntity<>(user, HttpStatus.OK);

		} catch( IllegalArgumentException | NullPointerException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(UserNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/search")
	public ResponseEntity<Object> findByRole(@RequestParam String role)
	throws ConnectException, SQLException {

		try{
			List<User> userList = role.replaceAll("[^0-9-]", "").length() == role.length()
			? userService.findByRoleId(Integer.parseInt(role))
			: userService.findByRoleName(role);
			return !userList.isEmpty() 
			? new ResponseEntity<>(userList, HttpStatus.OK)
			: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

		} catch(IllegalArgumentException | NullPointerException err) {
			return new ResponseEntity<>("Cannot process Role " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()), HttpStatus.BAD_REQUEST);

		} catch(UserRoleNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	public ResponseEntity<Object> insert(@RequestBody String body)
	throws ConnectException, SQLException {

		try {
			User user = new ObjectMapper().readValue(body, User.class);
			User newUser = userService.insert(1, user.getFirstName(), 
			user.getLastName(), user.getEmail(), user.getPassword(), user.getPhone());
			return new ResponseEntity<>(newUser, HttpStatus.CREATED);

		} catch(ArrayIndexOutOfBoundsException | JsonProcessingException | NullPointerException err) {
			return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
			
		} catch(IllegalArgumentException err) {
			return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);

		} catch(UserAlreadyExistsException err) {
			return new ResponseEntity<>(err, HttpStatus.CONFLICT);
		}
	}

	@PutMapping("{path}")
	public ResponseEntity<Object> update(@PathVariable String path, @RequestBody String body) 
	throws ConnectException, SQLException {

		try {
			Integer userId = Integer.parseInt(path);
			User user = new ObjectMapper().readValue(body, User.class);
			Integer userRole = Integer.parseInt(body.replaceAll("[^a-zA-Z0-9,]", "").split("userRoleid")[1].split(",")[0]);

			User newUser = userService.update(userId, userRole, user.getFirstName(), 
			user.getLastName(), user.getEmail(), user.getPassword(), user.getPhone());
			return new ResponseEntity<>(newUser, HttpStatus.ACCEPTED);

		} catch(ArrayIndexOutOfBoundsException | JsonProcessingException  err) {
			return new ResponseEntity<>("Invalid User formatting!", HttpStatus.BAD_REQUEST);

		} catch(IllegalArgumentException | NullPointerException err) {
			return new ResponseEntity<>("Cannot process ID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()), HttpStatus.BAD_REQUEST);

		} catch(UserNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("{userId}")
	public ResponseEntity<Object> delete(@PathVariable Integer userId)
	throws ConnectException, SQLException  {

		try {
			userService.delete(userId);
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

		} catch(IllegalArgumentException | NullPointerException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(UserNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// @ExceptionHandler(ConnectException.class)
	// public ResponseEntity<Object> invalidConnection() {
	// 	return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
	// }

	// @ExceptionHandler(HttpMessageNotReadableException.class)
	// public ResponseEntity<Object> invalidMessage() {
	// 	return new ResponseEntity<>("Invalid Message Content!", HttpStatus.BAD_REQUEST);
	// }

	// @ExceptionHandler(SQLException.class)
	// public ResponseEntity<Object> invalidSQL() {
	// 	return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
	// }
}