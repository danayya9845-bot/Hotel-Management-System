Hotel Management System - Final Project Documentation

1. Project Overview
The Hotel Management System is a fully layered Spring Boot application designed to manage:
•	Room bookings
•	Billing & invoices
•	Customer management
•	Staff payroll
•	Hotel revenue reporting

It follows a clean layered architecture with entity, repository, service, and runner layers.
The system also supports multi-database configuration (Oracle) for production use.

2. Key Features
Room Management – Add rooms, check availability, update status
Customer Management – Add and manage customers, view booking history
Booking Management – Book/cancel rooms, prevent overlaps
Billing – Generate bills with tax, discounts, and total
Payroll Management – Employee salary calculation (HRA, DA, Tax)
Revenue – Track total revenue, salary payouts, profit/loss
Profiles – Oracle DB (production-ready)

3. Database Schema & Sample Data
   
CREATE TABLE ROOM (
    ROOM_ID NUMBER(10) PRIMARY KEY,
    TYPE VARCHAR2(50),
    PRICE NUMBER(10,2),
    AVAILABLE VARCHAR2(5)
);


-- Sample data
INSERT INTO ROOM (ROOM_ID, TYPE, PRICE, AVAILABLE) VALUES (101, 'Single', 1000, 'Yes');
INSERT INTO ROOM (ROOM_ID, TYPE, PRICE, AVAILABLE) VALUES (102, 'Double', 1800, 'Yes');
INSERT INTO ROOM (ROOM_ID, TYPE, PRICE, AVAILABLE) VALUES (103, 'Suite', 3500, 'No');
INSERT INTO ROOM (ROOM_ID, TYPE, PRICE, AVAILABLE) VALUES (104, 'Deluxe', 2500, 'Yes');

=================================================================================================

CREATE TABLE CUSTOMER (
    CUSTOMER_ID NUMBER(5) PRIMARY KEY,
    NAME VARCHAR2(100),
    EMAIL VARCHAR2(100),
    PHONE VARCHAR2(20)
);

-- Sample data
INSERT INTO CUSTOMER (CUSTOMER_ID, NAME, EMAIL, PHONE) VALUES (1, 'John Doe', 'john@example.com', '9876543210');
INSERT INTO CUSTOMER (CUSTOMER_ID, NAME, EMAIL, PHONE) VALUES (2, 'Jane Smith', 'jane@example.com', '9876501234');
INSERT INTO CUSTOMER (CUSTOMER_ID, NAME, EMAIL, PHONE) VALUES (3, 'Robert Brown', 'robert@example.com', '9876512345');

=================================================================================================


CREATE TABLE EMPLOYEE (
    EMPLOYEE_ID NUMBER(5) PRIMARY KEY,
    NAME VARCHAR2(100),
    DEPARTMENT VARCHAR2(50),
    BASIC_SALARY NUMBER(10,2)
);


-- Sample data
INSERT INTO EMPLOYEE (EMPLOYEE_ID, NAME, DEPARTMENT, BASIC_SALARY) VALUES (1, 'Alice Johnson', 'Reception', 30000);
INSERT INTO EMPLOYEE (EMPLOYEE_ID, NAME, DEPARTMENT, BASIC_SALARY) VALUES (2, 'Bob Williams', 'Housekeeping', 25000);
INSERT INTO EMPLOYEE (EMPLOYEE_ID, NAME, DEPARTMENT, BASIC_SALARY) VALUES (3, 'Charlie Davis', 'Manager', 50000);

=================================================================================================
CREATE TABLE BOOKING (
    BOOKING_ID NUMBER(10) PRIMARY KEY,
    ROOM_ID NUMBER(10),
    CUSTOMER_ID NUMBER(5),
    START_DATE DATE,
    END_DATE DATE,
    TOTAL_AMOUNT NUMBER(10,2),
    STATUS VARCHAR2(20),
    CONSTRAINT FK_ROOM FOREIGN KEY (ROOM_ID) REFERENCES ROOM(ROOM_ID),
    CONSTRAINT FK_CUSTOMER FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMER(CUSTOMER_ID)
);
-- Sample data
INSERT INTO BOOKING (BOOKING_ID, ROOM_ID, CUSTOMER_ID, START_DATE, END_DATE, TOTAL_AMOUNT, STATUS)
VALUES (1001, 101, 1, TO_DATE('2025-10-01','YYYY-MM-DD'), TO_DATE('2025-10-05','YYYY-MM-DD'), 4000, 'BOOKED');

INSERT INTO BOOKING (BOOKING_ID, ROOM_ID, CUSTOMER_ID, START_DATE, END_DATE, TOTAL_AMOUNT, STATUS)
VALUES (1002, 102, 2, TO_DATE('2025-10-02','YYYY-MM-DD'), TO_DATE('2025-10-04','YYYY-MM-DD'), 3600, 'BOOKED');
=================================================================================================
CREATE TABLE SALARY (
    SALARY_ID NUMBER(10) PRIMARY KEY,
    EMPLOYEE_ID NUMBER(5),
    HRA NUMBER(10,2),
    DA NUMBER(10,2),
    TAX NUMBER(10,2),
    NET_SALARY NUMBER(10,2),
    PAY_DATE DATE,
    CONSTRAINT FK_SALARY_EMPLOYEE FOREIGN KEY (EMPLOYEE_ID) REFERENCES EMPLOYEE(EMPLOYEE_ID)
);
-- Sample data
INSERT INTO SALARY (SALARY_ID, EMPLOYEE_ID, HRA, DA, TAX, NET_SALARY, PAY_DATE)
VALUES (2001, 1, 5000, 2000, 3000, 27000, SYSDATE);

INSERT INTO SALARY (SALARY_ID, EMPLOYEE_ID, HRA, DA, TAX, NET_SALARY, PAY_DATE)
VALUES (2002, 2, 4000, 1500, 2000, 24500, SYSDATE);
=================================================================================================
CREATE TABLE BILL (
    BILL_ID NUMBER(10) PRIMARY KEY,
    BOOKING_ID NUMBER(10),
    CUSTOMER_ID NUMBER(5),
    ROOM_CHARGES NUMBER(10,2),
    TAXES NUMBER(10,2),
    DISCOUNT NUMBER(10,2),
    TOTAL_AMOUNT NUMBER(10,2),
    BILL_DATE DATE,
    CONSTRAINT FK_BOOKING FOREIGN KEY (BOOKING_ID) REFERENCES BOOKING(BOOKING_ID),
    CONSTRAINT FK_BILL_CUSTOMER FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMER(CUSTOMER_ID)
);

-- Sample data
INSERT INTO BILL (BILL_ID, BOOKING_ID, CUSTOMER_ID, ROOM_CHARGES, TAXES, DISCOUNT, TOTAL_AMOUNT, BILL_DATE)
VALUES (5001, 1001, 1, 4000, 400, 200, 4200, SYSDATE);

INSERT INTO BILL (BILL_ID, BOOKING_ID, CUSTOMER_ID, ROOM_CHARGES, TAXES, DISCOUNT, TOTAL_AMOUNT, BILL_DATE)
VALUES (5002, 1002, 2, 3600, 360, 100, 3860, SYSDATE);

4. Project Folder Structure
 HotelManagementSystem/
 ├── src/main/java/com/hotel/
 │   ├── entity/
 │   ├── repository/
 │   ├── service/
 │   ├── runner/
 │   └── HotelManagementSystemApplication.java
 ├── src/main/resources/
 │   ├── application.yml
 │   └── data.sql
 └── pom.xml

5. Repository Layer 
Room Repository – CRUD + availability 
Customer Repository – CRUD Booking Repository – Create/Cancel bookings, fetch history * * Billing Repository – Generate bills, fetch customer bills, hotel revenue
Employee Repository – CRUD employees 
Salary Repository – Calculate payroll, fetch salary history

6. Service Layer 

Room Service – Check availability, update room status 
Customer Service – Validate customer, fetch booking history 
Booking Service – Book/cancel rooms, prevent overlaps, call Billing Service 
Billing Service – Generate bill: room charges, tax, discount, total Amount, fetch revenue Employee Service – Add employees, fetch data 
Payroll Service – Calculate salary, fetch payroll reports

7. Console Runner 

Layer Customer Flow: 
1. Add customer
2. Add rooms 
3. Book rooms → generate bill 
4. Cancel booking → update availability 
5. View booking history & bills 
Hotel Owner Flow: 
1. Add employees 
2. Calculate payroll 
3. Fetch all salaries 
4. Print total revenue from bookings 
5. Print total salary payouts 
6. Calculate hotel profit/loss

8. Learning Outcomes 

Fully layered architecture: 
•	Entity → Repository → Service → Console Runner 
•	Database configuration (Oracle) using YML profiles 
•	JdbcTemplate CRUD and business logic integration 
•	Billing & payroll calculation in real-time simulation 
•	Profile-based discounts, Database management, and revenue reporting

9.Tech Stack
Backend: Java, Spring Boot
Database: Oracle
Tools: JDBC, Maven, Spring Data, YML Configuration

10.Author: Danayya
Year: 2025
A complete production-ready hotel management simulation system.
