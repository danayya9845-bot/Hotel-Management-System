package com.hotel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.entity.Bill;
import com.hotel.entity.Booking;
import com.hotel.entity.Room;
import com.hotel.repository.BookingRepository;
import com.hotel.service.implementation.IBookingService;

@Service
public class BookingService implements IBookingService {

	 @Autowired
    private final BookingRepository bookingRepository;
	 @Autowired
    private final RoomService roomService;
	 @Autowired
    private final BillingService billingService;

    private final double discountRate = 0.10;
    private final double taxRate = 0.12;
    
    public BookingService(BookingRepository bookingRepository, RoomService roomService, BillingService billingService) {
        this.bookingRepository = bookingRepository;
        this.roomService = roomService;
        this.billingService = billingService;
    }

    @Override
    public String bookRoom(Booking booking) {
        if (!roomService.isRoomAvailable(booking.getRoomId())) {
            return "Room is already booked!";
        }

        
        List<Booking> history = bookingRepository.fetchBookingHistory();
        for (Booking b : history) {
            if (b.getRoomId() == booking.getRoomId() &&
                !(booking.getEndDate().isBefore(b.getStartDate()) || booking.getStartDate().isAfter(b.getEndDate()))) {
                return "Room is already booked for this date range!";
            }
        }

        bookingRepository.bookRoom(booking);
        roomService.updateRoomStatus(booking.getRoomId(), false);

        Room room = roomService.fetchAllRooms().stream()
                .filter(r -> r.getRoomid() == booking.getRoomId())
                .findFirst()
                .orElse(null);

        if (room == null) return "Room not found for billing!";

        long nights = java.time.temporal.ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
        double roomCharges = room.getPricePerNight() * nights;
        double discount = roomCharges * discountRate;
        double taxes = (roomCharges - discount) * taxRate;
        double totalAmount = Math.round((roomCharges - discount + taxes) * 100.0) / 100.0; // round to 2 decimals

        Bill bill = new Bill();
        bill.setBookingId(booking.getId());
        bill.setCustomerId(booking.getCustomerId());
        bill.setRoomCharges(roomCharges);
        bill.setDiscount(discount);
        bill.setTaxes(taxes);
        bill.setTotalAmount(totalAmount);
        bill.setBillDate(java.time.LocalDate.now());

        billingService.generateBill(bill);

        return "Room booked successfully! Total bill: " + totalAmount;
    }

    @Override
    public String cancelBooking(int bookingId, int roomId) {
        String result = bookingRepository.cancelBooking(bookingId, roomId);
        roomService.updateRoomStatus(roomId, true);
        return result;
    }

    @Override
    public List<Booking> fetchBookingHistory() {
        return bookingRepository.fetchBookingHistory();
    }
}
