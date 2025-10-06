package com.hotel.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
	
	private int id;
	private int roomId;
	private int customerId;
	private double totalAmount;
	private  LocalDate startDate;
    private 	 LocalDate endDate;
	private String status;
	private double discount;
	
	
	
	
	

}
