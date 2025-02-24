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
    
    // toString method for representing the details of the shift
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        
        sb.append("Shift ID: ").append(id).append(", ");
        sb.append("Shift Start: ").append(shiftStart).append(", ");
        sb.append("Lunch Start: ").append(lunchStart).append(", ");
        sb.append("Lunch Stop: ").append(lunchStop).append(", ");
        sb.append("Shift Stop: ").append(shiftStop).append(", ");
        sb.append("Lunch Duration: ").append(lunchDuration).append(" minutes, ");
        sb.append("Shift Duration: ").append(shiftDuration).append(" minutes");
        
        return sb.toString();

    }
}
