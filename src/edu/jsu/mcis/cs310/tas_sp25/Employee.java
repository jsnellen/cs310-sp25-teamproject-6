/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The {@code Employee} class models a single employee within the Time and Attendance System.
 * <p>
 * This class contains all essential information for an employee, including identification,
 * full name, badge, department, assigned work shift, employment type, and activation timestamp.
 * It is instantiated and populated primarily through the {@link edu.jsu.mcis.cs310.tas_sp25.dao.EmployeeDAO}
 * using data from the database. Fields such as {@link Badge}, {@link Department}, and {@link Shift}
 * are linked model objects representing related records in other tables.
 * </p>
 * <p>
 * The {@code EmployeeType} is stored as an enum and indicates whether the employee is
 * part-time/temporary or full-time. The {@code active} field indicates the employee’s start date.
 * </p>
 * 
 * @author Nehemias Lucas
 */
public class Employee { //Class 
    // Initiate variables
    /** 
     * Unique numeric ID of the employee. 
     */
    private final int id;
    /**
     * First, middle, last name of the employee.
     */
    private final String firstname,middlename,lastname;
    /**
     * Timestamp of when the employee became active in the system.
     */
    private LocalDateTime active = null;
    /**
     * Badge object associated with this employee.
     */
    private final Badge badge;
    /**
     * Department to which the employee is assigned.
     */
    private final Department department;
    /**
     * Shift rules assigned to this employee.
     */
    private final Shift shift;
    /**
     * Type of employee (full-time or part-time).
     */
    private final EmployeeType employeetype;
    

    // Constructor
    /**
     * Constructs a new {@code Employee} with the provided parameters.
     *
     * @param id the numeric ID of the employee
     * @param firstname the employee's first name
     * @param middlename the employee's middle name
     * @param lastname the employee's last name
     * @param badge the {@code Badge} object assigned to the employee
     * @param department the {@code Department} the employee belongs to
     * @param shift the {@code Shift} rule set for the employee
     * @param employeetype the {@code EmployeeType} (PART_TIME or FULL_TIME)
     * @param active the activation timestamp of the employee
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
     * Gets the employee's numeric ID.
     *
     * @return the employee ID
     */
    public int getId() {
        return id;
    }
    /**
     * Gets the employee's first name.
     *
     * @return the first name
     */
    public String getFirstname() {
        return firstname;
    }
    /**
     * Gets the employee's middle name.
     *
     * @return the middle name
     */
    public String getMiddlename() {
        return middlename;
    }
    /**
     * Gets the employee's last name.
     *
     * @return the last name
     */
    public String getLastname() {
        return lastname;
    }
    /**
     * Gets the activation timestamp of the employee.
     *
     * @return the activation date and time
     */
    public LocalDateTime getActive() {
        return active;
    }
    /**
     * Gets the {@code Badge} associated with the employee.
     *
     * @return the {@code Badge} object
     */
    public Badge getBadge() {
        return badge;
    }
    /**
     * Gets the employee's assigned {@code Department}.
     *
     * @return the {@code Department} object
     */
    public Department getDepartment() {
        return department;
    }
    /**
     * Gets the {@code Shift} rule set for this employee.
     *
     * @return the {@code Shift} object
     */
    public Shift getShift() {
        return shift;
    }
    /**
     * Gets the {@code EmployeeType} (FULL_TIME or PART_TIME).
     *
     * @return the employee type
     */
    public EmployeeType getEmployeetype() {
        return employeetype;
    }
    
        
    // ToString method 
    //rewrote this to pass tests in EmployeeFindTest.Java
    /**
     * Returns a formatted string representing the full details of this {@code Employee},
     * including name, badge ID, type, department, and activation date.
     *
     * @return the formatted employee description
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
