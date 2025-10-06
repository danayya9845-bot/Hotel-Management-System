package com.hotel.repository.implementation;

import java.util.List;

import com.hotel.entity.Bill;

public interface IBillRepository {

	  public String generateBill(Bill bill);
	  public  List<Bill> fetchBillsByCustomer(int customerId);
	  public  double fetchTotalRevenue();
}
