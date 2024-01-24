CREATE DATABASE PAY;
USE PAY;

CREATE TABLE Employee(
EmployeeID INT AUTO_INCREMENT PRIMARY KEY,
FirstName varchar (255),
LastName varchar (255),
DateOfBirth Date,
Gender varchar (10),
Email varchar (255),
PhoneNumber varchar(20),
address varchar(255),
Position varchar(255),
JoiningDate Date,
TerminationDate Date 
);

CREATE TABLE Payroll(
PayrollID INT auto_increment primary key,
EmployeeID int,
foreign key(EmployeeID) references Employee(EmployeeID),
PayPeriodStartDate Date,
PayPeriodEndDate Date,
BasicSalary Decimal(10,2),
OvertimePay Decimal(10,2),
Deductions Decimal(10,2),
NetSalary Decimal(10,2)
);

CREATE TABLE Tax(
TaxID INT AUTO_INCREMENT primary key,
EmployeeID INT,
foreign key(EmployeeID) references Employee(EmployeeID),
TaxYear year,
TaxableIncome Decimal(10,2),
TaxAmount Decimal(10,2)
);

CREATE TABLE FinancialRecord(
RecordID INT auto_increment primary key,
EmployeeID INT,
foreign key(EmployeeID) references Employee(EmployeeID),
RecordDate Date,
Description varchar(255),
Amount Decimal(10,2),
RecordType varchar(255)
);
