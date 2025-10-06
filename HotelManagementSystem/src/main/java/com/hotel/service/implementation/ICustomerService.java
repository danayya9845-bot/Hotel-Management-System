package com.hotel.service.implementation;

import java.util.List;

import com.hotel.entity.Customer;

public interface ICustomerService {

	 String addCustomer(Customer customer);
	    String updateCustomer(Customer customer);
	    String deleteCustomer(int customerId);
	    List<Customer> fetchAllCustomers();
}
