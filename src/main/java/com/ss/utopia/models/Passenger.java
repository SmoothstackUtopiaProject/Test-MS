package com.ss.utopia.models;

import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "passenger")
public class Passenger {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Integer id;

  @Column(name = "booking_id")
  private Integer bookingId;

  @Column(name = "passport_id")
  private String passportId;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "date_of_birth")
  private Date dateOfBirth;

  @Column(name = "gender")
  private String sex;

  @Column(name = "address")
  private String address;

  @Column(name = "veteran_status")
  private Boolean isVeteran;

  public Passenger() {}

  public Passenger(
    Integer bookingId,
    String passportId,
    String firstName,
    String lastName,
    Date dateOfBirth,
    String sex,
    String address,
    Boolean isVeteran
  ) {
    this.bookingId = bookingId;
    this.passportId = passportId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.sex = sex;
    this.address = address;
    this.isVeteran = isVeteran;
  }

  public Passenger(
    Integer id,
    Integer bookingId,
    String passportId,
    String firstName,
    String lastName,
    Date dateOfBirth,
    String sex,
    String address,
    Boolean isVeteran
  ) {
    this.id = id;
    this.bookingId = bookingId;
    this.passportId = passportId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.sex = sex;
    this.address = address;
    this.isVeteran = isVeteran;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getBookingId() {
    return this.bookingId;
  }

  public void setBookingId(Integer bookingId) {
    this.bookingId = bookingId;
  }

  public String getPassportId() {
    return passportId;
  }

  public void setPassportId(String passportId) {
    this.passportId = passportId;
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

  public Date getDateOfBirth() {
    return this.dateOfBirth;
  }

  public void setDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getSex() {
    return this.sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getAddress() {
    return this.address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Boolean getIsVeteran() {
    return this.isVeteran;
  }

  public void setIsVeteran(Boolean isVeteran) {
    this.isVeteran = isVeteran;
  }
}
