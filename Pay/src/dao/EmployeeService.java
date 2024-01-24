package dao;

import entity.Employee;
import exception.EmployeeNotFoundException;
import exception.InvalidInputException;
import util.DatabaseContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService implements IEmployeeService {

    private Connection connection;

    public EmployeeService() {
        this.connection = DatabaseContext.getDBConn();
    }

    //
    private List<Employee> employeeList;

    @Override
    public void getEmployeeById(int employeeID) throws SQLException, EmployeeNotFoundException, InvalidInputException {
        String query = "select * from employee where EmployeeID= ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, employeeID);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
        Employee employee = new Employee(employeeID,resultSet.getString("FirstName"),
                resultSet.getString("LastName"),
                resultSet.getDate("DateOfBirth"),
                resultSet.getString("Gender"),
                resultSet.getString("Email"),
                resultSet.getString("PhoneNumber"),
                resultSet.getString("address"),
                resultSet.getString("Position"),
                resultSet.getDate("JoiningDate"),
                resultSet.getDate("TerminationDate"));

        getDetails(employee);
        }


    }



    public void getDetails(Employee employee) {
        System.out.println("------------------------------------");
        System.out.println("EmployeeID: " + employee.getEmployeeId());
        System.out.println("FirstName: " +employee.getFirstName());
        System.out.println("LastName: " +employee.getLastName());
        System.out.println("DateOfBirth: " +employee.getDateOfBirth());
        System.out.println("Gender: "+employee.getGender());
        System.out.println("Email: "+employee.getEmail());
        System.out.println("PhoneNumber: "+employee.getPhoneNumber());
        System.out.println("address: "+employee.getAddress());
        System.out.println("Position: "+employee.getPosition());
        System.out.println("JoiningDate: "+employee.getJoiningDate());
        System.out.println("TerminationDate: "+employee.getTerminationDate());

    }
    @Override
    public void getAllEmployees() throws SQLException {

        List<Employee> employeeList1 = new ArrayList<>();
        String query = "select * from employee";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Employee employee = new Employee(resultSet.getInt("EmployeeID"),
                    resultSet.getString("FirstName"),
                    resultSet.getString("LastName"),
                    resultSet.getDate("DateOfBirth"),
                    resultSet.getString("Gender"),
                    resultSet.getString("Email"),
                    resultSet.getString("PhoneNumber"),
                    resultSet.getString("address"),
                    resultSet.getString("Position"),
                    resultSet.getDate("JoiningDate"),
                    resultSet.getDate("TerminationDate"));

            getDetails(employee);
        }


    }

    @Override
    public void addEmployee(Employee employeeData) throws SQLException, EmployeeNotFoundException, InvalidInputException {
        String query = "INSERT INTO employee (FirstName, LastName, DateOfBirth, Gender, Email, PhoneNumber, Address, Position, JoiningDate, TerminationDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, employeeData.getFirstName());
            preparedStatement.setString(2, employeeData.getLastName());
            preparedStatement.setDate(3, employeeData.getDateOfBirth());
            preparedStatement.setString(4, employeeData.getGender());
            preparedStatement.setString(5, employeeData.getEmail());
            preparedStatement.setString(6, employeeData.getPhoneNumber());
            preparedStatement.setString(7, employeeData.getAddress());
            preparedStatement.setString(8, employeeData.getPosition());
            preparedStatement.setDate(9, employeeData.getJoiningDate());
            preparedStatement.setDate(10, employeeData.getTerminationDate());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee added successfully.");
            } else {
                System.out.println("Failed to add employee.");
            }
        }



    @Override
    public void updateEmployee(Employee employeeData) throws SQLException, EmployeeNotFoundException, InvalidInputException {


        String query = "Update employee set FirstName=?, LastName=?, DateOfBirth=?, Gender=?, Email=?, PhoneNumber=?, address=?, Position=?, JoiningDate=?, TerminationDate=? where EmployeeID=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, employeeData.getFirstName());
        preparedStatement.setString(2, employeeData.getLastName());
        preparedStatement.setDate(3, employeeData.getDateOfBirth());
        preparedStatement.setString(4, employeeData.getGender());
        preparedStatement.setString(5, employeeData.getEmail());
        preparedStatement.setString(6, employeeData.getPhoneNumber());
        preparedStatement.setString(7, employeeData.getAddress());
        preparedStatement.setString(8, employeeData.getPosition());
        preparedStatement.setDate(9, employeeData.getJoiningDate());
        preparedStatement.setDate(10, employeeData.getTerminationDate());
        preparedStatement.setInt(11,employeeData.getEmployeeId());
        preparedStatement.executeUpdate();

    }

    @Override
    public void removeEmployee(int employeeID) throws SQLException, EmployeeNotFoundException, InvalidInputException {

        String query = "delete from employee where EmployeeID=? ";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, employeeID);
        int remove=preparedStatement.executeUpdate();
        if(remove>0){
            System.out.println("removed successfully");
        }
        else{
            System.out.println("not removed successfully");
        }

    }




}
