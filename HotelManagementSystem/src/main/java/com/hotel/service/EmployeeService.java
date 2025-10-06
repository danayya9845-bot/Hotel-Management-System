package com.hotel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.entity.Employee;
import com.hotel.repository.EmployeeRepository;
import com.hotel.service.implementation.IEmployeeService;

@Service
public class EmployeeService implements IEmployeeService {

	@Autowired
    private final EmployeeRepository employeeRepository;

    
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public String addEmployee(Employee employee) {
        return employeeRepository.addEmployee(employee);
    }

    @Override
    public String updateEmployee(Employee employee) {
        return employeeRepository.updateEmployee(employee.getId(), employee);
    }

    @Override
    public String deleteEmployee(int employeeId) {
        return employeeRepository.deleteEmployee(employeeId);
    }

    @Override
    public List<Employee> fetchAllEmployees() {
        return employeeRepository.fetchEmployees();
    }
}
