package com.ss.utopia.controllers;

import java.net.ConnectException;
import java.sql.SQLException;

import java.util.Map;

import javax.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.exceptions.ExpiredTokenExpception;
import com.ss.utopia.exceptions.PasswordNotAllowedException;
import com.ss.utopia.exceptions.TokenAlreadyIssuedException;
import com.ss.utopia.exceptions.TokenNotFoundExpection;
import com.ss.utopia.exceptions.UserAlreadyExistsException;
import com.ss.utopia.exceptions.UserNotFoundException;
import com.ss.utopia.models.User;
import com.ss.utopia.models.Role;

import com.ss.utopia.services.UserService;
import com.ss.utopia.services.UserTokenService;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	UserTokenService userTokenService;

	@GetMapping()
	public ResponseEntity<Object> findAll() throws ConnectException, SQLException {

		List<User> userList = userService.findAll();
		return !userList.isEmpty() ? new ResponseEntity<>(userList, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping()
	public ResponseEntity<Object> insert(@RequestBody @Valid User user) {
		try {
			user.setUserRole(Role.USER);
			return new ResponseEntity<>(userService.insert(user), HttpStatus.CREATED);
		} 
		catch (UserAlreadyExistsException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> update(@PathVariable Integer id, @RequestBody @Valid User user)
			throws ConnectException, IllegalArgumentException, SQLException {
		try {
			return new ResponseEntity<>(userService.update(id, user), HttpStatus.CREATED);
		} 
		catch (UserNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<Object> forgotPassword(@RequestBody Map<String, String> uMap)
			throws ConnectException, IllegalArgumentException, SQLException {
		String email = uMap.get("email");
		try {
			userService.sendRecoveryEmail(email);
			return new ResponseEntity<>(null, HttpStatus.OK);
		} 
		catch (UserNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		} 
		catch (TokenAlreadyIssuedException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@PostMapping("/forgot-password/verify-token")
	public ResponseEntity<Object> verifyToken(@RequestBody Map<String, String> uMap) {

		String recoveryCode = uMap.get("recoveryCode");
		try {
			userTokenService.verifyToken(recoveryCode);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (ExpiredTokenExpception | TokenNotFoundExpection e) {
			return new ResponseEntity<>("Link is expired, please request a new one", HttpStatus.NOT_FOUND);
		}

	}

	@PostMapping("/forgot-password/recover")
	public ResponseEntity<Object> passwordRecovery(@RequestBody Map<String, String> uMap)
			throws ConnectException, IllegalArgumentException, SQLException {

		String recoveryCode = uMap.get("recoveryCode");
		String password = uMap.get("password");

		try {
			userService.ChangePassword(userTokenService.verifyToken(recoveryCode), password);
			userTokenService.delete(recoveryCode);
			return new ResponseEntity<>("Password successfully changed ", HttpStatus.OK);

		} catch (ExpiredTokenExpception | TokenNotFoundExpection | UserNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch (PasswordNotAllowedException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@GetMapping("{path}")
	public ResponseEntity<Object> findById(@PathVariable String path) throws ConnectException, SQLException {

		try {
			Integer userId = Integer.parseInt(path);
			User user = userService.findById(userId);
			return new ResponseEntity<>(user, HttpStatus.OK);

		} catch (IllegalArgumentException | NullPointerException err) {
			return new ResponseEntity<>("Cannot process ID " + err.getMessage().substring(0, 1).toLowerCase()
					+ err.getMessage().substring(1, err.getMessage().length()), HttpStatus.BAD_REQUEST);

		} catch (UserNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<Object> findByEmail(@PathVariable String email) throws ConnectException, SQLException {

		try {
			User user = userService.findByEmail(email);
			return new ResponseEntity<>(user, HttpStatus.OK);

		} catch (IllegalArgumentException | NullPointerException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (UserNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/phone/{phone}")
	public ResponseEntity<Object> findByPhone(@PathVariable String phone) throws ConnectException, SQLException {

		try {
			User user = userService.findByPhone(phone);
			return new ResponseEntity<>(user, HttpStatus.OK);

		} catch (IllegalArgumentException | NullPointerException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (UserNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// @GetMapping("/search")
	// public ResponseEntity<Object> findByRole(@RequestParam String role)
	// throws ConnectException, SQLException {
	//
	// try{
	// List<User> userList = role.replaceAll("[^0-9-]", "").length() ==
	// role.length()
	// ? userService.findByRoleId(Integer.parseInt(role))
	// : userService.findByRoleName(role);
	// return !userList.isEmpty()
	// ? new ResponseEntity<>(userList, HttpStatus.OK)
	// : new ResponseEntity<>(HttpStatus.NO_CONTENT);
	//
	// } catch(IllegalArgumentException | NullPointerException err) {
	// return new ResponseEntity<>("Cannot process Role " + err.getMessage()
	// .substring(0, 1).toLowerCase() + err.getMessage()
	// .substring(1, err.getMessage().length()), HttpStatus.BAD_REQUEST);
	//
	// } catch(UserRoleNotFoundException err) {
	// return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
	// }
	// }

	@DeleteMapping("{userId}")
	public ResponseEntity<Object> delete(@PathVariable Integer userId) throws ConnectException, SQLException {

		try {
			userService.delete(userId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		} catch (IllegalArgumentException | NullPointerException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (UserNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@ExceptionHandler(ConnectException.class)
	public ResponseEntity<Object> invalidConnection() {
		return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> invalidMessage() {
		return new ResponseEntity<>("Invalid Message Content!", HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<Object> invalidSQL() {
		return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
	}
}