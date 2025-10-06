package com.hotel.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Salary {

	private int id;
	private int employeeId;
	private double hra;
	private double da;
	private double tax;
	private double netSalary;
	private LocalDate payDate;
}
