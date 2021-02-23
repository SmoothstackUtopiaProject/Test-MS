package com.ss.utopia.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "airplane")
public class Airplane {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;
	
	@NotNull(message = "Type ID should not be empty")
	@Column(name = "type_id", nullable = false)
	private Integer typeId;

	public Airplane() {}
	public Airplane(Integer typeId) {
		this.typeId = typeId;
	}
	public Airplane(Integer id, Integer typeId) {
		this.id = id;
		this.typeId = typeId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
}