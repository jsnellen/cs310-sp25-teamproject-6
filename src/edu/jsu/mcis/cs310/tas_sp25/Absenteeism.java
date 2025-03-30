/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25;
import java.math.BigDecimal; // Imported for BigDecimal var - NLL
import java.time.LocalDate; // Imports for LocalDate variable - NLL
import java.time.format.DateTimeFormatter; //Imported to change format of payPeriod to requirements of test 
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import java.text.DecimalFormat;


/**
 *
 * @author afrix
 * // Assisting, NLL 3-13-2025
 */
public class Absenteeism {
    // Initialze var - NLL
    private static Employee employee;
    private static LocalDate payPeriod; // represents start date of the pey period
    private static BigDecimal percentage; // represents employee's abesentee.
    
 
    
     // Constructors-NLL

    public Absenteeism(Employee employee, LocalDate payPeriod, BigDecimal percentage ) {
    this.employee = employee;
    // this.payPeriod = payPeriod;
    // adjusts the provided date to the start of the pay period, to the previous or same sunday
    this.payPeriod = payPeriod.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
    this.percentage = percentage;
    }
        
    // Getters Methods-NLL

    public static Employee getEmployee() {
        return employee;
    }

    public static LocalDate getPayPeriod() {
        return payPeriod;
    }

    public static BigDecimal getPercentage() {
        return percentage;
    }
    // ToString method -NLL
    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();
        
        String payPeriodformat = (payPeriod != null) ? payPeriod.format(DateTimeFormatter.ofPattern("MM-dd-yyyy")) : "null"; // Reference from Employee class
        
        DecimalFormat df = new DecimalFormat("0.00");
        String formattedPercentage = df.format(percentage);
       
        s.append('#').append(employee.getBadge().getId()).append(' ');
        s.append('(').append("Pay Period Starting ").append(payPeriodformat).append(')');
        s.append(": ").append(formattedPercentage).append("%");

        return s.toString();
    }
}
