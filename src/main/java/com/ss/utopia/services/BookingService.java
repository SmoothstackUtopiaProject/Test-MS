package com.ss.utopia.services;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.exceptions.BookingAlreadyExistsException;
import com.ss.utopia.exceptions.BookingGuestNotFoundException;
import com.ss.utopia.exceptions.BookingNotFoundException;
import com.ss.utopia.exceptions.BookingUserNotFoundException;
import com.ss.utopia.exceptions.FlightNotFoundException;
import com.ss.utopia.models.Booking;
import com.ss.utopia.models.BookingGuest;
import com.ss.utopia.models.BookingUser;
import com.ss.utopia.models.BookingWithReferenceData;
import com.ss.utopia.models.FlightBooking;
import com.ss.utopia.models.Passenger;
import com.ss.utopia.repositories.FlightBookingRepository;
import com.ss.utopia.repositories.BookingRepository;
import com.ss.utopia.repositories.PassengerRepository;

@Service
public class BookingService {
	
	@Autowired
	BookingRepository bookingRepository;

	@Autowired 
	BookingGuestService bookingGuestService;

	@Autowired 
	BookingUserService bookingUserService;

	@Autowired 
	FlightBookingRepository flightBookingRepository;

	@Autowired
	FlightService flightService;

	@Autowired 
	PassengerRepository passengerRepository;


	public List<Booking> findAll() throws ConnectException, IllegalArgumentException, SQLException {
		return bookingRepository.findAll();
	}

	public List<BookingWithReferenceData> findAllWithReferenceData() throws ConnectException, IllegalArgumentException, SQLException {
		List<Booking> bookings = bookingRepository.findAll();
		List<BookingGuest> bookingGuests = bookingGuestService.findAll();
		List<BookingUser> bookingUsers = bookingUserService.findAll();
		List<FlightBooking> flightBookings = flightBookingRepository.findAll();
		List<Passenger> passengers = passengerRepository.findAll();

		List<BookingWithReferenceData> bookingsWithNames = new ArrayList<BookingWithReferenceData>();
		for(Booking booking : bookings) {
			BookingWithReferenceData newBookingWithReferenceData = new BookingWithReferenceData(booking.getId(), booking.getStatus(), booking.getConfirmationCode(), 0, 0, 0);
			
			for(BookingGuest bookingGuest : bookingGuests) {
				if(bookingGuest.getBookingId().equals(newBookingWithReferenceData.getId())) {
					newBookingWithReferenceData.setGuestEmail(bookingGuest.getEmail());
					newBookingWithReferenceData.setGuestPhone(bookingGuest.getPhone());
				}
			}

			for(BookingUser bookingUser : bookingUsers) {
				if(bookingUser.getBookingId().equals(newBookingWithReferenceData.getId())) {
					newBookingWithReferenceData.setUserId(bookingUser.getUserId());
				}
			}

			for(FlightBooking flightBooking : flightBookings) {
				if(flightBooking.getBookingId().equals(newBookingWithReferenceData.getId())) {
					newBookingWithReferenceData.setFlightId(flightBooking.getFlightId());
				}
			}
			
			for(Passenger passenger : passengers) {
				if(passenger.getBookingId().equals(newBookingWithReferenceData.getId())) {
					newBookingWithReferenceData.setPassengerId(passenger.getId());
				}
			}
			bookingsWithNames.add(newBookingWithReferenceData);
		}
		return bookingsWithNames;
	}

	public Booking findById(Integer bookingId) throws BookingNotFoundException, 
	ConnectException, IllegalArgumentException, SQLException {
		
		Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
		if(!optionalBooking.isPresent()) throw new BookingNotFoundException("No booking with ID: " + bookingId + " exist!");
		return optionalBooking.get();
	}

	public BookingWithReferenceData findByIdWithReferenceData(Integer bookingId) throws BookingNotFoundException, 
	ConnectException, IllegalArgumentException, SQLException {
		
		Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
		if(!optionalBooking.isPresent()) throw new BookingNotFoundException("No booking with ID: " + bookingId + " exist!");
		Booking booking = optionalBooking.get();
		BookingWithReferenceData bookingWithReferenceData = new BookingWithReferenceData(booking.getId(), booking.getStatus(), booking.getConfirmationCode(), 0, 0, 0, "", "");

		Optional<FlightBooking> optionalFlightBooking = flightBookingRepository.findById(bookingId);
		if(optionalFlightBooking.isPresent()) bookingWithReferenceData.setFlightId(optionalFlightBooking.get().getFlightId());

		// List<Passenger> passengersList = passengerRepository.findByBookingId(bookingId);
		// if(passengersList.isEmpty()) bookingWithReferenceData.setPassengerId(passengersList.get().getId());

		try {
			BookingUser bookingUser = bookingUserService.findByBookingId(bookingId);
			bookingWithReferenceData.setUserId(bookingUser.getUserId());
		} catch(BookingUserNotFoundException err){/* Nothing needed if not exists */}
		
		try {
			BookingGuest bookingGuest = bookingGuestService.findByBookingId(bookingId);
			bookingWithReferenceData.setGuestEmail(bookingGuest.getEmail());
			bookingWithReferenceData.setGuestPhone(bookingGuest.getPhone());
		} catch(BookingGuestNotFoundException err2){/* Nothing needed if not exists */}
		
		return bookingWithReferenceData;
	}
	
	public Booking findByConfirmationCode(String confirmationCode) throws BookingNotFoundException, 
	ConnectException, IllegalArgumentException, SQLException {

		Optional<Booking> optionalBooking = bookingRepository.findByConfirmationCode(confirmationCode);
		if(!optionalBooking.isPresent()) throw new BookingNotFoundException("No booking with Confirmation Code: " + confirmationCode + " exist!");
		return optionalBooking.get();
	}

	public List<Booking> findByStatus(Integer statusId) throws ConnectException, 
	IllegalArgumentException, SQLException {
		return bookingRepository.findByStatus(statusId);
	}

	public Booking insertByBookingUser(Integer userId) throws BookingAlreadyExistsException,
	BookingUserNotFoundException, ConnectException, IllegalArgumentException, SQLException {

		bookingUserService.findUserByUserId(userId);
		Booking newBooking = bookingRepository.save(new Booking(0));
		bookingUserService.insert(newBooking.getId(), userId);
		return newBooking;
	}

	public Booking insertByBookingGuest(String email, String phone) throws BookingAlreadyExistsException,
	 ConnectException, IllegalArgumentException, SQLException {

		Booking newBooking = bookingRepository.save(new Booking(2));
		bookingGuestService.insert(newBooking.getId(), email, phone);
		return newBooking;
	}

	public Booking update(Integer bookingId, Integer status) 
	throws BookingNotFoundException, ConnectException, IllegalArgumentException, SQLException {

		Booking booking = findById(bookingId);
		booking.setstatus(status);
		return bookingRepository.save(booking);
	}

	public FlightBooking updateFlightBooking(Integer bookingId, Integer flightId) throws BookingNotFoundException, 
	ConnectException, FlightNotFoundException, IllegalArgumentException, SQLException {

		findById(bookingId);
		flightService.findById(flightId);
		try {
			Optional<FlightBooking> optionalFlightBooking = flightBookingRepository.findById(bookingId);
			if(!optionalFlightBooking.isPresent()) throw new IllegalArgumentException();
			FlightBooking flightBooking = optionalFlightBooking.get();
			flightBooking.setFlightId(flightId);
			return flightBookingRepository.save(flightBooking);
		} catch(IllegalArgumentException err) {
			return flightBookingRepository.save(new FlightBooking(flightId, bookingId));
		}
	}

	public void delete(Integer id) throws BookingNotFoundException, 
	ConnectException, IllegalArgumentException, SQLException {
		findById(id);
		bookingRepository.deleteById(id);
	}

	public long deleteFlightBooking(Integer bookingId) throws BookingNotFoundException, 
	ConnectException, IllegalArgumentException, SQLException {

		flightBookingRepository.findById(bookingId);
		long preRowsCount = flightBookingRepository.count();
		flightBookingRepository.deleteByBookingId(bookingId);
		long postRowsCount = flightBookingRepository.count();
		return preRowsCount - postRowsCount;
	}
}