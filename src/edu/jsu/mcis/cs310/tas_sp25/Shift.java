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
    private final String description;
    private final LocalTime shiftStart;
    private final LocalTime shiftStop;
    private final LocalTime lunchStart;
    private final LocalTime lunchStop;
    private final int lunchDuration;
    private final int shiftDuration;
    // add the previously missing fields
    private final int roundInterval;         
    private final int gracePeriod;      
    private final int dockPenalty; 
    private final int lunchThreshold;         

    // create the constructor
    public Shift(int id, HashMap<String, String> shiftData){
        this.id = id;
        this.description = shiftData.get("description");
        // converts the shift start/stop, lunch start/stop to LocalTime objects
        this.shiftStart = LocalTime.parse(shiftData.get("shiftStart"));
        this.shiftStop = LocalTime.parse(shiftData.get("shiftStop"));
        this.lunchStart = LocalTime.parse(shiftData.get("lunchStart"));
        this.lunchStop = LocalTime.parse(shiftData.get("lunchStop"));
        // calculates the length of lunch and shift
        this.lunchDuration = (int) Duration.between(this.lunchStart, this.lunchStop).toMinutes();
        this.shiftDuration = (int) Duration.between(this.shiftStart, this.shiftStop).toMinutes();
        
        this.roundInterval = Integer.parseInt(shiftData.get("roundInterval"));
        this.gracePeriod = Integer.parseInt(shiftData.get("gracePeriod"));
        this.dockPenalty = Integer.parseInt(shiftData.get("dockPenalty"));
        this.lunchThreshold = Integer.parseInt(shiftData.get("lunchThreshold"));
        
    }
    
    // getter methods
    public int getId(){
        return id;
    }
    
    public String getDescription() { 
        return description; 
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
    
    public int getRoundInterval() { 
        return roundInterval; 
    }
    public int getGracePeriod() { 
        return gracePeriod; 
    }
    public int getDockPenalty() { 
        return dockPenalty; 
    }
    public int getLunchThreshold() { 
        return lunchThreshold; 
    }
    
    @Override
    public String toString() {

        String label = (description != null && !description.isEmpty())
                ? description
                : "Shift " + id;

        StringBuilder sb = new StringBuilder(label);
        sb.append(": ");  

        sb.append(shiftStart).append(" - ");
        sb.append(shiftStop).append(" (");
        sb.append(shiftDuration).append(" minutes); Lunch: ");
        sb.append(lunchStart).append(" - ");
        sb.append(lunchStop).append(" (");
        sb.append(lunchDuration).append(" minutes)");

        return sb.toString();
    }


    
}
