package com.ss.utopia.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "booking_guest")
public class BookingGuest {
  
  @Id
	@Column(name = "booking_id")
	private Integer bookingId;

  @Column(name = "contact_email")
	private String bookingEmail;

  @Column(name = "contact_phone")
	private String bookingPhone;

  public BookingGuest(){}
  public BookingGuest(Integer bookingId, String bookingEmail, String bookingPhone) {
    super();
    this.bookingId = bookingId;
    this.bookingEmail = bookingEmail;
    this.bookingPhone = bookingPhone;
  }

	public Integer getBookingId() {
		return this.bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

  public String getBookingEmail() {
		return this.bookingEmail;
	}

	public void setBookingEmail(String bookingEmail) {
		this.bookingEmail = bookingEmail;
	}

  public String getBookingPhone() {
		return this.bookingPhone;
	}

	public void setBookingPhone(String bookingPhone) {
		this.bookingPhone = bookingPhone;
	}
}