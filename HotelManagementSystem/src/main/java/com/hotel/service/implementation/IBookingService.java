package com.hotel.service.implementation;

import java.util.List;

import com.hotel.entity.Booking;

public interface IBookingService {

	String bookRoom(Booking booking);
    String cancelBooking(int bookingId, int roomId);
    List<Booking> fetchBookingHistory();
}
