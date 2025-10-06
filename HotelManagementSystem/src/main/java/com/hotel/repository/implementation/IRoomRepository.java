package com.hotel.repository.implementation;

import java.util.List;

import com.hotel.entity.Room;

public interface IRoomRepository {
 
	public String addRoom(Room room);
	public String updateRoom(int id,Room room);
	public String deleteRoom(int id);
	public List<Room> readRoom();
	public boolean isAvailable(int roomId);
    public String updateRoomAvailability(int roomId, boolean isAvailable);
}
