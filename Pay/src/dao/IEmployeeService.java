package dao;

import entity.Employee;
import exception.EmployeeNotFoundException;
import exception.InvalidInputException;

import java.sql.SQLException;

public interface IEmployeeService {
   void getEmployeeById(int employeeID) throws SQLException, EmployeeNotFoundException, InvalidInputException;
   void getAllEmployees() throws SQLException;
   void addEmployee(Employee employee) throws SQLException, EmployeeNotFoundException, InvalidInputException;
   void updateEmployee(Employee employee) throws SQLException, EmployeeNotFoundException, InvalidInputException;
   void removeEmployee(int employeeID) throws SQLException, EmployeeNotFoundException, InvalidInputException;


}
