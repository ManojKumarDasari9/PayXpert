package entity;

public class Tax {
    private int taxID;
    private int employeeID;
    private int taxYear;
    private double taxableIncom;
    private double taxAmount;
    private String firstName;
    private String lastName;
    private double deductions;


    public Tax(int taxID, int employeeID, int taxYear, double taxableIncom, double taxAmount) {
        this.taxID = taxID;
        this.employeeID = employeeID;
        this.taxYear = taxYear;
        this.taxableIncom = taxableIncom;
        this.taxAmount = taxAmount;
    }

    public Tax(int taxID, int employeeID, Employee employee, int taxYear, double taxableIncome, double taxAmount) {
        this.taxID = taxID;
        this.employeeID = employeeID;
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.taxYear = taxYear;
        this.taxableIncom = taxableIncome;
        this.taxAmount = taxAmount;
    }

    public Tax(double taxableIncome, double deductions) {
        setTax(taxableIncome,deductions);
        this.taxableIncom= taxableIncome;
        this.deductions = deductions;

    }

    public void setTax(double taxableIncome, double deductions){
        this.taxableIncom= taxableIncome;
        this.deductions = deductions;
    }

    public int getTaxID() {
        return taxID;
    }

    public void setTaxID(int taxID) {
        this.taxID = taxID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getTaxYear() {
        return taxYear;
    }

    public void setTaxYear(int taxYear) {
        this.taxYear = taxYear;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getTaxableIncom() {
        return taxableIncom;
    }

    public void setTaxableIncom(double taxableIncom) {
        this.taxableIncom = taxableIncom;
    }

    public double getDeductions() {
        return deductions;
    }

    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

}
