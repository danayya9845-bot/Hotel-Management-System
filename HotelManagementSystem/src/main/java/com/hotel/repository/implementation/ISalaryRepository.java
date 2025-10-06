package com.hotel.repository.implementation;

import java.util.List;

import com.hotel.entity.Salary;

public interface ISalaryRepository {
  
	public  String generateSalary(Salary salary);
     public List<Salary> fetchSalaryHistory(int employeeId);
     public double fetchTotalSalaryPayout();
}
