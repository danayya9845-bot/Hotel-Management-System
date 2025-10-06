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

import com.hotel.entity.Bill;
import com.hotel.repository.implementation.IBillRepository;

@Repository
public class BillRepository implements IBillRepository {

    private final String insertBillQuery =
            "INSERT INTO BILL (BILL_ID, BOOKING_ID, CUSTOMER_ID, ROOM_CHARGES, TAXES, DISCOUNT, TOTAL_AMOUNT, BILL_DATE) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private final String fetchBillsByCustomerQuery =
            "SELECT BILL_ID, BOOKING_ID, CUSTOMER_ID, ROOM_CHARGES, TAXES, DISCOUNT, TOTAL_AMOUNT, BILL_DATE " +
            "FROM BILL WHERE CUSTOMER_ID=?";

    private final String fetchTotalRevenueQuery =
            "SELECT SUM(TOTAL_AMOUNT) AS REVENUE FROM BILL";

    @Autowired
    private DataSource dataSource;

    @Override
    public String generateBill(Bill bill) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(insertBillQuery)) {

            ps.setInt(1, bill.getId());
            ps.setInt(2, bill.getBookingId());
            ps.setInt(3, bill.getCustomerId());
            ps.setDouble(4, bill.getRoomCharges());
            ps.setDouble(5, bill.getTaxes());
            ps.setDouble(6, bill.getDiscount());
            ps.setDouble(7, bill.getTotalAmount());
            ps.setDate(8, java.sql.Date.valueOf(bill.getBillDate()));

            ps.executeUpdate();
            return "Bill generated successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating bill: " + e.getMessage();
        }
    }

    @Override
    public List<Bill> fetchBillsByCustomer(int customerId) {
        List<Bill> bills = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(fetchBillsByCustomerQuery)) {

            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // ✅ Print column names properly
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + "\t");
            }
            System.out.println();

            while (rs.next()) {
                Bill bill = new Bill();
                bill.setId(rs.getInt("BILL_ID"));
                bill.setBookingId(rs.getInt("BOOKING_ID"));
                bill.setCustomerId(rs.getInt("CUSTOMER_ID"));
                bill.setRoomCharges(rs.getDouble("ROOM_CHARGES"));
                bill.setTaxes(rs.getDouble("TAXES"));
                bill.setDiscount(rs.getDouble("DISCOUNT"));
                bill.setTotalAmount(rs.getDouble("TOTAL_AMOUNT"));
                bill.setBillDate(rs.getDate("BILL_DATE").toLocalDate());

                System.out.println(
                        bill.getId() + "\t" +
                        bill.getBookingId() + "\t" +
                        bill.getCustomerId() + "\t" +
                        bill.getRoomCharges() + "\t" +
                        bill.getTaxes() + "\t" +
                        bill.getDiscount() + "\t" +
                        bill.getTotalAmount() + "\t" +
                        bill.getBillDate()
                );

                bills.add(bill);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bills;
    }

    @Override
    public double fetchTotalRevenue() {
        double revenue = 0.0;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(fetchTotalRevenueQuery);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                revenue = rs.getDouble("REVENUE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return revenue;
    }
}
