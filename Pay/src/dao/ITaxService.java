package dao;

import entity.Tax;
import exception.EmployeeNotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface ITaxService {
    Tax calculateTax(int employeeID, int taxYear) throws SQLException;
    Tax getTaxById(int taxID) throws SQLException;
    List<Tax> getTaxesforEmployee(int employeeID) throws SQLException, EmployeeNotFoundException;
    List<Tax> getTaxesforYear(int taxYear) throws SQLException;


}
