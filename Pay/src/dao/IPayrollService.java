package dao;

import entity.Payroll;
import exception.EmployeeNotFoundException;
import exception.InvalidInputException;
import exception.PayrollGenerationException;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface IPayrollService {
    void generatePayroll(int employeeID, Date startDate, Date endDate ) throws SQLException, EmployeeNotFoundException, PayrollGenerationException;
    Payroll getPayrollById(int payrollId) throws SQLException, InvalidInputException;
    List<Payroll> getPayrollsforEmployee(int employeeID) throws EmployeeNotFoundException, SQLException, InvalidInputException;
    List<Payroll> getPayrollsforPeriod(Date startDate, Date endDate) throws SQLException;
}
