package com.hotel.runners;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.hotel.HotelManagementSystemApplication;
import com.hotel.entity.*;
import com.hotel.service.*;

@Component
public class ConsoleRunner implements ApplicationRunner {

    @Autowired private CustomerService customerService;
    @Autowired private RoomService roomService;
    @Autowired private BookingService bookingService;
    @Autowired private BillingService billingService;
    @Autowired private EmployeeService employeeService;
    @Autowired private SalaryService salaryService;

    private Scanner sc = new Scanner(System.in);

    public ConsoleRunner(HotelManagementSystemApplication hotelManagementSystemApplication) {
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        while (true) {
            System.out.println("\n--- HOTEL MANAGEMENT SYSTEM ---");
            System.out.println("1. Customer Flow");
            System.out.println("2. Hotel Owner Flow");
            System.out.println("3. Exit");
            System.out.print("Choose flow: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> customerFlow();
                case 2 -> ownerFlow();
                case 3 -> {
                    System.out.println("Exiting system...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // ------------------- CUSTOMER FLOW -------------------
    private void customerFlow() {
        while (true) {
            System.out.println("\n--- CUSTOMER FLOW ---");
            System.out.println("1. Add Customer");
            System.out.println("2. Add Room");
            System.out.println("3. Book Room");
            System.out.println("4. Cancel Booking");
            System.out.println("5. View Booking History & Bills");
            System.out.println("6. Back");
            System.out.print("Choose option: ");
            int option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1 -> addCustomer();
                case 2 -> addRoom();
                case 3 -> bookRoom();
                case 4 -> cancelBooking();
                case 5 -> viewHistoryAndBills();
                case 6 -> { return; }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void addCustomer() {
        Customer customer = new Customer();
        System.out.print("Enter Customer Id: ");
        customer.setCustomerId(sc.nextInt());
        sc.nextLine();

        System.out.print("Enter customer name: ");
        customer.setName(sc.nextLine());

        System.out.print("Enter email: ");
        customer.setEmail(sc.nextLine());

        System.out.print("Enter phone: ");
        customer.setPhone(sc.nextLine());

        System.out.println(customerService.addCustomer(customer));
    }

    private void addRoom() {
        Room room = new Room();
        System.out.print("Enter Room Id: ");
        room.setRoomid(sc.nextInt());
        sc.nextLine();

        System.out.print("Enter room type: ");
        room.setType(sc.nextLine());

        System.out.print("Enter price per night: ");
        room.setPricePerNight(sc.nextDouble());
        sc.nextLine();

        System.out.print("Enter Room status (Yes/No): ");
        room.setAvailable(sc.nextLine());

        System.out.println(roomService.addRoom(room));
    }

    private void bookRoom() {
        Booking booking = new Booking();
        int newId = bookingService.fetchBookingHistory().stream()
                .mapToInt(Booking::getId)
                .max().orElse(0) + 1;
        booking.setId(newId);

        System.out.print("Enter customer ID: ");
        booking.setCustomerId(sc.nextInt());
        System.out.print("Enter room ID: ");
        booking.setRoomId(sc.nextInt());
        sc.nextLine();

        System.out.print("Enter start date (yyyy-mm-dd): ");
        booking.setStartDate(LocalDate.parse(sc.nextLine()));
        System.out.print("Enter end date (yyyy-mm-dd): ");
        booking.setEndDate(LocalDate.parse(sc.nextLine()));

        System.out.println(bookingService.bookRoom(booking));
    }

    private void cancelBooking() {
        System.out.print("Enter booking ID: ");
        int bookingId = sc.nextInt();
        System.out.print("Enter room ID: ");
        int roomId = sc.nextInt();
        sc.nextLine();

        System.out.println(bookingService.cancelBooking(bookingId, roomId));
    }

    private void viewHistoryAndBills() {
        List<Booking> bookings = bookingService.fetchBookingHistory();
        System.out.println("\n--- BOOKING HISTORY ---");
        bookings.forEach(b -> System.out.println(
                b.getId() + " | Customer: " + b.getCustomerId() + " | Room: " + b.getRoomId() + " | " +
                        b.getStartDate() + " to " + b.getEndDate()
        ));

        System.out.println("\n--- BILLS ---");
        bookings.forEach(b -> {
            List<Bill> bills = billingService.fetchBillsByCustomer(b.getCustomerId());
            bills.forEach(bill -> System.out.println(
                    "\nBooking: " + bill.getBookingId() + " | Total: " + bill.getTotalAmount() + " | Date: " + bill.getBillDate()
           +"\n" ));
        });
    }

    // ------------------- HOTEL OWNER FLOW -------------------
    private void ownerFlow() {
        while (true) {
            System.out.println("\n--- HOTEL OWNER FLOW ---");
            System.out.println("1. Add Employee");
            System.out.println("2. Calculate Payroll");
            System.out.println("3. Fetch All Salaries");
            System.out.println("4. Print Total Revenue");
            System.out.println("5. Print Total Salary Payouts");
            System.out.println("6. Calculate Profit/Loss");
            System.out.println("7. Back");
            System.out.print("Choose option: ");
            int option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1 -> addEmployee();
                case 2 -> calculatePayroll();
                case 3 -> fetchSalaries();
                case 4 -> printTotalRevenue();
                case 5 -> printTotalSalaryPayout();
                case 6 -> calculateProfitLoss();
                case 7 -> { return; }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void addEmployee() {
        Employee emp = new Employee();
        System.out.print("Enter the employee Id: ");
        emp.setId(sc.nextInt());
        sc.nextLine();

        System.out.print("Enter employee name: ");
        emp.setName(sc.nextLine());

        System.out.print("Enter department: ");
        emp.setDepartment(sc.nextLine());

        System.out.print("Enter basic salary: ");
        emp.setBasicSalary(sc.nextDouble());
        sc.nextLine();

        System.out.println(employeeService.addEmployee(emp));
    }

    private void calculatePayroll() {
        List<Employee> employees = employeeService.fetchAllEmployees();
       System.out.println();
       double sum = employees
        .stream()
         .mapToDouble(Employee::getBasicSalary)
         .sum();
       System.out.println("Total Payroll is => "+sum);
    }

    private void fetchSalaries() {
        List<Employee> employees = employeeService.fetchAllEmployees();
        employees.forEach(e -> {
            List<Salary> salaries = salaryService.fetchSalaryHistory(e.getId());
            System.out.println("Employee " + e.getId() + " Salary History:");
            salaries.forEach(s -> System.out.println(
                    "Net Salary: " + s.getNetSalary() + " | Pay Date: " + s.getPayDate()
            ));
        });
    }

    private void printTotalRevenue() {
        double revenue = billingService.fetchTotalRevenue();
        System.out.println("Total Booking Revenue: " + revenue);
    }

    private void printTotalSalaryPayout() {
        double total = salaryService.fetchTotalSalaryPayout();
        System.out.println("Total Salary Payout: " + total);
    }

    private void calculateProfitLoss() {
        double revenue = billingService.fetchTotalRevenue();
        double salary = salaryService.fetchTotalSalaryPayout();
        double profitLoss = revenue - salary;
        System.out.println("Profit/Loss: " + profitLoss);
     
    }
}
