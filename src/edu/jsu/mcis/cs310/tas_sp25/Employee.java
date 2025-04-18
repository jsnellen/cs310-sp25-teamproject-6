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
    

    /**
     * Constructs a new {@code Employee} with the provided data
     *
     * @param id            the unique ID of the employee
     * @param firstname     the employee's first name
     * @param middlename    the employee's middle name
     * @param lastname      the employee's last name
     * @param badge         the {@link Badge} associated with the employee
     * @param department    the {@link Department} the employee belongs to
     * @param shift         the {@link Shift} assigned to the employee
     * @param employeetype  the {@link EmployeeType}
     * @param active        
     */
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

     /**
     * Gets the unique employee ID
     *
     * @return the employee ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the employee's first name
     *
     * @return the first name
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Gets the employee's middle name
     *
     * @return the middle name
     */
    public String getMiddlename() {
        return middlename;
    }

    /**
     * Gets the employee's last name
     *
     * @return the last name
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Gets the date and time the employee became active
     *
     * @return the activation timestamp, or {@code null} if not set
     */
    public LocalDateTime getActive() {
        return active;
    }

    /**
     * Gets the employee's badge
     *
     * @return the employee's {@link Badge}
     */
    public Badge getBadge() {
        return badge;
    }

    /**
     * Gets the employee's department
     *
     * @return the {@link Department} object
     */
    public Department getDepartment() {
        return department;
    }

    /**
     * Gets the employee's assigned shift
     *
     * @return the {@link Shift} object
     */
    public Shift getShift() {
        return shift;
    }

    /**
     * Gets the employee's type
     *
     * @return the {@link EmployeeType}
     */
    public EmployeeType getEmployeetype() {
        return employeetype;
    }
    
        
    // ToString method 
    //rewrote this to pass tests in EmployeeFindTest.Java
    /**
     * Returns a string description of the employee, including name,
     * badge, type, department, and activation date
     *
     * @return formatted string describing the employee
     */
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
