package com.ss.utopia.repositories;

import javax.transaction.Transactional;

import com.ss.utopia.models.FlightBooking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightBookingRepository extends JpaRepository<FlightBooking, Integer> {

  @Modifying
  @Transactional
  @Query(value="DELETE from flight_bookings WHERE booking_id = ?1", nativeQuery=true)
  void deleteByBookingId(Integer bookingId);
}