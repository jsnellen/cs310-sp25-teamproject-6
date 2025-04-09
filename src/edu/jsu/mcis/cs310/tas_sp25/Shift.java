/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25;
import java.time.LocalTime;
import java.time.Duration;
import java.util.HashMap;
import edu.jsu.mcis.cs310.tas_sp25.DailySchedule;
import java.time.DayOfWeek;

/**
 * <p>The Shift class represents a scheduled work shift in
 * the Time and Attendance system. Each Shift includes start/stop times,
 * lunch start/stop times, and shift-rule parameters (for example:
 * grace period, dock penalty, and rounding interval).</p>
 *
 * <p>By providing these fields, the Shift object helps determine when an
 * employee's punches should be adjusted, how long their lunch should last, 
 * and the total scheduled minutes for this shift.</p>
 *
 * <p>These parameters are retrieved from the database via a
 * {@code ShiftDAO} class.</p>
 *
 * @author Michael Frix
 */
public class Shift {
   
    // declare instance variables - MF
    
    /**
     * The numeric identifier for this shift, matching a record in the database.
     */
    private final int id;
    /**
     * A short descriptive label for this shift.
     */
    private final String description;
    /**
     * The start time of the shift.
     */
    
    //private final LocalTime shiftStart;
    /**
     * The stop time of the shift.
     */
    //private final LocalTime shiftStop;
    /**
     * The start time of the lunch break.
     */
    //private final LocalTime lunchStart;
    /**
     * The stop time of the lunch break.
     */
    //private final LocalTime lunchStop;
    /**
     * The length of the lunch break (minutes).
     */
    //private final int lunchDuration;
    /**
     * The total scheduled duration of the shift (minutes).
     */
    //private final int shiftDuration;
    /**
     * The rounding interval (minutes) used for adjusting punches near
     * the start or end of the shift or break periods.
     */
    //private final int roundInterval;
    /**
     * The grace period (minutes) for late arrivals or early departures
     */
    //private final int gracePeriod;
    /**
     * The docking penalty (in minutes) which is applied if an employee
     * arrives too late or leaves too early beyond the grace period.
     */
    //private final int dockPenalty;
    /**
     * The minimum number of minutes an employee must work before a
     * lunch deduction is applied, often used to skip lunch for short shifts.
     */
    //private final int lunchThreshold;    
    
    private DailySchedule defaultschedule;
    private HashMap<Integer, DailySchedule> dailyschedules;

    // create the constructor - MF
    /**
     * Constructs a new Shift by reading fields from a map of string values.
     * <p>This constructor converts times (shift start/stop and lunch start/stop)
     * to {@link LocalTime}, and parses integer fields like rounding interval,
     * grace period, dock penalty, and lunch threshold. It then calculates
     * {@code lunchDuration} and {@code shiftDuration} based on the start and
     * stop times provided.</p>
     *
     * @param id the numeric shift ID (as stored in the database)
     * @param shiftData a map of string values for all shift parameters.
     *                  Expected keys include "description", "shiftStart",
     *                  "shiftStop", "lunchStart", "lunchStop", "roundInterval",
     *                  "gracePeriod", "dockPenalty", and "lunchThreshold".
     */
    public Shift(int id, String description, DailySchedule defaultschedule) {
        this.id = id;
        this.description = description;
        // converts the shift start/stop, lunch start/stop to LocalTime objects
        //this.shiftStart = LocalTime.parse(shiftData.get("shiftStart"));
        //this.shiftStop = LocalTime.parse(shiftData.get("shiftStop"));
        //this.lunchStart = LocalTime.parse(shiftData.get("lunchStart"));
        //this.lunchStop = LocalTime.parse(shiftData.get("lunchStop"));
        // calculates the length of lunch and shift
        //this.lunchDuration = (int) Duration.between(this.lunchStart, this.lunchStop).toMinutes();
        //this.shiftDuration = (int) Duration.between(this.shiftStart, this.shiftStop).toMinutes();
        
        //this.roundInterval = Integer.parseInt(shiftData.get("roundInterval"));
        //this.gracePeriod = Integer.parseInt(shiftData.get("gracePeriod"));
        //this.dockPenalty = Integer.parseInt(shiftData.get("dockPenalty"));
        //this.lunchThreshold = Integer.parseInt(shiftData.get("lunchThreshold"));
        
        this.defaultschedule = defaultschedule;
        this.dailyschedules = new HashMap<>();
        
        // Set all weekdays (Monday–Friday) to use the default schedule
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day.getValue() >= DayOfWeek.MONDAY.getValue() && day.getValue() <= DayOfWeek.FRIDAY.getValue()) {
                dailyschedules.put(day.getValue(), defaultschedule);
            }
        }
    }

    // getter methods - MF
    /**
     * Returns the numeric ID of this shift.
     *
     * @return the shift ID
     */
    public int getId(){
        return id;
    }
    /**
     * Returns the descriptive label for this shift.
     *
     * @return the shift description
     */
    public String getDescription() { 
        return description; 
    }
    
    // Default schedule
    public DailySchedule getDefaultschedule() {
        return defaultschedule;
    }

    // Daily schedule access
    public DailySchedule getDailySchedule(DayOfWeek day) {
        return dailyschedules.getOrDefault(day.getValue(), defaultschedule);
    }
    
    /**
     * Returns the start time of the shift.
     *
     * @return shift start time
     */
    public LocalTime getShiftStart(){
        return defaultschedule.getShiftstart();
    }
    /**
     * Returns the stop time of the shift.
     *
     * @return shift stop time
     */
    public LocalTime getShiftStop(){
        return defaultschedule.getShiftstop();
    }
    /**
     * Returns the time at which lunch begins.
     *
     * @return lunch start time
     */
    public LocalTime getLunchStart(){
        return defaultschedule.getLunchstart();
    }
    /**
     * Returns the time at which lunch ends.
     *
     * @return lunch stop time
     */
    public LocalTime getLunchStop(){
        return defaultschedule.getLunchstop();
    }
    /**
     * Returns the total length of lunch (minutes).
     *
     * @return lunch duration in minutes
     */
    public int getLunchDuration() {
        return (int) java.time.Duration.between(getLunchStart(), getLunchStop()).toMinutes();
    }
    /**
     * Returns the total length of this shift in minutes, including lunch.
     *
     * @return the full shift duration in minutes
     */
    public int getShiftDuration() {
        return (int) java.time.Duration.between(getShiftStart(), getShiftStop()).toMinutes();
    }
    
    // Added new getter methods for adjustment parameters - WW
    /**
     * Returns the rounding interval (minutes) used for punch adjustments.
     *
     * @return the round interval
     */
    public int getRoundInterval(){
        return defaultschedule.getRoundinterval();
    }
    /**
     * Returns the grace period (minutes) before dock penalty is applied.
     *
     * @return the grace period
     */
    public int getGracePeriod(){
        return defaultschedule.getGraceperiod();
    }
    /**
     * Returns the dock penalty (minutes) for excessive tardiness/early departure.
     *
     * @return the dock penalty
     */
    public int getDockPenalty(){
        return defaultschedule.getDockpenalty();
    }
    /**
     * Returns the lunch threshold (minutes) which must be exceeded
     * for a lunch deduction to apply.
     *
     * @return the lunch threshold in minutes
     */
    public int getLunchThreshold() { 
        return defaultschedule.getLunchthreshold();
    }
    
    
    // MF
    /**
     * Returns a formatted string representation of the shift.
     *
     * @return a formatted string with the shift's times and durations
     */
    @Override
    public String toString() {

        String label = (description != null && !description.isEmpty())
                ? description
                : "Shift " + id;

        StringBuilder sb = new StringBuilder(label);
        sb.append(": ");
        
        sb.append(getShiftStart()).append(" - ");
        sb.append(getShiftStop()).append(" (");
        sb.append(getShiftDuration()).append(" minutes); Lunch: ");
        sb.append(getLunchStart()).append(" - ");
        sb.append(getLunchStop()).append(" (");
        sb.append(getLunchDuration()).append(" minutes)");

        return sb.toString();
    }
  
}
