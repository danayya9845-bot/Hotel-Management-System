package com.hotel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.entity.Bill;
import com.hotel.repository.BillRepository;
import com.hotel.service.implementation.IBillingService;

@Service
public class BillingService implements IBillingService {

	@Autowired
    private final BillRepository billRepository;

    
    public BillingService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    @Override
    public String generateBill(Bill bill) {
        return billRepository.generateBill(bill);
    }

    @Override
    public List<Bill> fetchBillsByCustomer(int customerId) {
        return billRepository.fetchBillsByCustomer(customerId);
    }

    @Override
    public double fetchTotalRevenue() {
        return billRepository.fetchTotalRevenue();
    }
}
