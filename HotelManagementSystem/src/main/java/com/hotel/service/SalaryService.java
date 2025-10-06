package com.hotel.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.entity.Employee;
import com.hotel.entity.Salary;
import com.hotel.repository.EmployeeRepository;
import com.hotel.repository.SalaryRepository;
import com.hotel.service.implementation.ISalaryService;

@Service
public class SalaryService implements ISalaryService {

	@Autowired
    private final EmployeeRepository employeeRepository;
	@Autowired
    private final SalaryRepository salaryRepository;

    
    public SalaryService(EmployeeRepository employeeRepository, SalaryRepository salaryRepository) {
        this.employeeRepository = employeeRepository;
        this.salaryRepository = salaryRepository;
    }

    @Override
    public String calculateSalary(int employeeId) {
        Employee emp = employeeRepository.fetchEmployees().stream()
                .filter(e -> e.getId() == employeeId)
                .findFirst()
                .orElse(null);

        if (emp == null) return "Employee not found!";

        double basic = emp.getBasicSalary();

        // HRA, DA, Tax calculation
        double hra = basic * 0.20;
        double da = basic * 0.10;
        double tax = (basic + hra + da) * 0.10;
        double netSalary = basic + hra + da - tax;

        Salary salary = new Salary();
        salary.setEmployeeId(employeeId);
        salary.setHra(hra);
        salary.setDa(da);
        salary.setTax(tax);
        salary.setNetSalary(netSalary);
        salary.setPayDate(LocalDate.now());

        return salaryRepository.generateSalary(salary);
    }

    @Override
    public List<Salary> fetchSalaryHistory(int employeeId) {
        return salaryRepository.fetchSalaryHistory(employeeId);
    }

    @Override
    public double fetchTotalSalaryPayout() {
        return salaryRepository.fetchTotalSalaryPayout();
    }
}
