package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.EventType;
import java.time.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.temporal.TemporalAdjusters;
import com.github.cliftonlabs.json_simple.*;
import edu.jsu.mcis.cs310.tas_sp25.DailySchedule;
import edu.jsu.mcis.cs310.tas_sp25.Punch; // imports Punch for DAT method
import edu.jsu.mcis.cs310.tas_sp25.Shift; // imports Shift for DAT method
import java.math.BigDecimal; // imported BigDecimal for calculateAbsenteeism method - nll
import java.math.RoundingMode; // imported RoundingMode for calculateAbsenteeism method - nll
/**
 * 
 * Utility class for DAOs.  This is a final, non-constructable class containing
 * common DAO logic and other repeated and/or standardized code, refactored into
 * individual static methods.
 * 
 */
public final class DAOUtility { 
    

    /**
    * Calculates the total number of minutes worked in a day based on punches and shift.
    *
    * @param dailypunchlist list of punches for a day
    * @param s the Shift object
    * @return total minutes worked that day
    * @author Nehemias Lucas, 3-3-2025
    */
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
        if (startTime.isBefore(s.getLunchStart()) && endTime.isAfter(s.getLunchStop())) {
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
    /**
    * Calculates the total minutes worked across a full pay period based on punch data.
    *
    * @param punchlist list of all punches in the pay period
    * @param shift the Shift object for the employee
    * @return total worked minutes in the pay period
    * @author NLL
    */
    
    public static int calculateTotalWorkedMinutesInPayPeriod(ArrayList<Punch> punchlist, Shift shift) {
        LocalDate sampleDate = punchlist.get(0).getAdjustedtimestamp().toLocalDate();
        LocalDate payPeriodStart = sampleDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate payPeriodEnd = sampleDate.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));

        // Group punches by date
        Map<LocalDate, ArrayList<Punch>> punchesByDate = new HashMap<>();
        for (Punch punch : punchlist) {
            LocalDate date = punch.getAdjustedtimestamp().toLocalDate();
            punchesByDate.computeIfAbsent(date, k -> new ArrayList<>()).add(punch);
        }

        int totalWorkedMinutes = 0;

        //System.out.println("--- Daily Worked Minutes Breakdown ---");

        for (LocalDate day = payPeriodStart; !day.isAfter(payPeriodEnd); day = day.plusDays(1)) {
            if (punchesByDate.containsKey(day)) {
                ArrayList<Punch> dayPunches = punchesByDate.get(day);
                int workedMinutes = calculateTotalMinutes(dayPunches, shift);
                totalWorkedMinutes += workedMinutes;
                //System.out.println("Day: " + day.getDayOfWeek() + " (" + day + "), Worked Minutes: " + workedMinutes);
            } else {
                //System.out.println("Day: " + day.getDayOfWeek() + " (" + day + "), No punches found.");
            }
        }

        //System.out.println("Total Worked Minutes: " + totalWorkedMinutes);
        //System.out.println("---------------------------------------");

        return totalWorkedMinutes;
    }
    /**
     * Converts a list of punches to JSON format.
     *
     * @param dailypunchlist list of punches to convert
     * @return JSON-formatted string of punch data
     * @author Parinita Sedai, 03/09/2025
     */

    
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
    /**
     * Converts a list of punches and totals (worked time, absenteeism)
     * to a JSON-formatted string.
     *
     * @param punchlist the list of punches
     * @param shift the Shift object
     * @return JSON-formatted string including punch data and totals
     * @author NLL
     */
    
    public static String getPunchListPlusTotalsAsJSON(ArrayList<Punch> punchlist, Shift shift) {
        ArrayList<HashMap<String, String>> punchData = new ArrayList<>();
        int totalMinutes = calculateTotalWorkedMinutesInPayPeriod(punchlist, shift);
        BigDecimal absenteeism = calculateAbsenteeism(punchlist, shift);

        for (Punch punch : punchlist) {
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

        HashMap<String, Object> json = new HashMap<>();
        json.put("punchlist", punchData);
        json.put("totalminutes", totalMinutes);
        json.put("absenteeism", String.format("%.2f%%", absenteeism));

        return Jsoner.serialize(json);
    }
    
    /**
    * Calculates the absenteeism percentage for a pay period based on expected vs. actual minutes.
     *
     * @param punchlist the punch data for the pay period
     * @param shift the Shift object for the employee
     * @return absenteeism percentage as BigDecimal
     */
 
    public static BigDecimal calculateAbsenteeism(ArrayList<Punch> punchlist, Shift shift) {
        int totalWorkedMinutes = calculateTotalWorkedMinutesInPayPeriod(punchlist, shift);
        
        //  ( 510 - 30 ) * 5
        //int totalScheduledMinutes =  (shift.getShiftDuration() - shift.getLunchDuration()) * workDays;
        
        
        // refactored to use getDailySchedule for each day
        int totalScheduledMinutes = 0;

        LocalDate sampleDate = punchlist.get(0).getAdjustedtimestamp().toLocalDate();
        LocalDate payPeriodStart = sampleDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate payPeriodEnd = sampleDate.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
        
        //System.out.println("\n--- Calculating Absenteeism for Pay Period: " + payPeriodStart + " to " + payPeriodEnd + " ---");

        for (LocalDate day = payPeriodStart; !day.isAfter(payPeriodEnd); day = day.plusDays(1)) {
            DayOfWeek dow = day.getDayOfWeek();

            if (dow.getValue() >= DayOfWeek.MONDAY.getValue() && dow.getValue() <= DayOfWeek.FRIDAY.getValue()) {
                DailySchedule schedule = shift.getDailySchedule(dow);

                int shiftMinutes = (int) Duration.between(schedule.getShiftstart(), schedule.getShiftstop()).toMinutes();
                int lunchMinutes = (int) Duration.between(schedule.getLunchstart(), schedule.getLunchstop()).toMinutes();
                int minutes = shiftMinutes - lunchMinutes;
                
                //System.out.println("Day: " + dow);
                //System.out.println("  Shift Start: " + schedule.getShiftstart());
                //System.out.println("  Shift Stop: " + schedule.getShiftstop());
                //System.out.println("  Lunch: " + schedule.getLunchstart() + "–" + schedule.getLunchstop());
                //System.out.println("  Scheduled Minutes: " + minutes);
                totalScheduledMinutes += minutes;
            }
        }
        //System.out.println("Total Scheduled Minutes: " + totalScheduledMinutes);
        //System.out.println("Total Worked Minutes: " + totalWorkedMinutes);

        double absenteeism = ((double)(totalScheduledMinutes - totalWorkedMinutes) / totalScheduledMinutes) * 100;
        BigDecimal percentage = BigDecimal.valueOf(absenteeism).setScale(2, RoundingMode.HALF_UP);
        
        //System.out.println("Absenteeism %: " + percentage);
        //System.out.println("--------------------------------------------------\n");


        return percentage;
    }
    
    

}
