package com.hotel.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hotel.entity.Employee;
import com.hotel.repository.implementation.IEmployeeRepository;

@Repository
public class EmployeeRepository implements IEmployeeRepository {

    private final String addEmployeeQuery =
            "INSERT INTO EMPLOYEE (EMPLOYEE_ID, NAME, DEPARTMENT, BASIC_SALARY) VALUES (?, ?, ?, ?)";
    private final String updateEmployeeQuery =
            "UPDATE EMPLOYEE SET NAME=?, DEPARTMENT=?, BASIC_SALARY=? WHERE EMPLOYEE_ID=?";
    private final String deleteEmployeeQuery =
            "DELETE FROM EMPLOYEE WHERE EMPLOYEE_ID=?";
    private final String fetchEmployeesQuery =
            "SELECT EMPLOYEE_ID, NAME, DEPARTMENT, BASIC_SALARY FROM EMPLOYEE";

    @Autowired
    private DataSource dataSource;

    @Override
    public String addEmployee(Employee employee) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(addEmployeeQuery)) {

            ps.setInt(1, employee.getId());
            ps.setString(2, employee.getName());
            ps.setString(3, employee.getDepartment());
            ps.setDouble(4, employee.getBasicSalary());

            ps.executeUpdate();
            return "Employee added successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error adding employee: " + e.getMessage();
        }
    }

    @Override
    public String updateEmployee(int id, Employee employee) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(updateEmployeeQuery)) {

            ps.setString(1, employee.getName());
            ps.setString(2, employee.getDepartment());
            ps.setDouble(3, employee.getBasicSalary());
            ps.setInt(4, id);

            ps.executeUpdate();
            return "Employee updated successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error updating employee: " + e.getMessage();
        }
    }

    @Override
    public String deleteEmployee(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(deleteEmployeeQuery)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return "Employee deleted successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error deleting employee: " + e.getMessage();
        }
    }

    @Override
    public List<Employee> fetchEmployees() {
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(fetchEmployeesQuery);
             ResultSet resultSet = ps.executeQuery()) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // ✅ fix index from 1 to columnCount
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + "\t");
            }
            System.out.println();

            while (resultSet.next()) {
                Employee emp = new Employee();
                emp.setId(resultSet.getInt("EMPLOYEE_ID"));
                emp.setName(resultSet.getString("NAME"));
                emp.setDepartment(resultSet.getString("DEPARTMENT"));
                emp.setBasicSalary(resultSet.getDouble("BASIC_SALARY"));

               
                System.out.println(
                        emp.getId() + "\t" +
                        emp.getName() + "\t" +
                        emp.getDepartment() + "\t" +
                        emp.getBasicSalary()
                );

                employees.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employees;
    }
}
