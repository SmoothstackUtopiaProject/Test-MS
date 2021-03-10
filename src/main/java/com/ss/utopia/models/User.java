package com.ss.utopia.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;
	@NotNull(message = "First name should not be empty")
	@Column(name = "first_name")
	private String firstName;
	@NotNull(message = "Last name should not be empty")
	@Column(name = "last_name")
	private String lastName;
	@NotNull(message = "Email should not be empty")
	@Column(name = "email")
	@Email(message = "Email should be valid")
	private String email;
	@NotNull(message = "Password should not be empty")
	@Column(name = "password")
	private String password;
	@NotNull(message = "Phone number should not be empty")
	@Column(name = "phone")
	private String phone;
	@NotNull(message = "Role should not be empty")
	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Role role;

	@Transient
	private String token;

	public User() {
	};

	public User(Role role, String firstName, String lastName, String email, String password, String phone) {

		this.role = role;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phone = phone;
	}

	public User(Role role, String firstName, String lastName, String email, String password, String phone,
			String token) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.role = role;
		this.token = token;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getId() {
		return id;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
}