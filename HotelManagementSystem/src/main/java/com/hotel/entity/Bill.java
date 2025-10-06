package com.hotel.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill {

	private int id;
	private int bookingId;
	private int customerId;
	private double roomCharges;
	private double taxes;
	private double discount;
	private double totalAmount;
	private LocalDate billDate;
}
