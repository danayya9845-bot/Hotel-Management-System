package com.hotel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.entity.Customer;
import com.hotel.repository.CustomerRepository;
import com.hotel.service.implementation.ICustomerService;


@Service
public class CustomerService implements ICustomerService {

	 @Autowired
    private final CustomerRepository customerRepository;
    
   
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public String addCustomer(Customer customer) {
        return customerRepository.addCustomer(customer);
    }

    @Override
    public String updateCustomer(Customer customer) {
        return customerRepository.updateCustomer(customer.getCustomerId(), customer);
    }

    @Override
    public String deleteCustomer(int customerId) {
        return customerRepository.deleteCustomer(customerId);
    }

    @Override
    public List<Customer> fetchAllCustomers() {
        return customerRepository.fetchCustomerDetails();
    }
}
