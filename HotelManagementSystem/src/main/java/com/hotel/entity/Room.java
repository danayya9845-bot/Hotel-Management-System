package com.hotel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {

	private int Roomid;
	private String type;
	private double pricePerNight;
	private String available;

	public Room(String type, double pricePerNight, String isAvailable, String available) {
		super();
		this.type = type;
		this.pricePerNight = pricePerNight;
		this.available = available;
	}
}
