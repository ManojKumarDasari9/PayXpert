package dao;

import entity.Employee;
import entity.Tax;
import exception.EmployeeNotFoundException;
import util.DatabaseContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaxService implements ITaxService {
    private Connection connection;
    List<Tax> taxList;
    public TaxService(){
        this.connection= DatabaseContext.getDBConn();


    }
    @Override
    public Tax calculateTax(int employeeID, int taxYear) throws SQLException {
        Tax tax=getTaxableIncomeForEmployeeAndYear(employeeID,taxYear);
      // double taxableIncome = getTaxableIncomeForEmployeeAndYear(employeeID,taxYear);
        double taxableIncome= tax.getTaxableIncom();
        //double deductions = tax.getDeductions();
       double taxAmount = 0.0;
       if(taxableIncome>100000 && taxableIncome<250000){
           taxAmount = taxableIncome * 0.03;
       }
       else if(taxableIncome>=250000 && taxableIncome<500000){
           taxAmount = taxableIncome * 0.05;
        }
       else if (taxableIncome>=500000 && taxableIncome<1000000) {
           taxAmount=taxableIncome * 0.1;

       }
       else if(taxableIncome>=1000000 && taxableIncome< 1500000){
           taxAmount = taxableIncome * 0.15;
        }
       else{
           taxAmount = taxableIncome * 0.18;
       }
        System.out.println("Employee ID: "+employeeID);
        System.out.println("Tax year: "+taxYear);
        System.out.println("Taxable income :"+taxableIncome);
        System.out.println("Tax Amount: "+taxAmount);

       String query = "insert into tax (EmployeeID, TaxYear, TaxableIncome, TaxAmount) values (?, ?, ?, ?)";
       PreparedStatement preparedStatement = connection.prepareStatement(query);
       preparedStatement.setInt(1,employeeID);
       preparedStatement.setInt(2,taxYear);
       preparedStatement.setDouble(3,taxableIncome);
       preparedStatement.setDouble(4,taxAmount);
       preparedStatement.executeUpdate();

        return tax;
    }

    public Tax helpTax(int employeeID,int taxYear) throws SQLException {
        Tax tax=getTaxableIncomeForEmployeeAndYear(employeeID,taxYear);
        double taxableIncome= tax.getTaxableIncom();
        double deductions = tax.getDeductions();
        return tax;
    }
    private Tax getTaxableIncomeForEmployeeAndYear(int employeeID, int taxYear) throws SQLException {
        String query = "select EmployeeID, year(PayPeriodEndDate) as SalaryYear, sum(BasicSalary) as TotalBasicSalary, sum(OvertimePay) as TotalOvertimePay from payroll where EmployeeID = ? and year(PayPeriodEndDate) = ? group by EmployeeID, year(PayPeriodEndDate)";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, employeeID);
            preparedStatement.setInt(2, taxYear);
            ResultSet resultSet = preparedStatement.executeQuery();
            double totalTax = 0.0;

            while(resultSet.next()) {
                int payrollYear = resultSet.getInt("SalaryYear");

                if (payrollYear == taxYear) {
                    double basicSalary = resultSet.getDouble("TotalBasicSalary");
                    double overtime = resultSet.getDouble("TotalOvertimePay");
                    double deductions = (basicSalary * 0.12);
                    //start
                    double taxableIncome = (basicSalary + overtime) - (basicSalary * 0.12);

                    Tax tax= new Tax(taxableIncome,deductions);
                    return tax;
                }
            }

        return null;
    }


    private int extractYear(Date date) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(java.util.Calendar.YEAR);
    }


    @Override
    public Tax getTaxById(int taxID) throws SQLException {
        String query="SELECT t.TaxID, t.EmployeeID, t.TaxYear, t.TaxableIncome, t.TaxAmount, e.FirstName, e.LastName FROM tax t INNER JOIN employee e ON t.EmployeeID = e.EmployeeID WHERE TaxID = ?";

        PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setInt(1,taxID);
            ResultSet resultSet= preparedStatement.executeQuery();
            while(resultSet.next()) {
                Tax tax = new Tax(taxID,//resultSet.getInt("TaxID"),
                        resultSet.getInt("EmployeeID"),
                        new Employee(
                                resultSet.getString("FirstName"),
                                resultSet.getString("LastName")),
                        resultSet.getInt("TaxYear"),
                        resultSet.getDouble("TaxableIncome"),
                        resultSet.getDouble("TaxAmount"));
                taxDetails(tax);
                return tax;
            }

        return null;
    }

    @Override
    public List<Tax> getTaxesforEmployee(int employeeID)throws SQLException, EmployeeNotFoundException {
        List<Tax> taxEmployee=new ArrayList<>();
        String query="SELECT t.TaxID, t.TaxYear, t.TaxableIncome, t.TaxAmount, e.FirstName, e.LastName FROM tax t INNER JOIN employee e on e.EmployeeID = t.EmployeeID where e.EmployeeID = ?";
        PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,employeeID);
            ResultSet resultSet= preparedStatement.executeQuery();
            while(resultSet.next()) {
                Tax tax = new Tax(resultSet.getInt("TaxID"),
                        employeeID,
                        new Employee(
                                resultSet.getString("FirstName"),
                                resultSet.getString("LastName")),
                        resultSet.getInt("TaxYear"),
                        resultSet.getDouble("TaxableIncome"),
                        resultSet.getDouble("TaxAmount"));
                taxEmployee.add(tax);

        }
            taxListofEmployee(taxEmployee);
        return taxEmployee;
    }

    @Override
    public List<Tax> getTaxesforYear(int taxYear) throws SQLException {
        List<Tax> taxforYear=new ArrayList<>();
        String query="SELECT t.TaxID, t.EmployeeID, t.TaxYear, t.TaxableIncome, t.TaxAmount, e.FirstName, e.LastName FROM tax t INNER JOIN employee e on e.EmployeeID = t.EmployeeID  where t.TaxYear =?";
        PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,taxYear);
            ResultSet resultSet= preparedStatement.executeQuery();
            while(resultSet.next()) {
                Tax tax = new Tax(resultSet.getInt("TaxID"),
                    resultSet.getInt("EmployeeID"),
                        new Employee(resultSet.getString("FirstName"),
                                resultSet.getString("LastName")),
                        taxYear,
                        resultSet.getDouble("TaxableIncome"),
                        resultSet.getDouble("TaxAmount"));
                taxforYear.add(tax);

        }
            taxListofEmployee(taxforYear);

        return taxforYear;
    }

    public void taxDetails(Tax tax) throws SQLException {
        System.out.println("------------------------------------");
        System.out.println("Tax id: "+tax.getTaxID());
        System.out.println("EmployeeId: "+tax.getEmployeeID());
        System.out.println("First Name: "+tax.getFirstName());
        System.out.println("Last Name: "+tax.getLastName());
        System.out.println("tax year: "+tax.getTaxYear());
        System.out.println("Taxable Income: "+tax.getTaxableIncom());
        System.out.println("Tax Amount: "+tax.getTaxAmount());
    }

    public void taxListofEmployee(List<Tax> taxList){
        for (Tax tax: taxList) {
            System.out.println("------------------------------------");
            System.out.println("Tax id: "+tax.getTaxID());
            System.out.println("EmployeeId: "+tax.getEmployeeID());
            System.out.println("First Name: "+tax.getFirstName());
            System.out.println("Last Name: "+tax.getLastName());
            System.out.println("tax year: "+tax.getTaxYear());
            System.out.println("Taxable Income: "+tax.getTaxableIncom());
            System.out.println("Tax Amount: "+tax.getTaxAmount());
        }
    }
}
