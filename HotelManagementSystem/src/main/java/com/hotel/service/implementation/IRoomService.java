package com.hotel.service.implementation;

import java.util.List;

import com.hotel.entity.Room;

public interface IRoomService {

	 String addRoom(Room room);

	    String updateRoom(Room room);

	    String deleteRoom(int roomId);

	    boolean isRoomAvailable(int roomId);

	    List<Room> fetchAllRooms();

	    String updateRoomStatus(int roomId, boolean isAvailable);
}
