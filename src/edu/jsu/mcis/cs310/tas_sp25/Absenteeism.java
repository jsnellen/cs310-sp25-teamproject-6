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
 * // Assisting NLL 3-13-2025
 */
public class Absenteeism {
    // Initialze var - NLL
    private static Employee employee1;
    private static LocalDate startDateofPayPeriod; // represents start date of the pey period
    private static BigDecimal percentage; // represents employee's abesentee.
    
    
    
     // Constructors

    public Absenteeism(Employee employee1, LocalDate startDateofPayPeriod, BigDecimal percentage ) {
    this.employee1 = employee1;
    this.startDateofPayPeriod = startDateofPayPeriod;
    this.percentage = percentage;
    }
    
    // Getters Methods

    public static Employee getEmployee1() {
        return employee1;
    }

    public static LocalDate getStartDateofPayPeriod() {
        return startDateofPayPeriod;
    }

    public static BigDecimal getPercentage() {
        return percentage;
    }
}
