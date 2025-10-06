package com.hotel.service.implementation;

import java.util.List;

import com.hotel.entity.Employee;

public interface IEmployeeService {

	String addEmployee(Employee employee);
    String updateEmployee(Employee employee);
    String deleteEmployee(int employeeId);
    List<Employee> fetchAllEmployees();
}
