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
 * <p>
 * The Absenteeism class works with the record of an employee's attendance for a
 * set pay period in the TAS application, including the employee's 
 * information, the start of the pay period,  and the calculated absenteeism 
 * percentage.
 * </p>
 *
 * <p>
 * Absenteeism is measured as the percentage of scheduled work time not worked,
 * with negative values representing overtime.
 * </p>
 * 
 * @author Michael Frix
 * @author Nehemias Loarca Lucas
 */
public class Absenteeism {
    // Initialze var - NLL
    /**
     * The employee associated with the absenteeism record.
     */
    private static Employee employee;
    /**
     * The start date (Sunday) of the pay period.
     */
    private static LocalDate payPeriod; // represents start date of the pey period
    /**
     * The absenteeism percentage for the pay period.
     */
    private static BigDecimal percentage; // represents employee's abesentee.
    
 
    
     // Constructors-NLL
    
    /**
     * Constructs a new Absenteeism record for a set employee and his or her 
     * equivalent pay period. (The pay period is adjusted 
     * to the previous or same Sunday.)
     *
     * @param employee   the {@link Employee} for employee the absenteeism record is created
     * @param payPeriod  a date within the pay period (init to Sunday)
     * @param percentage the absenteeism percentage as a {@link BigDecimal}
     */
    public Absenteeism(Employee employee, LocalDate payPeriod, BigDecimal percentage ) {
    this.employee = employee;
    // this.payPeriod = payPeriod;
    // adjusts the provided date to the start of the pay period, to the previous or same sunday
    this.payPeriod = payPeriod.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
    this.percentage = percentage;
    }
        
    // Getters Methods-NLL
    /**
     * Returns the employee for this absenteeism record.
     *
     * @return the {@link Employee} object
     */
    public static Employee getEmployee() {
        return employee;
    }
    /**
     * Returns the normalized pay period start date (Sunday).
     *
     * @return the {@link LocalDate} of the pay period start
     */
    public static LocalDate getPayPeriod() {
        return payPeriod;
    }
    /**
     * Returns the absenteeism percentage for the current record.
     *
     * @return the absenteeism value as a {@link BigDecimal}
     */
    public static BigDecimal getPercentage() {
        return percentage;
    }
    // ToString method -NLL
    /**
     * Returns a formatted string representation of the absenteeism record.
     * Example: <code>#28DC3FB8 (Pay Period Starting 09-02-2018): 2.50%</code>
     *
     * @return a string showing badge ID, pay period date, and absenteeism percentage
     */
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
