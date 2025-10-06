package com.hotel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.entity.Room;
import com.hotel.repository.RoomRepository;
import com.hotel.service.implementation.IRoomService;

@Service
public class RoomService implements IRoomService {

	@Autowired
	private final RoomRepository roomRepository;

    
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public String addRoom(Room room) {
        return roomRepository.addRoom(room);
    }

    @Override
    public String updateRoom(Room room) {
        return roomRepository.updateRoom(room.getRoomid(), room);
    }

    @Override
    public String deleteRoom(int roomId) {
        return roomRepository.deleteRoom(roomId);
    }

    @Override
    public boolean isRoomAvailable(int roomId) {
        return roomRepository.isAvailable(roomId);
    }

    @Override
    public List<Room> fetchAllRooms() {
        return roomRepository.readRoom();
    }

    @Override
    public String updateRoomStatus(int roomId, boolean isAvailable) {
        return roomRepository.updateRoomAvailability(roomId, isAvailable);
    }
}
