package com.ss.utopia.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    @Column(name="role")
    private Role role;
	
    @Transient
    private String token;
    
    public User() {
    };    
    public User(Role role, String firstName,String lastName,String email,String password,String phone) {
    	
    	this.role = role;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.email = email;
    	this.password = password;
    	this.phone = phone;
    }
 
	public User(Role role, String firstName,String lastName,String email,String password,String phone,
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

	public void setId(Integer id) {
		this.id = id;
	}


	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}