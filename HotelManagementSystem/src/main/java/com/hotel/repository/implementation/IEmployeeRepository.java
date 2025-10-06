package com.hotel.repository.implementation;

import java.util.List;

import com.hotel.entity.Employee;

public interface IEmployeeRepository {

	 public String addEmployee(Employee employee);
	 public   String updateEmployee(int id, Employee employee);
	 public  String deleteEmployee(int id);
	 public   List<Employee> fetchEmployees();
}
