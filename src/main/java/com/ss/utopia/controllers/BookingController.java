package com.ss.utopia.controllers;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.exceptions.BookingAlreadyExistsException;
import com.ss.utopia.exceptions.BookingNotFoundException;
import com.ss.utopia.exceptions.BookingUserNotFoundException;
import com.ss.utopia.exceptions.FlightNotFoundException;
import com.ss.utopia.models.Booking;
import com.ss.utopia.models.BookingGuest;
import com.ss.utopia.models.BookingUser;
import com.ss.utopia.models.HttpError;
import com.ss.utopia.models.BookingWithReferenceData;
import com.ss.utopia.models.FlightBooking;
import com.ss.utopia.services.BookingGuestService;
import com.ss.utopia.services.BookingService;
import com.ss.utopia.services.BookingUserService;

@RestController
@RequestMapping("/bookings")
public class BookingController {
	
	@Autowired
	private BookingService bookingService;

	@Autowired 
	BookingGuestService bookingGuestService;

	@Autowired 
	BookingUserService bookingUserService;

	@GetMapping()
	public ResponseEntity<Object> findAll() 
	throws ConnectException, SQLException {

		List<Booking> bookingList = bookingService.findAll();
		return !bookingList.isEmpty() 
		? new ResponseEntity<>(bookingList, HttpStatus.OK)
		: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

	@GetMapping("/referencedata")
	public ResponseEntity<Object> findAllWithReferenceData() 
	throws ConnectException, SQLException {

		List<BookingWithReferenceData> bookingList = bookingService.findAllWithReferenceData();
		return !bookingList.isEmpty() 
		? new ResponseEntity<>(bookingList, HttpStatus.OK)
		: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

	@GetMapping("{path}")
	public ResponseEntity<Object> findById(@PathVariable String path)
	throws ConnectException, SQLException {

		try {
			Integer bookingId = Integer.parseInt(path);
			BookingWithReferenceData bookingWithReferenceData = bookingService.findByIdWithReferenceData(bookingId);
			return new ResponseEntity<>(bookingWithReferenceData, HttpStatus.OK);

		} catch(IllegalArgumentException | NullPointerException err) {
			String errorMessage = "Cannot process Booking ID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()).replaceAll("[\"]", "");
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);
			
		} catch(BookingNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/confirmation/{confirmationCode}")
	public ResponseEntity<Object> findByConfirmationCode(@PathVariable String confirmationCode)
	throws ConnectException, SQLException {

		try {
			Booking booking = bookingService.findByConfirmationCode(confirmationCode);
			return new ResponseEntity<>(booking, HttpStatus.OK);

		} catch( IllegalArgumentException | NullPointerException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 400), HttpStatus.BAD_REQUEST);
		} catch(BookingNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/search")
	public ResponseEntity<Object> findByStatus(@RequestParam String status)
	throws ConnectException, SQLException {

		try{
			Integer statusId = Integer.parseInt(status);
			List<Booking> bookingList = bookingService.findByStatus(statusId);
			return !bookingList.isEmpty() 
			? new ResponseEntity<>(bookingList, HttpStatus.OK)
			: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

		} catch(IllegalArgumentException | NullPointerException err) {
			String errorMessage = "Cannot process Status " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()).replaceAll("[\"]", "");
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/guests")
	public ResponseEntity<Object> insertByGuest(@RequestBody String body)
	throws ConnectException, SQLException {

		try {
			BookingGuest guest = new ObjectMapper().readValue(body, BookingGuest.class);
			Booking newBooking = bookingService.insertByBookingGuest(guest.getEmail(), guest.getPhone());
			return new ResponseEntity<>(newBooking, HttpStatus.CREATED);

		} catch(IllegalArgumentException | JsonProcessingException | NullPointerException err) {
			String errorMessage = "Cannot process Guest contact information " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()).replaceAll("[\"]", "");
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);

		} catch(BookingAlreadyExistsException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 409), HttpStatus.CONFLICT);
		}
	}

	@PostMapping("/users/{userIdString}")
	public ResponseEntity<Object> insertByUser(@PathVariable String userIdString)
	throws ConnectException, SQLException {

		try {
			Integer userId = Integer.parseInt(userIdString);
			Booking newBooking = bookingService.insertByBookingUser(userId);
			return new ResponseEntity<>(newBooking, HttpStatus.CREATED);

		} catch(IllegalArgumentException | NullPointerException err) {
			String errorMessage = "Cannot process UserID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()).replaceAll("[\"]", "");
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);

		} catch(BookingAlreadyExistsException | BookingUserNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 409), HttpStatus.CONFLICT);
		}
	}

	@PutMapping()
	public ResponseEntity<Object> update(@RequestBody String body)
	throws ConnectException, SQLException {

		try {
			Booking booking = new ObjectMapper().readValue(body, Booking.class);
			Booking newBooking = bookingService.update(booking.getId(), booking.getStatus());		
			return new ResponseEntity<>(newBooking, HttpStatus.ACCEPTED);

		} catch(IllegalArgumentException | JsonProcessingException | NullPointerException err) {			
			String errorMessage = "Cannot process Booking, formatting is invalid (ID: number, Status: number).";
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);

		} catch(BookingNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/flights")
	public ResponseEntity<Object> updateFlightBooking(@RequestBody String body)
	throws ConnectException, SQLException {

		try {
			FlightBooking flightBooking = new ObjectMapper().readValue(body, FlightBooking.class);
			FlightBooking newflightBooking = bookingService.updateFlightBooking(flightBooking.getBookingId(), flightBooking.getFlightId());
			return new ResponseEntity<>(newflightBooking, HttpStatus.ACCEPTED);

		} catch(IllegalArgumentException | JsonProcessingException | NullPointerException err) {
			String errorMessage = "Cannot process BookingID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()).replaceAll("[\"]", "");
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);

		} catch(BookingNotFoundException | FlightNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/guests")
	public ResponseEntity<Object> updateBookingGuest(@RequestBody String body)
	throws ConnectException, SQLException {

		try {
			BookingGuest guest = new ObjectMapper().readValue(body, BookingGuest.class);
			BookingGuest newBookingGuest = bookingGuestService.update(guest.getBookingId(), guest.getEmail(), guest.getPhone());
			return new ResponseEntity<>(newBookingGuest, HttpStatus.ACCEPTED);

		} catch(IllegalArgumentException | JsonProcessingException | NullPointerException err) {
			String errorMessage = "Cannot process BookingID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()).replaceAll("[\"]", "");
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);

		} catch(BookingNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/users")
	public ResponseEntity<Object> updateBookingUser(@RequestBody String body)
	throws ConnectException, SQLException {

		try {
			BookingUser bookingUser = new ObjectMapper().readValue(body, BookingUser.class);
			BookingUser newBookingUser = bookingUserService.update(bookingUser.getBookingId(), bookingUser.getUserId());
			return new ResponseEntity<>(newBookingUser, HttpStatus.ACCEPTED);

		} catch(IllegalArgumentException | JsonProcessingException | NullPointerException err) {
			String errorMessage = "Cannot process BookingID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()).replaceAll("[\"]", "");
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);

		} catch(BookingUserNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("{bookingIdString}")
	public ResponseEntity<Object> delete(@PathVariable String bookingIdString)
	throws ConnectException, SQLException  {

		try {
			Integer bookingId = Integer.parseInt(bookingIdString);
			bookingService.delete(bookingId);
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

		} catch(IllegalArgumentException | NullPointerException err) {
			String errorMessage = "Cannot process BookingID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()).replaceAll("[\"]", "");
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);

		} catch(BookingNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/flights/{bookingIdString}")
	public ResponseEntity<Object> deleteFlightBooking(@PathVariable String bookingIdString)
	throws ConnectException, SQLException  {

		try {
			Integer bookingId = Integer.parseInt(bookingIdString);
			long flightBookingsDeleted = bookingService.deleteFlightBooking(bookingId);
			return new ResponseEntity<>("Removed (" + flightBookingsDeleted + ") Flight Bookings with ID: " + bookingIdString, HttpStatus.OK);

		} catch(IllegalArgumentException | NullPointerException err) {
			String errorMessage = "Cannot process BookingID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()).replaceAll("[\"]", "");
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);

		} catch(BookingNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/guests/{bookingIdString}")
	public ResponseEntity<Object> deleteBookingGuests(@PathVariable String bookingIdString)
	throws ConnectException, SQLException  {

		try {
			Integer bookingId = Integer.parseInt(bookingIdString);
			long guestBookingsDeleted = bookingGuestService.deleteByBookingId(bookingId);
			return new ResponseEntity<>("Removed (" + guestBookingsDeleted + ") Guest Bookings with ID: " + bookingIdString, HttpStatus.OK);

		} catch(IllegalArgumentException | NullPointerException err) {
			String errorMessage = "Cannot process BookingID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()).replaceAll("[\"]", "");
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);

		}
	}

	@DeleteMapping("/users/{bookingIdString}")
	public ResponseEntity<Object> deleteBookingUsers(@PathVariable String bookingIdString)
	throws ConnectException, SQLException  {

		try {
			Integer bookingId = Integer.parseInt(bookingIdString);
			long userBookingsDeleted = bookingUserService.deleteByBookingId(bookingId);
			return new ResponseEntity<>("Removed (" + userBookingsDeleted + ") Guest Bookings with ID: " + bookingIdString, HttpStatus.OK);

		} catch(IllegalArgumentException | NullPointerException err) {
			String errorMessage = "Cannot process BookingID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()).replaceAll("[\"]", "");
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);

		}
	}

	@ExceptionHandler(ConnectException.class)
	public ResponseEntity<Object> invalidConnection() {
		return new ResponseEntity<>(new HttpError("Service unavailable.", 503), HttpStatus.SERVICE_UNAVAILABLE);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> invalidMessage() {
		return new ResponseEntity<>(new HttpError("Invalid http request body.", 404), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<Object> invalidSQL() {
		return new ResponseEntity<>(new HttpError("Service unavailable.", 503), HttpStatus.SERVICE_UNAVAILABLE);
	}
}
