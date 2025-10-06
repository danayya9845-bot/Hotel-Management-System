package com.hotel.repository.implementation;

import java.util.List;

import com.hotel.entity.Booking;

public interface IBookingRepository {

	public String bookRoom(Booking booking);
	 public String cancelBooking(int bookingId, int roomId);
	 public List<Booking> fetchBookingHistory();
	
}
