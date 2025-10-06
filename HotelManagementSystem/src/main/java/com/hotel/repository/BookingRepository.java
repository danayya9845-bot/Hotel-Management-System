package com.hotel.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hotel.entity.Booking;
import com.hotel.repository.implementation.IBookingRepository;

@Repository
public class BookingRepository implements IBookingRepository {

	private final String checkRoomAvailabilityQuery = "SELECT AVAILABLE FROM ROOM WHERE ROOM_ID=?";
	private final String bookRoomQuery = "INSERT INTO BOOKING (BOOKING_ID, ROOM_ID, CUSTOMER_ID, START_DATE, END_DATE, TOTAL_AMOUNT, STATUS) VALUES (?, ?, ?, ?, ?, ?, 'BOOKED')";
	private final String updateRoomStatusQuery = "UPDATE ROOM SET AVAILABLE=? WHERE ROOM_ID=?";
	private final String cancelBookingQuery = "UPDATE BOOKING SET STATUS='CANCELLED' WHERE BOOKING_ID=?";
	private final String fetchBookingHistoryQuery = "SELECT B.BOOKING_ID, B.ROOM_ID, R.TYPE, B.CUSTOMER_ID, C.NAME, B.START_DATE, B.END_DATE, B.TOTAL_AMOUNT, B.STATUS "
			+ "FROM BOOKING B " + "JOIN ROOM R ON B.ROOM_ID = R.ROOM_ID "
			+ "JOIN CUSTOMER C ON B.CUSTOMER_ID = C.CUSTOMER_ID";

	@Autowired
	private DataSource dataSource;

	@Override
	public String bookRoom(Booking booking) {
		try (Connection connection = dataSource.getConnection()) {

			try (PreparedStatement ps = connection.prepareStatement(checkRoomAvailabilityQuery)) {
				ps.setInt(1, booking.getRoomId());
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					String available = rs.getString("AVAILABLE");
					if ("No".equalsIgnoreCase(available)) {
						return "Room already booked!";
					}
				}
			}

			try (PreparedStatement ps = connection.prepareStatement(bookRoomQuery)) {
				ps.setInt(1, booking.getId());
				ps.setInt(2, booking.getRoomId());
				ps.setInt(3, booking.getCustomerId());
				ps.setDate(4, java.sql.Date.valueOf(booking.getStartDate()));
				ps.setDate(5, java.sql.Date.valueOf(booking.getEndDate()));
				ps.setDouble(6, booking.getTotalAmount());
				ps.executeUpdate();
			}

			try (PreparedStatement ps = connection.prepareStatement(updateRoomStatusQuery)) {
				ps.setString(1, "No");
				ps.setInt(2, booking.getRoomId());
				ps.executeUpdate();
			}

			return "Room booked successfully!";
		} catch (Exception e) {
			e.printStackTrace();
			return "Error booking room: " + e.getMessage();
		}
	}

	@Override
	public String cancelBooking(int bookingId, int roomId) {
		try (Connection connection = dataSource.getConnection()) {

			try (PreparedStatement ps = connection.prepareStatement(cancelBookingQuery)) {
				ps.setInt(1, bookingId);
				ps.executeUpdate();
			}

			try (PreparedStatement ps = connection.prepareStatement(updateRoomStatusQuery)) {
				ps.setString(1, "Yes");
				ps.setInt(2, roomId);
				ps.executeUpdate();
			}

			return "Booking cancelled successfully!";
		} catch (Exception e) {
			e.printStackTrace();
			return "Error cancelling booking: " + e.getMessage();
		}
	}

	@Override
	public List<Booking> fetchBookingHistory() {
		List<Booking> bookings = new ArrayList<>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(fetchBookingHistoryQuery);
				ResultSet rs = ps.executeQuery()) {

			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();

			for (int i = 1; i <= columnCount; i++) {
				System.out.print(metaData.getColumnName(i) + "\t");
			}
			System.out.println();

			while (rs.next()) {
				Booking booking = new Booking();
				booking.setId(rs.getInt("BOOKING_ID"));
				booking.setRoomId(rs.getInt("ROOM_ID"));
				booking.setCustomerId(rs.getInt("CUSTOMER_ID"));
				booking.setStartDate(rs.getDate("START_DATE").toLocalDate());
				booking.setEndDate(rs.getDate("END_DATE").toLocalDate());
				booking.setTotalAmount(rs.getDouble("TOTAL_AMOUNT"));
				booking.setStatus(rs.getString("STATUS"));

				System.out.println(booking.getId() + "\t" + booking.getRoomId() + "\t" + booking.getCustomerId() + "\t"
						+ booking.getStartDate() + "\t" + booking.getEndDate() + "\t" + booking.getTotalAmount() + "\t"
						+ booking.getStatus());

				bookings.add(booking);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bookings;
	}
}
