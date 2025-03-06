package edu.jsu.mcis.cs310.tas_sp25.dao;

import java.time.*;
import java.util.*;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import com.github.cliftonlabs.json_simple.*;
import edu.jsu.mcis.cs310.tas_sp25.Punch; // imports Punch for DAT method
import edu.jsu.mcis.cs310.tas_sp25.Shift; // imports Shift for DAT method
import java.sql.Connection;

/**
 * 
 * Utility class for DAOs.  This is a final, non-constructable class containing
 * common DAO logic and other repeated and/or standardized code, refactored into
 * individual static methods.
 * 
 */
public final class DAOUtility { 
    

    
    //Creating Daily Accrued Total Method, Nehemias Lucas 3-3-2025
    public static int calculateTotalMinutes(ArrayList<Punch> dailypunchlist, Shift s){
        // Initialize variables
        Integer result = 0; // result is amount of minute accrued
        LocalTime startTime = null;
        LocalTime endTime = null;
        Integer durationBetween;
        try{
           
            for (Punch punch : dailypunchlist){  // clock in and clock out punches
                while (s.getShiftStart() != null || s.getShiftStop() != null){
                    
                    startTime = s.getShiftStart(); // gets time when shift starts
                    endTime = s.getShiftStop(); // gets time when shift stops
                    durationBetween = s.getShiftDuration(); // calculates minutes between startTime ans endTime
                    result  = result + durationBetween ; // Adds minutes of to total
                    return result;
                }
                
                // Subtract lunch duration from total time accrued between shift start and stop
                while (s.getLunchStart() != null || s.getLunchStop() != null){// clockin and timeout punches
                result = result - s.getLunchDuration();
                return result;
                }
            }
            
           
        }catch(Exception e) {
            throw new DAOException(e.getMessage());
        } 
        
        return result;
    }    
    



}