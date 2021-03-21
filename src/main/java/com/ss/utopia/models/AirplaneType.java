package com.ss.utopia.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "airplane_type")
public class AirplaneType {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer airplaneTypeId;

  @Column(name = "name")
  private String airplaneTypeName;

  @Column(name = "max_capacity")
  private Integer airplaneTypeCapacity;

  @Column(name = "class_first_capacity")
  private Integer airplaneTypeFirstClassCapacity;

  @Column(name = "class_business_capacity")
  private Integer airplaneTypeBusinessClassCapacity;

  @Column(name = "class_coach_capacity")
  private Integer airplaneTypeCoachClassCapacity;

  @Column(name = "class_first_columns")
  private Integer airplaneTypeFirstClassColumns;

  @Column(name = "class_business_columns")
  private Integer airplaneTypeBusinessClassColumns;

  @Column(name = "class_coach_columns")
  private Integer airplaneTypeCoachClassColumns;

  @Column(name = "emergency_exit_rows")
  private String airplaneTypeEmergencyExitRows;

  public AirplaneType() {}
  public AirplaneType(
    Integer airplaneTypeId,
    String airplaneTypeName,
    Integer airplaneTypeCapacity,
    Integer airplaneTypeFirstClassCapacity,
    Integer airplaneTypeBusinessClassCapacity,
    Integer airplaneTypeCoachClassCapacity,
    Integer airplaneTypeFirstClassColumns,
    Integer airplaneTypeBusinessClassColumns,
    Integer airplaneTypeCoachClassColumns,
    String airplaneTypeEmergencyExitRows
  ) {
    this.airplaneTypeId = airplaneTypeId;
    this.airplaneTypeName = airplaneTypeName;
    this.airplaneTypeCapacity = airplaneTypeCapacity;
    this.airplaneTypeFirstClassCapacity = airplaneTypeFirstClassCapacity;
    this.airplaneTypeBusinessClassCapacity = airplaneTypeBusinessClassCapacity;
    this.airplaneTypeCoachClassCapacity = airplaneTypeCoachClassCapacity;
    this.airplaneTypeFirstClassColumns = airplaneTypeFirstClassColumns;
    this.airplaneTypeBusinessClassColumns = airplaneTypeBusinessClassColumns;
    this.airplaneTypeCoachClassColumns = airplaneTypeCoachClassColumns;
    this.airplaneTypeEmergencyExitRows = airplaneTypeEmergencyExitRows;
  }

	public AirplaneType(
    String airplaneTypeName,
    Integer airplaneTypeCapacity,
    Integer airplaneTypeFirstClassCapacity,
    Integer airplaneTypeBusinessClassCapacity,
    Integer airplaneTypeCoachClassCapacity,
    Integer airplaneTypeFirstClassColumns,
    Integer airplaneTypeBusinessClassColumns,
    Integer airplaneTypeCoachClassColumns,
    String airplaneTypeEmergencyExitRows
  ) {
    this.airplaneTypeName = airplaneTypeName;
    this.airplaneTypeCapacity = airplaneTypeCapacity;
    this.airplaneTypeFirstClassCapacity = airplaneTypeFirstClassCapacity;
    this.airplaneTypeBusinessClassCapacity = airplaneTypeBusinessClassCapacity;
    this.airplaneTypeCoachClassCapacity = airplaneTypeCoachClassCapacity;
    this.airplaneTypeFirstClassColumns = airplaneTypeFirstClassColumns;
    this.airplaneTypeBusinessClassColumns = airplaneTypeBusinessClassColumns;
    this.airplaneTypeCoachClassColumns = airplaneTypeCoachClassColumns;
    this.airplaneTypeEmergencyExitRows = airplaneTypeEmergencyExitRows;
  }

  public Integer getAirplaneTypeId() {
    return this.airplaneTypeId;
  }

  public void setAirplaneTypeId(Integer airplaneTypeId) {
    this.airplaneTypeId = airplaneTypeId;
  }

  public String getAirplaneTypeName() {
    return this.airplaneTypeName;
  }

  public void setAirplaneTypeName(String airplaneTypeName) {
    this.airplaneTypeName = airplaneTypeName;
  }

  public Integer getAirplaneTypeCapacity() {
    return this.airplaneTypeCapacity;
  }

  public void setAirplaneTypeCapacity(Integer airplaneTypeCapacity) {
    this.airplaneTypeCapacity = airplaneTypeCapacity;
  }

  public Integer getAirplaneTypeFirstClassCapacity() {
    return this.airplaneTypeFirstClassCapacity;
  }

  public void setAirplaneTypeFirstClassCapacity(
    Integer airplaneTypeFirstClassCapacity
  ) {
    this.airplaneTypeFirstClassCapacity = airplaneTypeFirstClassCapacity;
  }

  public Integer getAirplaneTypeBusinessClassCapacity() {
    return this.airplaneTypeBusinessClassCapacity;
  }

  public void setAirplaneTypeBusinessClassCapacity(
    Integer airplaneTypeBusinessClassCapacity
  ) {
    this.airplaneTypeBusinessClassCapacity = airplaneTypeBusinessClassCapacity;
  }

  public Integer getAirplaneTypeCoachClassCapacity() {
    return this.airplaneTypeCoachClassCapacity;
  }

  public void setAirplaneTypeCoachClassCapacity(
    Integer airplaneTypeCoachClassCapacity
  ) {
    this.airplaneTypeCoachClassCapacity = airplaneTypeCoachClassCapacity;
  }

  public Integer getAirplaneTypeFirstClassColumns() {
    return this.airplaneTypeFirstClassColumns;
  }

  public void setAirplaneTypeFirstClassColumns(
    Integer airplaneTypeFirstClassColumns
  ) {
    this.airplaneTypeFirstClassColumns = airplaneTypeFirstClassColumns;
  }

  public Integer getAirplaneTypeBusinessClassColumns() {
    return this.airplaneTypeBusinessClassColumns;
  }

  public void setAirplaneTypeBusinessClassColumns(
    Integer airplaneTypeBusinessClassColumns
  ) {
    this.airplaneTypeBusinessClassColumns = airplaneTypeBusinessClassColumns;
  }

  public Integer getAirplaneTypeCoachClassColumns() {
    return this.airplaneTypeCoachClassColumns;
  }

  public void setAirplaneTypeCoachClassColumns(
    Integer airplaneTypeCoachClassColumns
  ) {
    this.airplaneTypeCoachClassColumns = airplaneTypeCoachClassColumns;
  }

  public String getAirplaneTypeEmergencyExitRows() {
    return this.airplaneTypeEmergencyExitRows;
  }

  public void setAirplaneTypeEmergencyExitRows(
    String airplaneTypeEmergencyExitRows
  ) {
    this.airplaneTypeEmergencyExitRows = airplaneTypeEmergencyExitRows;
  }
}
