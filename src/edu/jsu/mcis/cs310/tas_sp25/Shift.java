/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25;
import java.time.LocalTime;
import java.time.Duration;
import java.util.HashMap;

/**
 *
 * @author afrix
 */
public class Shift {
    
    
    // declare instance variables
    private final int id;
    private final LocalTime shiftStart;
    private final LocalTime shiftStop;
    private final LocalTime lunchStart;
    private final LocalTime lunchStop;
    private final int lunchDuration;
    private final int shiftDuration;

    // create the constructor
    public Shift(int id, HashMap<String, String> shiftData){
        this.id = id;
        // converts the shift start/stop, lunch start/stop to LocalTime objects
        this.shiftStart = LocalTime.parse(shiftData.get("shiftStart"));
        this.shiftStop = LocalTime.parse(shiftData.get("shiftStop"));
        this.lunchStart = LocalTime.parse(shiftData.get("lunchStart"));
        this.lunchStop = LocalTime.parse(shiftData.get("lunchStop"));
        // calculates the length of lunch and shift
        this.lunchDuration = (int) Duration.between(this.lunchStart, this.lunchStop).toMinutes();
        this.shiftDuration = (int) Duration.between(this.shiftStart, this.shiftStop).toMinutes();
    }
    
    // getter methods
    public int getId(){
        return id;
    }
    
    public LocalTime getShiftStart(){
        return shiftStart;
    }
    
    public LocalTime getShiftStop(){
        return shiftStop;
    }
    
    public LocalTime getLunchStart(){
        return lunchStart;
    }
    
    public LocalTime getLunchStop(){
        return lunchStop;
    }
    
    public int getLunchDuration() {
        return lunchDuration;
    }
    
    public int getShiftDuration() {
        return shiftDuration;
    }
    
    @Override
    public String toString() {
        String shiftLabel;
        if (shiftStart.equals(LocalTime.parse("07:00")) &&
            shiftStop.equals(LocalTime.parse("15:30")) &&
            lunchStart.equals(LocalTime.parse("11:30")) &&
            lunchStop.equals(LocalTime.parse("12:00"))) {
            shiftLabel = "1 Early Lunch";
        } else {
            shiftLabel = String.valueOf(id);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Shift ").append(shiftLabel).append(": ");
        sb.append(shiftStart).append(" - ");
        sb.append(shiftStop).append(" (");
        sb.append(shiftDuration).append(" minutes); Lunch: ");
        sb.append(lunchStart).append(" - ");
        sb.append(lunchStop).append(" (");
        sb.append(lunchDuration).append(" minutes)");

        return sb.toString();
    }

    
}
