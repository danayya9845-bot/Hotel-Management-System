package com.hotel.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hotel.entity.Room;
import com.hotel.repository.implementation.IRoomRepository;

@Repository
public class RoomRepository implements IRoomRepository {

    @Autowired
    private DataSource dataSource;

   
    private static final String ADD_ROOM = 
        "INSERT INTO ROOM (ROOM_ID, TYPE, PRICE, AVAILABLE) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_ROOM = 
        "UPDATE ROOM SET TYPE=?, PRICE=?, AVAILABLE=? WHERE ROOM_ID=?";
    private static final String DELETE_ROOM = 
        "DELETE FROM ROOM WHERE ROOM_ID=?";
    private static final String FETCH_ALL_ROOMS = 
        "SELECT * FROM ROOM";
    @SuppressWarnings("unused")
	private static final String FETCH_AVAILABLE_ROOMS = 
        "SELECT ROOM_ID FROM ROOM WHERE AVAILABLE='Yes'";
    private static final String CHECK_AVAILABILITY = 
        "SELECT AVAILABLE FROM ROOM WHERE ROOM_ID=?";
    private static final String UPDATE_AVAILABILITY = 
        "UPDATE ROOM SET AVAILABLE=? WHERE ROOM_ID=?";

    @Override
    public String addRoom(Room room) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(ADD_ROOM)) {

            ps.setInt(1, room.getRoomid());
            ps.setString(2, room.getType());
            ps.setDouble(3, room.getPricePerNight());
            ps.setString(4, room.getAvailable());
            ps.executeUpdate();
            return "✅ Room added successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error adding room: " + e.getMessage();
        }
    }

    @Override
    public String updateRoom(int id, Room room) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_ROOM)) {

            ps.setString(1, room.getType());
            ps.setDouble(2, room.getPricePerNight());
            ps.setString(3, room.getAvailable());
            ps.setInt(4, id);
            ps.executeUpdate();
            return "✅ Room updated successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error updating room: " + e.getMessage();
        }
    }

    @Override
    public String deleteRoom(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_ROOM)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return "✅ Room deleted successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error deleting room: " + e.getMessage();
        }
    }

    @Override
    public List<Room> readRoom() {
        List<Room> rooms = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(FETCH_ALL_ROOMS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Room room = new Room();
                room.setRoomid(rs.getInt("ROOM_ID"));
                room.setType(rs.getString("TYPE"));
                room.setPricePerNight(rs.getDouble("PRICE"));
                room.setAvailable(rs.getString("AVAILABLE"));
                rooms.add(room);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rooms;
    }

    @Override
    public boolean isAvailable(int roomId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(CHECK_AVAILABILITY)) {

            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String availability = rs.getString("AVAILABLE");
                return "Yes".equalsIgnoreCase(availability);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String updateRoomAvailability(int roomId, boolean isAvailable) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_AVAILABILITY)) {

            ps.setString(1, isAvailable ? "Yes" : "No");
            ps.setInt(2, roomId);
            ps.executeUpdate();
            return "✅ Room availability updated successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error updating room availability: " + e.getMessage();
        }
    }
}
