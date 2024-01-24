package dao;

import dao.IPayrollService;
import entity.Employee;
import entity.Payroll;
import entity.Tax;
import exception.EmployeeNotFoundException;
import exception.InvalidInputException;
import exception.PayrollGenerationException;
import util.DatabaseContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;


public class PayrollService implements IPayrollService {
    List<Payroll> payrollList;
    private Connection connection;

    public PayrollService(){
        this.connection= DatabaseContext.getDBConn();
//        this.payrollList=new ArrayList<>();
    }
    @Override
    public void generatePayroll(int employeeID, Date startDate, Date endDate) throws SQLException, EmployeeNotFoundException, PayrollGenerationException {
      //


        taxHelp(employeeID,startDate,endDate);
        Double deduction = 0.0;

        String query="select  e.FirstName, e.LastName, p.PayrollID, p.BasicSalary, p.OvertimePay, p.Deductions, p.NetSalary  from Employee e inner join Payroll p on e.EmployeeID = p.EmployeeID  where e.EmployeeID = ? and p.PayPeriodStartDate >= ? and p.PayPeriodEndDate <= ?";

        PreparedStatement preparedStatement=connection.prepareStatement(query);
        preparedStatement.setInt(1,employeeID);
        preparedStatement.setDate(2,startDate);
        preparedStatement.setDate(3,endDate);

        ResultSet resultSet= preparedStatement.executeQuery();
        while (resultSet.next()) {
            int payrollId = resultSet.getInt("PayrollID");
            String firstName = resultSet.getString("FirstName");
            String lastName = resultSet.getString("LastName");
            Double basic = resultSet.getDouble("BasicSalary");
            Double overtime = resultSet.getDouble("OvertimePay");
            deduction = resultSet.getDouble("Deductions");
            //
            Double netSalary = basic + overtime - deduction;
            String query1 ="update payroll set  NetSalary=?  where EmployeeId= ? and PayPeriodStartDate= ? and PayPeriodEndDate=?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement1.setDouble(1,netSalary);
            preparedStatement1.setInt(2,employeeID);
            preparedStatement1.setDate(3,startDate);
            preparedStatement1.setDate(4,endDate);
            preparedStatement1.executeUpdate();

            System.out.println("PayrollId: " + payrollId + ", FirstName: " + firstName + ", LastName: " + lastName + ", BasicSalary: " + basic + ", OvertimePay: " + overtime + ",NetSalary: " + netSalary);
        }

    }



    @Override
    public Payroll getPayrollById(int payrollId) throws SQLException,InvalidInputException {
        String query="select * from payroll where PayrollID=?";
        PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,payrollId);

            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                Payroll payroll= new Payroll(resultSet.getInt("EmployeeID"),
                        resultSet.getDate("PayPeriodStartDate"),
                        resultSet.getDate("PayPeriodEndDate"),
                        resultSet.getDouble("BasicSalary"),
                        resultSet.getDouble("OvertimePay"),
                        resultSet.getDouble("Deductions"),
                        resultSet.getDouble("NetSalary"));
                        payrollDetails(payroll);

                return payroll;
            }


        return null;
    }


    public void addPayroll(Payroll payroll) throws SQLException, EmployeeNotFoundException {
        String query= "insert into payroll (EmployeeID,PayPeriodStartDate,PayPeriodEndDate,BasicSalary,OvertimePay,Deductions,NetSalary) values (?,?,?,?,?,?,?)";

        PreparedStatement preparedStatement=connection.prepareStatement(query);

        preparedStatement.setInt(1,payroll.getEmployeeID());
        preparedStatement.setDate(2,payroll.getPayPeriodStartDate());
        preparedStatement.setDate(3,payroll.getPayPeriodEndDate());
        preparedStatement.setDouble(4,payroll.getBasicSalary());
        preparedStatement.setDouble(5,payroll.getOvertimePay());
        preparedStatement.setDouble(6,payroll.getDeductions());
        preparedStatement.setDouble(7,payroll.getNetSalary());

        int t =preparedStatement.executeUpdate();
        if(t>0){
            System.out.println("inserted successfully");
        }
        else{
            System.out.println("not added");
        }
    }

    public void taxHelp(int employeeID, Date start, Date end) throws SQLException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.setTime(end);
        int year =0;
        int starty = calendar.get(Calendar.YEAR);
        int endy = calendar.get(Calendar.YEAR);
        if(starty == endy){
            year = starty;
        }

       TaxService taxService = new TaxService();
       Tax tax= taxService.helpTax(employeeID,year);
       double taxAmount= tax.getTaxAmount();
        double pf =tax.getDeductions();
        double deductions =0.0;

        String query ="select count(EmployeeID) as NumberofRecords from payroll where EmployeeID = ? and YEAR(PayPeriodEndDate) = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1,employeeID);
        preparedStatement.setInt(2,year);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            int months = resultSet.getInt("NumberofRecords");
            //System.out.println("Months:"+months);
            deductions = (taxAmount / months) + (pf / months);

        }
        deduction(employeeID,deductions);

    }

    public void deduction(int employeeID,double deductions) throws SQLException {
        String query1 = "update payroll set Deductions= ? where EmployeeID = ?";
        PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
        preparedStatement1.setDouble(1, deductions);
        preparedStatement1.setInt(2, employeeID);
        preparedStatement1.executeUpdate();
        System.out.println("updated");
    }

    @Override
    public List<Payroll> getPayrollsforEmployee(int employeeID) throws SQLException, EmployeeNotFoundException, InvalidInputException {
        List<Payroll> payrollList1= new ArrayList<>();
        String query = "SELECT e.FirstName, e.LastName, p.PayrollID, p.PayPeriodStartDate, p.PayPeriodEndDate, p.BasicSalary, p.OvertimePay, p.Deductions, p.NetSalary from employee e inner join payroll p on e.EmployeeID = p.EmployeeID where e.EmployeeID = ?";
        PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,employeeID);
            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                Payroll payroll=new Payroll(resultSet.getInt("PayrollID"),
                        employeeID,

                        new Employee(

                                resultSet.getString("FirstName"),
                                resultSet.getString("LastName")
                        ),
                        resultSet.getDate("PayPeriodStartDate"),
                        resultSet.getDate("PayPeriodEndDate"),
                        resultSet.getDouble("BasicSalary"),
                        resultSet.getDouble("OvertimePay"),
                        resultSet.getDouble("Deductions"),
                        resultSet.getDouble("NetSalary"));
                payrollList1.add(payroll);


            }
        payrollsEmployee(payrollList1);

        return payrollList1;
    }

    @Override
    public List<Payroll> getPayrollsforPeriod(Date startDate, Date endDate) throws SQLException{
        List<Payroll> payrollList1= new ArrayList<>();
        String query="select e.EmployeeID as EmployeeID, e.FirstName, e.LastName, p.PayrollID, p.BasicSalary, p.OvertimePay, p.Deductions, p.NetSalary from employee e inner join payroll p on e.EmployeeID = p.EmployeeID where p.PayPeriodStartDate >= ? and p.PayPeriodEndDate <= ?";
                //"select * from payroll where PayPeriodStartDate >= ? and PayPeriodEndDate <= ? ";

        //"select e.FirstName, e.LastName, p.PayrollID,p.BasicSalary, p.OvertimePay, p.Deductions, p.NetSalary"+ "from employee e inner join payroll p on e.EmployeeID = p.EmployeeID"+ " where p.PayPeriodStartDate >= ? and p.PayPeriodEndDate <= ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1,startDate);
            preparedStatement.setDate(2,endDate);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Payroll payroll = new Payroll(resultSet.getInt("PayrollID"),
                            resultSet.getInt("EmployeeID"),
                        new Employee(
                                resultSet.getInt("EmployeeID"),
                                resultSet.getString("FirstName"),
                                resultSet.getString("LastName")
                        ),
//                        //resultSet.getInt("EmployeeID"),
//                        //resultSet.getDate("PayPeriodStartDate"),
//                        //resultSet.getDate("PayPeriodEndDate"),
                        startDate,endDate,
                        resultSet.getDouble("BasicSalary"),
                        resultSet.getDouble("OvertimePay"),
                        resultSet.getDouble("Deductions"),
                        resultSet.getDouble("NetSalary"));
                payrollList1.add(payroll);

            }
        payrollPeriod(payrollList1);

        return payrollList1;
    }


    public void payrollDetails(Payroll payroll){

        System.out.println("EmployeeID: "+payroll.getEmployeeID()+", pay period start date: "+payroll.getPayPeriodStartDate()+", pay period end date: "+payroll.getPayPeriodEndDate()+", Basic salary: "+payroll.getBasicSalary()+", Overtime Pay: "+payroll.getOvertimePay()+", Deductions: "+payroll.getDeductions()+", Net Salary: "+payroll.getNetSalary());
    }

    public void payrollsEmployee(List<Payroll> payrollEmployeeList){
        for (Payroll payroll: payrollEmployeeList) {
            System.out.println("Payroll ID: "+payroll.getPayrollID());
            System.out.println("EmployeeID: "+payroll.getEmployeeID());
            System.out.println("FirstName: "+payroll.getFirstName());
            System.out.println("LastName: "+payroll.getLastName());
            System.out.println("Pay period start date: "+payroll.getPayPeriodStartDate());
            System.out.println("Pay period end date: "+payroll.getPayPeriodEndDate());
            System.out.println("Basic salary: "+payroll.getBasicSalary());
            System.out.println("Overtime pay: "+payroll.getOvertimePay());
            System.out.println("Deductions: "+payroll.getDeductions());
            System.out.println("Net salary: "+payroll.getNetSalary());
            System.out.println("-------------------------");
        }
    }
    public void payrollPeriod(List<Payroll> periodList){
        for (Payroll payroll: periodList) {
            System.out.println("Payroll ID: "+payroll.getPayrollID());
            System.out.println("EmployeeID: "+payroll.getEmployeeID());
            System.out.println("FirstName: "+payroll.getFirstName());
            System.out.println("LastName: "+payroll.getLastName());
            System.out.println("Basic salary: "+payroll.getBasicSalary());
            System.out.println("Overtime pay: "+payroll.getOvertimePay());
            System.out.println("Deductions: "+payroll.getDeductions());
            System.out.println("Net salary: "+payroll.getNetSalary());
            System.out.println("---------------------------");

        }
    }

    public void generatePay(Payroll payroll) throws SQLException {
        Double netSalary = payroll.getBasicSalary() + payroll.getDeductions() -  payroll.getDeductions();
        String query="insert into payroll (Deductions,NetSalary) values (?,?)";
        PreparedStatement preparedStatement=connection.prepareStatement(query);

//        preparedStatement.setInt(1,payroll.getEmployeeID());
//        preparedStatement.setDate(2,payroll.getPayPeriodStartDate());
//        preparedStatement.setDate(3,payroll.getPayPeriodEndDate());
//        preparedStatement.setDouble(4,payroll.getBasicSalary());
//        preparedStatement.setDouble(5,payroll.getOvertimePay());
        preparedStatement.setDouble(1,payroll.getDeductions());
        preparedStatement.setDouble(2,netSalary);
        int t =preparedStatement.executeUpdate();
        if(t>0){
            System.out.println("inserted successfully");
        }
        else{
            System.out.println("not added");
        }
    }
}
