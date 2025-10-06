package com.hotel.repository.implementation;

import java.util.List;

import com.hotel.entity.Customer;

public interface ICustomerRepository {

	public String addCustomer(Customer customer);
	public String updateCustomer(int id,Customer customer);
	public String deleteCustomer(int id);
	public List<Customer> fetchCustomerDetails();
	
}
