package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.EventType;
import java.time.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.format.DateTimeFormatter;
import com.github.cliftonlabs.json_simple.*;
import edu.jsu.mcis.cs310.tas_sp25.Punch; // imports Punch for DAT method
import edu.jsu.mcis.cs310.tas_sp25.Shift; // imports Shift for DAT method
import java.sql.Connection;
import java.math.BigDecimal; // imported BigDecimal for calculateAbsenteeism method - nll
import java.math.RoundingMode; // imported RoundingMode for calculateAbsenteeism method - nll
import java.text.DecimalFormat;
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
        // simple check if there are no punches, return 0
        if (dailypunchlist == null || dailypunchlist.isEmpty()) {
            return 0;
        }
        
        // Initialize variables
        Integer result = 0; // result is amount of minute accrued
        LocalTime startTime = null;
        LocalTime endTime = null;
        Integer durationBetween;
        
        try{
        
        // sort punches by their adjusted timestamps
        // I dont think this is needed due to the list already being sorted
        //dailypunchlist.sort(Comparator.comparing(Punch::getAdjustedtimestamp));
        
        Punch clockInPunch = dailypunchlist.get(0);
        Punch clockOutPunch = dailypunchlist.get(dailypunchlist.size() - 1);
        
        // check to see if the punches are the correct EventType
        if (clockInPunch.getPunchtype() != EventType.CLOCK_IN || clockOutPunch.getPunchtype() != EventType.CLOCK_OUT) {
            return 0;
        }
        
        // get the adjusted start and end time
        startTime = clockInPunch.getAdjustedtimestamp().toLocalTime();
        endTime = clockOutPunch.getAdjustedtimestamp().toLocalTime();
        
        // Compute total minutes between adjusted clock-in and clock-out times
        durationBetween = (int) java.time.temporal.ChronoUnit.MINUTES.between(
                clockInPunch.getAdjustedtimestamp(), 
                clockOutPunch.getAdjustedtimestamp()
        );
        // If the shift spans the entire lunch period, subtract lunch duration
        // the employee must be clocked in before lunch starts and clocked out after lunch ends
        if (startTime.isBefore(s.getLunchStart()) && endTime.isAfter(s.getLunchStart())) {
            durationBetween -= s.getLunchDuration();
        }
        // set and return the result
        result = durationBetween;
        
        /*
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
        */
        
           
        }catch(Exception e) {
            throw new DAOException(e.getMessage());
    }  
    
        return result;
    }  

    public static int calculateTotalWorkedMinutesInPayPeriod(ArrayList<Punch> punchlist, Shift shift) {
        LocalDate sampleDate = punchlist.get(0).getAdjustedtimestamp().toLocalDate();
        LocalDate payPeriodStart = sampleDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

        // Group punches by date
        Map<LocalDate, ArrayList<Punch>> punchesByDate = new HashMap<>();
        for (Punch punch : punchlist) {
            LocalDate date = punch.getAdjustedtimestamp().toLocalDate();
            punchesByDate.computeIfAbsent(date, k -> new ArrayList<>()).add(punch);
        }

        int totalWorkedMinutes = 0;
        for (int i = 1; i <= 5; i++) {
            LocalDate day = payPeriodStart.plusDays(i);
            if (punchesByDate.containsKey(day)) {
                ArrayList<Punch> dayPunches = punchesByDate.get(day);
                totalWorkedMinutes += calculateTotalMinutes(dayPunches, shift); // Added shift argument- nll
            }
        }

        LocalDate saturday = payPeriodStart.plusDays(6);
        if (punchesByDate.containsKey(saturday)) {
            ArrayList<Punch> dayPunches = punchesByDate.get(saturday);
            totalWorkedMinutes += calculateTotalMinutes(dayPunches, shift); // Added shift argument- nll
        }

        return totalWorkedMinutes;
    }
/**
     * Converts a list of Punch objects into a JSON-formatted string.  
     * Each punch is represented as a HashMap containing relevant details,  
     * including timestamps, badge ID, terminal ID, punch type, and adjustment type.  
     *  
     * Parinita Sedai 03/09/2025  */
    
    
    public static String getPunchListAsJSON(ArrayList<Punch> dailypunchlist) {
        ArrayList<HashMap<String, String>> punchData = new ArrayList<>();
        
        for (Punch punch : dailypunchlist) {
            HashMap<String, String> punchMap = new HashMap<>();
            
            punchMap.put("id", String.valueOf(punch.id()));
            punchMap.put("badgeid", punch.getBadge().getId());
            punchMap.put("terminalid", String.valueOf(punch.getTerminalid()));
            punchMap.put("punchtype", punch.getPunchtype().toString());
            punchMap.put("originaltimestamp", punch.getOriginaltimestamp().format(Punch.TIMESTAMP_FORMAT).toUpperCase());
            punchMap.put("adjustedtimestamp", punch.getAdjustedtimestamp().format(Punch.TIMESTAMP_FORMAT).toUpperCase());
            punchMap.put("adjustmenttype", punch.getAdjustmentType().toString());

            punchData.add(punchMap);
        }
        
        return Jsoner.serialize(punchData);     
    }
    
    /*
    public static String getPunchListPlusTotalsAsJSON(ArrayList<Punch> punchlist, Shift shift) {
        ArrayList<HashMap<String, String>> punchData = new ArrayList<>();
        int totalMinutes = calculateTotalWorkedMinutesInPayPeriod(punchlist, shift);
        BigDecimal absenteeismPercentage = calculateAbsenteeism(punchlist, shift);
        
        
        String jsonString = Jsoner.serialize(json);
        
        return jsonString;
    }
    */
    
    public static BigDecimal calculateAbsenteeism(ArrayList<Punch> punchlist, Shift shift) {
        LocalDate sampleDate = punchlist.get(0).getAdjustedtimestamp().toLocalDate();
        LocalDate payPeriodStart = sampleDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        /*
        List<LocalDate> scheduledDays = new ArrayList<>();
        // Monday through Friday (days 1 through 5)
        for (int i = 1; i <= 5; i++) {
            scheduledDays.add(payPeriodStart.plusDays(i));
        }

        // https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html
        
        // Group punches by date
        Map<LocalDate, ArrayList<Punch>> punchesByDate = new HashMap<>();
        for (Punch punch : punchlist) {
            LocalDate date = punch.getAdjustedtimestamp().toLocalDate();
            punchesByDate.computeIfAbsent(date, k -> new ArrayList<>()).add(punch);
        }
        */
        
        int totalWorkedMinutes = calculateTotalWorkedMinutesInPayPeriod(punchlist, shift);
        
        //  ( 510 - 30 ) * 5
        int totalScheduledMinutes =  (shift.getShiftDuration() - shift.getLunchDuration()) * 5;

        double absenteeism = ((double)(totalScheduledMinutes - totalWorkedMinutes) / totalScheduledMinutes) * 100;
        BigDecimal percentage = BigDecimal.valueOf(absenteeism).setScale(2, RoundingMode.HALF_UP);
        
        return percentage;
    }
    
    

}
