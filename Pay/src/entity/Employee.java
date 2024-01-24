package entity;

import java.sql.Date;
import java.time.LocalDate;

public class Employee {
    private int employeeId;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String gender;
    private String email;
    private String phoneNumber;
    private String address;
    private String position;
    private Date joiningDate;
    private Date terminationDate;

    public Employee(String firstName, String lastName, Date dob1, String gender, String email, String phone, String address, String position, Date joiningDate, Date terminationDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dob1;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phone;
        this.address = address;
        this.position = position;
        this.joiningDate = joiningDate;
        this.terminationDate = terminationDate;
    }

    public Employee(int employeeID, String firstName, String lastName) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Employee(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public Employee(int employeeId, String firstName, String lastName, Date dateOfBirth, String gender, String email, String phoneNumber, String address, String position, Date joiningDate, Date terminationDate) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.position = position;
        this.joiningDate = joiningDate;
        this.terminationDate = terminationDate;
    }

    public Date getJoiningDate() {
        return joiningDate;
    }

    public Date getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(Date terminationDate) {
        this.terminationDate = Date.valueOf(terminationDate.toLocalDate());
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = Date.valueOf(joiningDate.toLocalDate());
    }

    public String getAddress() {
        return address;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = Date.valueOf(dateOfBirth.toLocalDate());
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int calculateAge(){
        LocalDate curDate= LocalDate.now();
        int age = curDate.getYear()-dateOfBirth.getYear();
        return age;
    }
}
