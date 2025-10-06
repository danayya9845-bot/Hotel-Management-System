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

import com.hotel.entity.Salary;
import com.hotel.repository.implementation.ISalaryRepository;

@Repository
public class SalaryRepository implements ISalaryRepository {

    private final String insertSalaryQuery =
            "INSERT INTO SALARY (SALARY_ID, EMPLOYEE_ID, HRA, DA, TAX, NET_SALARY, PAY_DATE) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private final String fetchSalaryHistoryQuery =
            "SELECT SALARY_ID, EMPLOYEE_ID, HRA, DA, TAX, NET_SALARY, PAY_DATE FROM SALARY WHERE EMPLOYEE_ID=?";

    private final String fetchTotalPayoutQuery =
            "SELECT SUM(NET_SALARY) AS PAYOUT FROM SALARY";

    @Autowired
    private DataSource dataSource;

    @Override
    public String generateSalary(Salary salary) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(insertSalaryQuery)) {

            ps.setInt(1, salary.getId());
            ps.setInt(2, salary.getEmployeeId());
            ps.setDouble(3, salary.getHra());
            ps.setDouble(4, salary.getDa());
            ps.setDouble(5, salary.getTax());
            ps.setDouble(6, salary.getNetSalary());
            ps.setDate(7, java.sql.Date.valueOf(salary.getPayDate()));

            ps.executeUpdate();
            return "Salary generated successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating salary: " + e.getMessage();
        }
    }

    @Override
    public List<Salary> fetchSalaryHistory(int employeeId) {
        List<Salary> salaries = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(fetchSalaryHistoryQuery)) {

            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + "\t");
            }
     

            while (rs.next()) {
                Salary salary = new Salary();
                salary.setId(rs.getInt("SALARY_ID"));
                salary.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
                salary.setHra(rs.getDouble("HRA"));
                salary.setDa(rs.getDouble("DA"));
                salary.setTax(rs.getDouble("TAX"));
                salary.setNetSalary(rs.getDouble("NET_SALARY"));
                salary.setPayDate(rs.getDate("PAY_DATE").toLocalDate());

                System.out.println(
                        salary.getId() + "\t" +
                        salary.getEmployeeId() + "\t" +
                        salary.getHra() + "\t" +
                        salary.getDa() + "\t" +
                        salary.getTax() + "\t" +
                        salary.getNetSalary() + "\t" +
                        salary.getPayDate()
                );

                salaries.add(salary);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return salaries;
    }

    @Override
    public double fetchTotalSalaryPayout() {
        double payout = 0.0;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(fetchTotalPayoutQuery);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                payout = rs.getDouble("PAYOUT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return payout;
    }
}
