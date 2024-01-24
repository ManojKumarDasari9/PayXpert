package main;

import dao.EmployeeService;
import dao.FinancialRecordService;
import dao.PayrollService;
import dao.TaxService;
import entity.Employee;
import entity.FinancialRecord;
import entity.Payroll;
import entity.Tax;
import exception.EmployeeNotFoundException;
import exception.FinancialRecordException;
import exception.InvalidInputException;
import exception.PayrollGenerationException;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException, EmployeeNotFoundException {
        EmployeeService employeeService= new EmployeeService();
        PayrollService payrollService=new PayrollService();
        TaxService taxService= new TaxService();
        FinancialRecordService financialRecordService = new FinancialRecordService();
        Scanner scan = new Scanner(System.in);
        Employee employee= null;
        Payroll payroll=null;
        boolean enter = true;
        while(enter){
        System.out.println("1.Employee Services, 2. Payroll Services, 3. Tax Services, 4. Financial Records, 5. exit");
        int classes = scan.nextInt();
        switch(classes) {
            case 1:
                System.out.println("1. Get employee by id, 2. Add employee, 3. update employee, 4. remove employee by id, 5. get all employees");
                int functions = scan.nextInt();
                switch (functions) {
                    case 1:
                        System.out.println("Enter employee id to get details");
                        int employeeId = scan.nextInt();
                        try {
                            employeeService.getEmployeeById(employeeId);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        } catch (EmployeeNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (InvalidInputException e) {
                            throw new RuntimeException(e);
                        }
                        break;

                    case 2:
                        System.out.println("Enter employee details to add employee");
                        System.out.println("Enter First Name");
                        String firstName = scan.next();
                        System.out.println("Enter LastName");
                        String lastName = scan.next();
                        System.out.println("Enter DOB in order yyyy-mm-dd");
                        String dob = scan.next();
                        Date dob1 = Date.valueOf(dob);
                        System.out.println("Parsed DOB: " + dob);
                        System.out.println("Enter Gender");
                        String gender = scan.next();
                        System.out.println("Enter email");
                        String email = scan.next();
                        System.out.println("Enter Phone");
                        String phone = scan.next();
                        System.out.println("phone number: " + phone);
                        System.out.println("Enter address");
                        String address = scan.next();
                        System.out.println("Enter Position");
                        String position = scan.next();
                        System.out.println("Enter Joining date");
                        Date jd = parseDate(scan.next());
                        System.out.println("jd : " + jd);
                        System.out.println("Enter Termination date");
                        Date td = parseDate(scan.next());
                        System.out.println("td: " + td);

                        employee = new Employee(firstName, lastName, dob1, gender, email, phone, address, position, jd, td);
                        try {
                            employeeService.addEmployee(employee);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        } catch (EmployeeNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (InvalidInputException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println("Employee added");
                        //employeeService.getDetails(employee);
                        break;

                    case 3:
                        System.out.println("Enter employee id to update");
                        int employeeID = scan.nextInt();
                        System.out.println("Enter First Name");
                        firstName = scan.next();
                        System.out.println("Enter LastName");
                        lastName = scan.next();
                        System.out.println("Enter DOB in order yyyy-mm-dd");
                        dob = scan.next();
                        dob1 = Date.valueOf(dob);
                        System.out.println("Enter Gender");
                        gender = scan.next();
                        System.out.println("Enter email");
                        email = scan.next();
                        System.out.println("Enter Phone");
                        phone = scan.next();
                        System.out.println("Enter address");
                        address = scan.next();
                        System.out.println("Enter Position");
                        position = scan.next();
                        System.out.println("Enter Joining date");
                        jd = parseDate(scan.next());
                        System.out.println("Enter Termination date");
                        td = parseDate(scan.next());

                        employee = new Employee(employeeID, firstName, lastName, dob1, gender, email, phone, address, position, jd, td);
                        try {
                            employeeService.updateEmployee(employee);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        } catch (EmployeeNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (InvalidInputException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println("Employee added");
                        employeeService.getDetails(employee);
                        break;

                    case 4:
                        System.out.println("Enter employee id");
                        employeeID = scan.nextInt();
                        try {
                            employeeService.removeEmployee(employeeID);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        } catch (EmployeeNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (InvalidInputException e) {
                            throw new RuntimeException(e);
                        }

                        break;

                    case 5:
                        try {
                            employeeService.getAllEmployees();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        break;

                    default:
                        System.out.println("Choose correct number");
                }
                break;

            case 2:
                List<Payroll> payrollList;
                System.out.println("Enter 1. Add payroll, 2. get payroll by id, 3. generate payroll by employee id, 4. get payrolls of employee, 5. get payrolls for period");
                int payrollFunc = scan.nextInt();
                switch (payrollFunc) {

                    case 1:
                        System.out.println("Enter employee id");
                        int employeeId = scan.nextInt();
                        System.out.println("Enter payperiod start date");
                        Date payStart = parseDate(scan.next());
                        System.out.println("Enter pay period end date");
                        Date payEnd = parseDate(scan.next());
                        System.out.println("Enter Basic Salary");
                        Double basicSalary = scan.nextDouble();
                        System.out.println("Enter Overtime pay");
                        Double overtime = scan.nextDouble();
                        System.out.println("Enter deductions");
                        Double deduction = scan.nextDouble();
                        System.out.println("Enter net Salary");
                        Double netSalary = scan.nextDouble();
                        payroll = new Payroll(employeeId, payStart, payEnd, basicSalary, overtime, deduction, netSalary);
                        try {
                            payrollService.addPayroll(payroll);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        } catch (EmployeeNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        break;


                    case 2:
                        System.out.println("payroll id");
                        int payrollID = scan.nextInt();
                        try {
                            payrollService.getPayrollById(payrollID);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        } catch (InvalidInputException e) {
                            throw new RuntimeException(e);
                        }
                        //System.out.println("EmployeeID: "+payroll.getEmployeeID()+", pay period start date: "+payroll.getPayPeriodStartDate()+", pay period end date: "+payroll.getPayPeriodEndDate()+", Basic salary: "+payroll.getBasicSalary()+"Overtime Pay: "+payroll.getOvertimePay()+", Deductions: "+payroll.getDeductions()+", Net Salary: "+payroll.getNetSalary());

                        break;


                    case 3:
                        System.out.println("Enter employeeID");
                        employeeId = scan.nextInt();
                        System.out.println("Enter startDate");
                        payStart = parseDate(scan.next());
                        System.out.println("Enter endDate");
                        payEnd = parseDate(scan.next());
                        try {
                            payrollService.generatePayroll(employeeId, payStart, payEnd);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        } catch (EmployeeNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (PayrollGenerationException e) {
                            throw new RuntimeException(e);
                        }
                        break;


                    case 4:
                        System.out.println("Enter employee id");
                        int employeeID = scan.nextInt();
                        try {
                            payrollList = payrollService.getPayrollsforEmployee(employeeID);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        } catch (EmployeeNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (InvalidInputException e) {
                            throw new RuntimeException(e);
                        }

                        break;


                    case 5:
                        System.out.println("Enter start date");
                        Date start = parseDate(scan.next());
                        System.out.println("Enter end date");
                        Date end = parseDate(scan.next());
                        try {
                            payrollService.getPayrollsforPeriod(start, end);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        break;



                }
                break;

            case 3:
                System.out.println("Enter 1. calculate and update tax, 2. Get tax by id, 3. get employee taxes, 4. get tax by year");
                int taxFunc = scan.nextInt();
                switch (taxFunc) {
                    case 1:
                        System.out.println("Enter employee id");
                        int employeeID = scan.nextInt();
                        System.out.println("Enter tax year");
                        int taxYear = scan.nextInt();
                        try {

                            taxService.calculateTax(employeeID, taxYear);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        break;


                    case 2:
                        System.out.println("Enter tax id");
                        int taxId = scan.nextInt();
                        try {
                            taxService.getTaxById(taxId);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        break;


                    case 3:
                        System.out.println("Enter employee id");
                        int employeeId = scan.nextInt();
                        try {
                            taxService.getTaxesforEmployee(employeeId);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        } catch (EmployeeNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        break;


                    case 4:
                        System.out.println("Enter year");
                        int year = scan.nextInt();
                        try {
                            taxService.getTaxesforYear(year);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        break;


                }
                break;


            case 4:
                System.out.println("1. add financial record, 2. get financial records by record id, 3. get financial record by employee id, 4. get financial record by date");
                int financialFunc=scan.nextInt();
                switch(financialFunc){
                    case 1:
                        System.out.println("Enter employeeId");
                        int employeeId=scan.nextInt();
                        System.out.println("Enter Record date");
                        Date recordDate=parseDate(scan.next());
                        System.out.println("Description");
                        String description = scan.next();
//                        System.out.println("amount");
//                        Double amount = scan.nextDouble();
                        System.out.println("record type income or deductions");
                        String recordType = scan.next();
                        try {
                            financialRecordService.addFinancialRecord(employeeId,recordDate,description,recordType);
                        } catch (FinancialRecordException e) {
                            throw new RuntimeException(e);
                        }

                        //FinancialRecord financialRecord = new FinancialRecord(employeeId,recordDate,description,amount,recordType);
                        break;

                    case 2:
                        System.out.println("Enter record id");
                        int recordId = scan.nextInt();

                        try {
                            financialRecordService.getFinancialRecordByID(recordId);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        } catch (FinancialRecordException e) {
                            throw new RuntimeException(e);
                        }
                        break;


                    case 3:
                        System.out.println("Enter employee id");
                        employeeId = scan.nextInt();

                        try {
                            financialRecordService.getFinancialRecordsForEmployee(employeeId);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        } catch (EmployeeNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (FinancialRecordException e) {
                            throw new RuntimeException(e);
                        } catch (InvalidInputException e) {
                            throw new RuntimeException(e);
                        }

                        break;


                    case 4:
                        System.out.println("enter date");
                        recordDate = parseDate(scan.next());
                        try {
                            financialRecordService.getFinancialRecordsForDate(recordDate);
                        }catch (SQLException e) {
                            throw new RuntimeException(e);
                        } catch (InvalidInputException e) {
                            throw new RuntimeException(e);
                        }
                        break;

                }
                break;


            case 5:
                enter = false;
                break;
        }


        }
    }
    private static Date parseDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return new java.sql.Date(dateFormat.parse(dateString).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
