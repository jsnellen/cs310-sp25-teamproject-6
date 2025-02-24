/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25;

import java.time.LocalDateTime;

/**
 *
 * @Creating Employee class, Nehemias Lucas 2-24-2025
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
    public Employee(int id, String firstname, String middlename, String lastname, Badge badge, Department department, Shift shift, EmployeeType employeetype) {
        this.id = id;
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.badge = badge;
        this.department = department;
        this.shift = shift;
        this.employeetype = employeetype;
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
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append("#ID").append(id).append(':');
        s.append(' ').append(firstname).append(',');
        s.append(' ').append(lastname).append(',');
        s.append(' ').append(middlename).append(' ');
        s.append('(').append(badge).append(')');
        s.append(", Type: ").append(employeetype).append(',');
        s.append(", Department: ").append(department).append(',');
        s.append(", Active: ").append(active);
        

        return s.toString();                        
    }
    
    
    
}
