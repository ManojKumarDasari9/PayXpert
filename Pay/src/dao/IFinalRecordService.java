package dao;

import entity.FinancialRecord;
import exception.EmployeeNotFoundException;
import exception.FinancialRecordException;
import exception.InvalidInputException;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface IFinalRecordService {
    //void addFinancialRecord(int employeeID,String description,double amount,String recordType) throws SQLException;

    //void addFinancialRecord(int employeeID, Date recordDate, String description, double amount, String recordType) throws SQLException, EmployeeNotFoundException;

    void addFinancialRecord(int employeeID, Date recordDate, String description, String recordType) throws SQLException, EmployeeNotFoundException, FinancialRecordException;

    FinancialRecord getFinancialRecordByID(int recordID) throws SQLException, FinancialRecordException;
    List<FinancialRecord> getFinancialRecordsForEmployee(int employeeID) throws SQLException, EmployeeNotFoundException, FinancialRecordException, InvalidInputException;
    List<FinancialRecord> getFinancialRecordsForDate(Date recordDate) throws SQLException, InvalidInputException;
}
