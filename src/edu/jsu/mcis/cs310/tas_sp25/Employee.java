/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Nehemias Lucas
 */
public class Employee { //Class 
    // Initiate variables
    private final int id;
    private final String firstname,middlename,lastname;
    private LocalDateTime active = null;
    private final Badge badge;
    private final Department department;
    private final Shift shift;
    private final EmployeeType employeetype;
    

    // Constructor
    public Employee(int id, String firstname, String middlename, String lastname, Badge badge, Department department, Shift shift, EmployeeType employeetype, LocalDateTime active) {
        this.id = id;
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.badge = badge;
        this.department = department;
        this.shift = shift;
        this.employeetype = employeetype;
        // Added active timestamp to Constructor
        this.active = active;
    }
    // Getters

    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public LocalDateTime getActive() {
        return active;
    }

    public Badge getBadge() {
        return badge;
    }

    public Department getDepartment() {
        return department;
    }

    public Shift getShift() {
        return shift;
    }

    public EmployeeType getEmployeetype() {
        return employeetype;
    }
    
        
    // ToString method 
    //rewrote this to pass tests in EmployeeFindTest.Java
    @Override
    public String toString() {
        // Format the active date as MM/dd/yyyy, if available
        String activeStr = (active != null) ? active.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) : "null";

        // StringBuilder for constructing the string
        StringBuilder sb = new StringBuilder();
        sb.append("ID #").append(id);
        sb.append(": ").append(lastname);
        sb.append(", ").append(firstname);
        sb.append(" ").append(middlename);
        sb.append(" (#").append(badge.getId());
        sb.append("), ").append("Type: ").append(employeetype);
        sb.append(", ").append("Department: ").append(department.getDescription());
        sb.append(", ").append("Active: ").append(activeStr);

        return sb.toString();
    }
    
    
    
}
