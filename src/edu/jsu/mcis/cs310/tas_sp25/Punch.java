/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.DayOfWeek;  // added for DayOfWeek

/**
 *
 * @Creating Punch class Nehemias Loarca Lucas 2-21-2025
 */
public class Punch {
    // Intialize
    private final int terminalid;
    private final Badge badge;
    private final EventType punchtype;
    private final Integer id;
    
    //changed orginaltimestamp to originaltimestamp - WW
    private PunchAdjustmentType adjustmentType = null;
    private LocalDateTime originaltimestamp = null;
    private LocalDateTime adjustedtimestamp = null;
    
    // Constructor
    public Punch(int terminalid, Badge badge, EventType punchtype){ // for new punch objects
        this.terminalid = terminalid;
        this.badge = badge;
        this.punchtype = punchtype;
        this.originaltimestamp = LocalDateTime.now();
        this.id = null;
    }
    public Punch(int id, int terminalid, Badge badge,LocalDateTime originaltimestamp, EventType punchtype){ // for existing punch objects
        this.id = id;  // Fixed: assign the provided id instead of null - WW
        this.terminalid = terminalid;
        this.badge = badge;
        this.originaltimestamp = originaltimestamp;
        this.punchtype = punchtype;
        
    }
    
    
    // Getters
    public int getTerminalid() {
        return terminalid;
    }

    public Badge getBadge() {
        return badge;
    }

    public EventType getPunchtype() {
        return punchtype;
    }

    public LocalDateTime getOriginaltimestamp() {
        return originaltimestamp;
    }
    public int id(){
        return id;
    }
    
    // created this printOriginal method -ww
    public String printOriginal() {
        // Define date-time format "SUN 2/23/2025 05:26:07"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MM/dd/yyyy HH:mm:ss");

        // Convert the entire string to uppercase
        String formattedTimestamp = originaltimestamp.format(formatter).toUpperCase();

        // Get the EventType string representation
        String eventDescription = punchtype.toString();

        // StringBuilder for constructing the string
        StringBuilder sb = new StringBuilder();
        sb.append("#").append(badge.getId());
        sb.append(" ").append(eventDescription);
        sb.append(": ").append(formattedTimestamp);

        return sb.toString();
    }
    
    public String printAdjusted() {
        // Define date-time format "SUN 2/23/2025 05:26:07"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MM/dd/yyyy HH:mm:ss");
        
        // Convert the entire string to uppercase
        String formattedTimestamp = adjustedtimestamp.format(formatter).toUpperCase();
        
        // Build the string
        StringBuilder sb = new StringBuilder();
        sb.append("#").append(badge.getId());
        sb.append(" ").append(punchtype);
        sb.append(": ").append(formattedTimestamp);
        sb.append(" (").append(adjustmentType.toString()).append(")");
        
        return sb.toString();
    }

    //toString
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append('#').append(id).append(' ');
        sb.append('#').append(terminalid).append(' ');
        sb.append("Badge:").append(badge).append(' ');
        sb.append("Punch Type:").append(punchtype).append(' ');
        

        return sb.toString();

    }
    
    public void adjust(Shift s) {
        // Use the original timestamp with nanoseconds zero
        LocalDateTime original = originaltimestamp.withNano(0);
        LocalDate date = original.toLocalDate();

        LocalDateTime shiftStartTime = LocalDateTime.of(date, s.getShiftStart());
        LocalDateTime shiftStopTime  = LocalDateTime.of(date, s.getShiftStop());
        LocalDateTime lunchStartTime = LocalDateTime.of(date, s.getLunchStart());
        LocalDateTime lunchStopTime  = LocalDateTime.of(date, s.getLunchStop());

        // Retrieve shift parameters
        int interval = s.getRoundInterval();
        int grace = s.getGracePeriod();
        int dock = s.getDockPenalty();

        // If the punch occurs on a weekend, perform IntervalRound
        if (isWeekend(original)) {
            applyIntervalRound(original, interval);
            return;
        }

        // Adjustments for CLOCK_IN punches:
        if (punchtype == EventType.CLOCK_IN) {
            // Early clock-in: if before shift start and within the interval, adjust to shift start
            if (original.isBefore(shiftStartTime)) {
                long diff = java.time.Duration.between(original, shiftStartTime).toMinutes();
                if (diff <= interval) {
                    setAdjustment(shiftStartTime, PunchAdjustmentType.SHIFT_START);
                    return;
                }
            }
            // Exactly at shift start.
            if (original.equals(shiftStartTime)) {
                setAdjustment(shiftStartTime, PunchAdjustmentType.SHIFT_START);
                return;
            }
            // If within the grace period after shift start, adjust to shift start.
            if (original.isAfter(shiftStartTime) && original.isBefore(shiftStartTime.plusMinutes(grace))) {
                setAdjustment(shiftStartTime, PunchAdjustmentType.SHIFT_START);
                return;
            }
            // Lunch break: if the punch occurs during lunch, adjust to lunch stop.
            if (isDuringLunch(original, lunchStartTime, lunchStopTime)) {
                setAdjustment(lunchStopTime, PunchAdjustmentType.LUNCH_STOP);
                return;
            }
            // Dock penalty: if the punch is later than the grace period but before shiftStart + dock.
            if (original.isBefore(shiftStartTime.plusMinutes(dock))) {
                setAdjustment(shiftStartTime.plusMinutes(dock), PunchAdjustmentType.SHIFT_DOCK);
                return;
            }
        }

        // Adjustments for CLOCK_OUT punches:
        if (punchtype == EventType.CLOCK_OUT) {
            // First, if the punch (when seconds are zeroed) exactly equals shift stop
            if (original.withSecond(0).equals(shiftStopTime)) {
                if (s.getDockPenalty() > 0) {
                    setAdjustment(shiftStopTime, PunchAdjustmentType.SHIFT_DOCK);
                } else {
                    setAdjustment(shiftStopTime, PunchAdjustmentType.NONE);
                }
                return;
            }
            // Late clock-out: if after shift stop and within the interval, adjust to shift stop
            if (original.isAfter(shiftStopTime)) {
                long diff = java.time.Duration.between(shiftStopTime, original).toMinutes();
                if (diff <= interval) {
                    setAdjustment(shiftStopTime, PunchAdjustmentType.SHIFT_STOP);
                    return;
                }
            }
            // Lunch break: if the punch occurs during lunch, adjust to lunch start
            if (isDuringLunch(original, lunchStartTime, lunchStopTime)) {
                setAdjustment(lunchStartTime, PunchAdjustmentType.LUNCH_START);
                return;
            }
            // For punches before shift stop, check:
            if (original.isBefore(shiftStopTime)) {
                // If the punch is very near shift stop (difference less than or equal to half the interval)
                // then round upward to shift stop
                long diff = java.time.Duration.between(original, shiftStopTime).toMinutes();
                if (diff <= (interval / 2.0)) {
                    setAdjustment(shiftStopTime, PunchAdjustmentType.SHIFT_STOP);
                    return;
                }
                // Otherwise, if the punch is within the grace period before shift stop, adjust to shift stop
                if (!original.isBefore(shiftStopTime.minusMinutes(grace))) {
                    setAdjustment(shiftStopTime, PunchAdjustmentType.SHIFT_STOP);
                    return;
                }
                // Or if within the dock penalty window, adjust to shift stop - dock
                if (!original.isBefore(shiftStopTime.minusMinutes(dock))) {
                    setAdjustment(shiftStopTime.minusMinutes(dock), PunchAdjustmentType.SHIFT_DOCK);
                    return;
                }
            }
        }
        
        // if no condition is met, use IntervalRound
        applyIntervalRound(original, interval);
    }

    /* --- --- adjust() helper methods --- --- */

        // Rounds the given time to the nearest multiple of interval minutes
        private void applyIntervalRound(LocalDateTime time, int interval) {
            int hour = time.getHour();
            int minute = time.getMinute();
            int second = time.getSecond();
            double totalMinutes = hour * 60 + minute + second / 60.0;

            // Round total minutes to the nearest multiple of the interval
            double roundedTotal = Math.round(totalMinutes / interval) * interval;

            int newHour = (int) (roundedTotal / 60);
            int newMinute = (int) (roundedTotal % 60);

            LocalDateTime roundedTime = time.withHour(newHour).withMinute(newMinute).withSecond(0);

            // If no change is made, set adjustment type to NONE; otherwise, INTERVAL_ROUND.
            if (roundedTime.equals(time.withSecond(0))) {
                setAdjustment(roundedTime, PunchAdjustmentType.NONE);
            } else {
                setAdjustment(roundedTime, PunchAdjustmentType.INTERVAL_ROUND);
            }
        }

        // Sets the adjusted timestamp and adjustment type.
        private void setAdjustment(LocalDateTime time, PunchAdjustmentType type) {
            this.adjustedtimestamp = time;
            this.adjustmentType = type;
        }

        // Returns true if the given time occurs on a weekend.
        private boolean isWeekend(LocalDateTime time) {
            DayOfWeek day = time.getDayOfWeek();
            return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
        }

        // Returns true if the punch occurs during lunch.
        private boolean isDuringLunch(LocalDateTime punch, LocalDateTime lunchStart, LocalDateTime lunchStop) {
            return !punch.isBefore(lunchStart) && !punch.isAfter(lunchStop);
        }
    /* --- --- end of adjust() helper methods --- --- */
}
