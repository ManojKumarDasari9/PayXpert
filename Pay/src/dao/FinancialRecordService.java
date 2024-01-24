package dao;

import entity.FinancialRecord;
import exception.EmployeeNotFoundException;
import exception.FinancialRecordException;
import exception.InvalidInputException;
import util.DatabaseContext;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class FinancialRecordService implements IFinalRecordService {
    private Connection connection;
    List<FinancialRecord> financialRecordList;

    public FinancialRecordService() {

        this.connection= DatabaseContext.getDBConn();
    }

    @Override
    public void addFinancialRecord(int employeeID, Date recordDate, String description, String recordType) throws SQLException, EmployeeNotFoundException, FinancialRecordException {

        String recordTypes = recordType;
        String query =" select BasicSalary, OvertimePay, Deductions from payroll where EmployeeID = ? and PayPeriodEndDate = ? ";
        PreparedStatement preparedStatement =connection.prepareStatement(query);
        preparedStatement.setInt(1,employeeID);
        preparedStatement.setDate(2,recordDate);
        ResultSet resultSet = preparedStatement.executeQuery();
        double basic = 0.0;
        double overtime =0.0;
        double deductions = 0.0;
        while(resultSet.next()){
            basic = resultSet.getDouble("BasicSalary");
            overtime = resultSet.getDouble("OvertimePay");
            deductions = resultSet.getDouble("Deductions");
            if(recordTypes.equals("income")) {
                insertRecord(employeeID, recordDate, "Basic salary", basic, recordTypes);
                insertRecord(employeeID, recordDate, "Overtime Pay", overtime, recordTypes);
                System.out.println("EmployeeId :"+ employeeID+", record date: "+recordDate+", Basic salary: "+basic+", record type:"+recordTypes);
                System.out.println("EmployeeId :"+ employeeID+", record date: "+recordDate+", Overtime Pay: "+overtime+", record type:"+recordTypes);
            }
            else if(recordTypes.equals("deductions")){
                insertRecord(employeeID, recordDate, "Deductions and tax", deductions, recordTypes);
                System.out.println("EmployeeId :"+ employeeID+", record date: "+recordDate+", Deductions: "+deductions+", record type "+recordTypes);
            }
        }

    }

    public void insertRecord(int employeeID, Date recordDate, String description, double amount, String recordType) throws SQLException {
        String query="insert into financialrecord (EmployeeID,RecordDate,Description,Amount,RecordType) values (?,?,?,?,?)";
        PreparedStatement preparedStatement=connection.prepareStatement(query);
        preparedStatement.setInt(1,employeeID);
        preparedStatement.setDate(2, recordDate);
        preparedStatement.setString(3,description);
        preparedStatement.setDouble(4,amount);
        preparedStatement.setString(5,recordType);
        int rowsUpdate=preparedStatement.executeUpdate();
        if(rowsUpdate>0){
            System.out.println("Row got updated");
        }else {
            System.out.println("Row not updated");
        }
    }

    @Override
    public FinancialRecord getFinancialRecordByID(int recordID) throws SQLException, FinancialRecordException {
        String query="select * from financialrecord where RecordID= ?";
        PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,recordID);
            ResultSet resultSet= preparedStatement.executeQuery();
            while(resultSet.next()) {
                FinancialRecord financialRecord = new FinancialRecord(recordID,
                        resultSet.getInt("EmployeeID"),
                        resultSet.getDate("RecordDate"),
                        resultSet.getString("Description"),
                        resultSet.getDouble("Amount"),
                        resultSet.getString("RecordType"));
                recordById(financialRecord);
                return financialRecord;
            }
            return null;
        }



    public void recordById(FinancialRecord financialRecord){
        System.out.println("EmployeeID: "+financialRecord.getEmployeeID()+", Record date: "+financialRecord.getRecordDate()+", Description: "+financialRecord.getDescription()+", amount: "+financialRecord.getAmount()+", record type: "+financialRecord.getRecordType());
    }

    @Override
    public List<FinancialRecord> getFinancialRecordsForEmployee(int employeeID) throws SQLException, EmployeeNotFoundException, FinancialRecordException, InvalidInputException {
        List<FinancialRecord> finanacialforEmployeeList=new ArrayList<>();
        String query="select * from financialrecord where EmployeeID =?";
        PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setInt(1,employeeID);
            ResultSet resultSet= preparedStatement.executeQuery();
            while(resultSet.next()) {
                FinancialRecord financialRecord = new FinancialRecord(resultSet.getInt("RecordID"),employeeID,
                        resultSet.getDate("RecordDate"),
                        resultSet.getString("Description"),
                        resultSet.getDouble("Amount"),
                        resultSet.getString("RecordType"));
                finanacialforEmployeeList.add(financialRecord);

            }
        financialforEmployee(finanacialforEmployeeList);

        return finanacialforEmployeeList;
    }

    public void financialforEmployee(List<FinancialRecord> financialRecordList){
        for (FinancialRecord financialRecord: financialRecordList) {
            System.out.println("record id: "+financialRecord.getRecordID()+", EmployeeID: "+financialRecord.getEmployeeID()+", Record date: "+financialRecord.getRecordDate()+", Description: "+financialRecord.getDescription()+", amount: "+financialRecord.getAmount()+", record type: "+financialRecord.getRecordType());

        }
    }

    @Override
    public List<FinancialRecord> getFinancialRecordsForDate(Date recordDate) throws SQLException, InvalidInputException {
        List<FinancialRecord> financialRecordsList=new ArrayList<>();
        String query="select * from financialrecord where RecordDate  >= ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
               preparedStatement.setDate(1,recordDate);
               ResultSet resultSet= preparedStatement.executeQuery();
                while(resultSet.next()){
               FinancialRecord financialRecord=new FinancialRecord(resultSet.getInt("RecordID"),
                       resultSet.getInt("EmployeeID"),
                      // resultSet.getDate("RecordDate"),
                       recordDate,
                       resultSet.getString("Description"),
                       resultSet.getDouble("Amount"),
                       resultSet.getString("RecordType"));

               financialRecordsList.add(financialRecord);

        }
        financialforEmployee(financialRecordsList);

        return financialRecordsList;
    }
}
