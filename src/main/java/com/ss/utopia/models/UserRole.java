package com.ss.utopia.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_role")
public class UserRole {
	
	@Id
	@Column(name = "id")
	private Integer userRoleId;
	
	@Column(name = "name")
	private String userRoleName;
	
	public UserRole() {}
	public UserRole(Integer userRoleId, String userRoleName) {
		super();
		this.userRoleId = userRoleId;
		this.userRoleName = userRoleName;
	}

	public Integer getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getUserRoleName() {
		return userRoleName;
	}

	public void setUserRoleName(String userRoleName) {
		this.userRoleName = userRoleName;
	}
}