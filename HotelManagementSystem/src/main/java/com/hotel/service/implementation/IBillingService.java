package com.hotel.service.implementation;

import java.util.List;

import com.hotel.entity.Bill;

public interface IBillingService {
	  String generateBill(Bill bill);
	    List<Bill> fetchBillsByCustomer(int customerId);
	    double fetchTotalRevenue();
}
