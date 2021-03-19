package com.ss.utopia.controllers;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.Map;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ss.utopia.exceptions.BookingNotFoundException;
import com.ss.utopia.exceptions.BookingUserNotFoundException;
import com.ss.utopia.models.Booking;
import com.ss.utopia.models.HttpError;
import com.ss.utopia.models.BookingWithReferenceData;
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
		: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/referencedata")
	public ResponseEntity<Object> findAllWithReferenceData() 
	throws ConnectException, SQLException {

		List<BookingWithReferenceData> bookingList = bookingService.findAllWithReferenceData();
		return !bookingList.isEmpty() 
			? new ResponseEntity<>(bookingList, HttpStatus.OK)
			: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("{path}")
	public ResponseEntity<Object> findByBookingId(@PathVariable String path)
	throws ConnectException, SQLException {

		try {
			Integer bookingId = Integer.parseInt(path);
			BookingWithReferenceData bookingWithReferenceData = bookingService.findByIdWithReferenceData(bookingId);
			return new ResponseEntity<>(bookingWithReferenceData, HttpStatus.OK);

		} catch(IllegalArgumentException err) {
			String errorMessage = "Cannot process Booking ID " + err.getMessage()
			.substring(0, 1).toLowerCase(Locale.getDefault()) + err.getMessage()
			.substring(1, err.getMessage().length()).replaceAll("[\"]", "");
			return new ResponseEntity<>(new HttpError(
				errorMessage, HttpStatus.BAD_REQUEST.value()),
				HttpStatus.BAD_REQUEST
			);
			
		} catch(BookingNotFoundException err) {
			return new ResponseEntity<>(new HttpError(
				err.getMessage(), HttpStatus.NOT_FOUND.value()), 
				HttpStatus.NOT_FOUND
			);
		}
	}

	@PostMapping("/search")
	public ResponseEntity<Object> findBySearchAndFilter(@RequestBody Map<String, String> filterMap)
	throws ConnectException, SQLException {

		List<BookingWithReferenceData> bookings = bookingService.findBySearchAndFilter(filterMap);
		return !bookings.isEmpty()
			? new ResponseEntity<>(bookings, HttpStatus.OK)
			: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping()
	public ResponseEntity<Object> insert(@RequestBody Map<String, String> bookingMap)
	throws ConnectException, SQLException {

		try {
			BookingWithReferenceData newBooking = bookingService.insert(bookingMap);
			return new ResponseEntity<>(newBooking, HttpStatus.CREATED);

		} catch(BookingUserNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 409), HttpStatus.CONFLICT);
		
		} catch(IllegalArgumentException | NullPointerException err) {
			String errorMessage = "Cannot process User ID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()).replaceAll("[\"]", "");
			return new ResponseEntity<>(new HttpError(errorMessage, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
		} 
	}

	@PutMapping()
	public ResponseEntity<Object> update(@RequestBody Map<String, String> bookingMap)
	throws ConnectException, SQLException {

		try {
			BookingWithReferenceData updatedBooking = bookingService.update(bookingMap);
			return new ResponseEntity<>(updatedBooking, HttpStatus.ACCEPTED);

		} catch(BookingNotFoundException | BookingUserNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
		
		} catch(IllegalArgumentException | NullPointerException err) {
			String errorMessage = "Cannot process User ID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()).replaceAll("[\"]", "");
			return new ResponseEntity<>(new HttpError(errorMessage, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
		} 
	}

	@DeleteMapping("{bookingIdString}")
	public ResponseEntity<Object> delete(@PathVariable String bookingIdString)
	throws ConnectException, SQLException  {

		try {
			Integer bookingId = Integer.parseInt(bookingIdString);
			bookingService.delete(bookingId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		} catch(IllegalArgumentException | NullPointerException err) {
			String errorMessage = "Cannot process BookingID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()).replaceAll("[\"]", "");
			return new ResponseEntity<>(new HttpError(errorMessage, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);

		} catch(BookingNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
		}
	}

	@ExceptionHandler(ConnectException.class)
	public ResponseEntity<Object> invalidConnection() {
		return new ResponseEntity<>(new HttpError("Service unavailable.", 503), HttpStatus.SERVICE_UNAVAILABLE);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> invalidMessage() {
		return new ResponseEntity<>(new HttpError("Invalid http request body.", HttpStatus.NOT_FOUND.value()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<Object> invalidSQL() {
		return new ResponseEntity<>(new HttpError("Service unavailable.", 503), HttpStatus.SERVICE_UNAVAILABLE);
	}
}
