/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25;
import java.math.BigDecimal; // Imported for BigDecimal var - NLL
import java.time.LocalDate; // Imports for LocalDate variable - NLL


/**
 *
 * @author afrix
 * // Assisting, NLL 3-13-2025
 */
public class Absenteeism {
    // Initialze var - NLL
    private static Employee employee;
    private static LocalDate startDateofPayPeriod; // represents start date of the pey period
    private static BigDecimal percentage; // represents employee's abesentee.
    
    
    
     // Constructors-NLL

    public Absenteeism(Employee employee, LocalDate startDateofPayPeriod, BigDecimal percentage ) {
    this.employee = employee;
    this.startDateofPayPeriod = startDateofPayPeriod;
    this.percentage = percentage;
    }
    
    // Getters Methods-NLL

    public static Employee getEmployee() {
        return employee;
    }

    public static LocalDate getStartDateofPayPeriod() {
        return startDateofPayPeriod;
    }

    public static BigDecimal getPercentage() {
        return percentage;
    }
    // ToString method -NLL
    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        s.append('#').append(employee).append(' ');
        s.append('(').append("Pay Period Starting").append(startDateofPayPeriod).append(')');
        s.append(':').append(percentage).append("%");

        return s.toString();
    }
}
