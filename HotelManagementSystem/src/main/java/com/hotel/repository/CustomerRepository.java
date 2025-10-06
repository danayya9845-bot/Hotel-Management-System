package com.hotel.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hotel.entity.Customer;
import com.hotel.repository.implementation.ICustomerRepository;

@Repository
public class CustomerRepository implements ICustomerRepository {

    private static final String ADD_CUSTOMER =
        "INSERT INTO CUSTOMER (CUSTOMER_ID, NAME, EMAIL, PHONE) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_CUSTOMER =
        "UPDATE CUSTOMER SET NAME=?, EMAIL=?, PHONE=? WHERE CUSTOMER_ID=?";
    private static final String DELETE_CUSTOMER =
        "DELETE FROM CUSTOMER WHERE CUSTOMER_ID=?";
    private static final String FETCH_CUSTOMERS =
        "SELECT * FROM CUSTOMER";

    @Autowired
    private DataSource dataSource;

    @Override
    public String addCustomer(Customer customer) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(ADD_CUSTOMER)) {

            ps.setInt(1, customer.getCustomerId());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getPhone());

            int rows = ps.executeUpdate();
            return rows > 0 ? "✅ Customer inserted successfully!" 
                            : "⚠️ Customer insert failed!";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error adding customer: " + e.getMessage();
        }
    }

    @Override
    public String updateCustomer(int id, Customer customer) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_CUSTOMER)) {

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPhone());
            ps.setInt(4, id);

            int rows = ps.executeUpdate();
            return rows > 0 ? "✅ Customer updated successfully!"
                            : "⚠️ Customer update failed!";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error updating customer: " + e.getMessage();
        }
    }

    @Override
    public String deleteCustomer(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_CUSTOMER)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0 ? "✅ Customer deleted successfully!"
                            : "⚠️ Customer delete failed!";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error deleting customer: " + e.getMessage();
        }
    }

    @Override
    public List<Customer> fetchCustomerDetails() {
        List<Customer> customers = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(FETCH_CUSTOMERS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("CUSTOMER_ID"));
                customer.setName(rs.getString("NAME"));
                customer.setEmail(rs.getString("EMAIL"));
                customer.setPhone(rs.getString("PHONE"));
                customers.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }
}
