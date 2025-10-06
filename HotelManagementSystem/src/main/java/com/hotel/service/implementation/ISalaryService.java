package com.hotel.service.implementation;

import java.util.List;

import com.hotel.entity.Salary;

public interface ISalaryService {
	 String calculateSalary(int employeeId);       
	    List<Salary> fetchSalaryHistory(int employeeId);  
	    double fetchTotalSalaryPayout();  
}
