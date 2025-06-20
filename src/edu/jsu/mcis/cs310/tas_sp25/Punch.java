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
 * The {@code Punch} class represents a single time punch event made by an employee in
 * the Time and Attendance System. Each punch includes a terminal ID, a badge,
 * a punch type (e.g., CLOCK IN, CLOCK OUT), and two timestamps:
 * the original timestamp and an adjusted timestamp.
 * <p>
 * 
 * Punches can be created either as new events or by retrieving existing events
 * from the database. The class supports the adjustment of timestamps based on
 * shift rules provided in the {@link Shift} class. Adjustments are tracked via
 * {@link PunchAdjustmentType}.
 * </p>
 *
 * @author Nehemias Loarca Lucas
 * @author Weston Wyatt
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
    
    /** Formatter for printing timestamps in standard format. */
    public static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("EEE MM/dd/yyyy HH:mm:ss");

    // Constructor
    /**
     * Constructs a new Punch object for a new punch entry.
     * The punch ID is null and the timestamp is initialized to the current system time.
     *
     * @param terminalid  the terminal ID where the punch was made
     * @param badge       the {@link Badge} associated with the employee
     * @param punchtype   the type of punch (e.g., CLOCK_IN, CLOCK_OUT)
     */
    public Punch(int terminalid, Badge badge, EventType punchtype){ // for new punch objects
        this.terminalid = terminalid;
        this.badge = badge;
        this.punchtype = punchtype;
        this.originaltimestamp = LocalDateTime.now().withNano(0);
        this.id = null;
    }
    /**
     * Constructs a Punch object for an existing punch retrieved from the database.
     *
     * @param id                the punch ID
     * @param terminalid        the terminal ID
     * @param badge             the employee's badge
     * @param originaltimestamp the original timestamp of the punch
     * @param punchtype         the type of punch
     */
    public Punch(int id, int terminalid, Badge badge,LocalDateTime originaltimestamp, EventType punchtype){ // for existing punch objects
        this.id = id;  // Fixed: assign the provided id instead of null - WW
        this.terminalid = terminalid;
        this.badge = badge;
        this.originaltimestamp = originaltimestamp;
        this.punchtype = punchtype;
        
    }
    
    
    // Getters
    /** @return the terminal ID where the punch occurred */
    public int getTerminalid() {
        return terminalid;
    }
    /** @return the badge used for the punch */
    public Badge getBadge() {
        return badge;
    }
    /** @return the punch type (CLOCK IN, CLOCK OUT, TIME OUT) */
    public EventType getPunchtype() {
        return punchtype;
    }
    /** @return the original timestamp of the punch */
    public LocalDateTime getOriginaltimestamp() {
        return originaltimestamp;
    }
    /** @return the adjusted timestamp of the punch */
    public LocalDateTime getAdjustedtimestamp() {
        return adjustedtimestamp;
    }
    /** @return the punch adjustment type used (if any) */
    public PunchAdjustmentType getAdjustmentType(){
        return adjustmentType;
    }
    /** @return the punch ID */
    public int id(){
        return id;
    }
    
    // created this printOriginal method -ww
    /**
     * Returns a formatted string representing the original timestamp of the punch.
     *
     * @return formatted string of original timestamp
     */
    public String printOriginal() {
        // Convert the entire string to uppercase
        String formattedTimestamp = originaltimestamp.format(TIMESTAMP_FORMAT).toUpperCase();

        // Get the EventType string representation
        String eventDescription = punchtype.toString();

        // StringBuilder for constructing the string
        StringBuilder sb = new StringBuilder();
        sb.append("#").append(badge.getId());
        sb.append(" ").append(eventDescription);
        sb.append(": ").append(formattedTimestamp);

        return sb.toString();
    }
    
    /**
     * Returns a formatted string representing the adjusted timestamp and the type of adjustment applied.
     *
     * @return formatted string of adjusted timestamp and adjustment type
     */
    public String printAdjusted() {   
        // Convert the entire string to uppercase
        String formattedTimestamp = adjustedtimestamp.format(TIMESTAMP_FORMAT).toUpperCase();
        
        // Build the string
        StringBuilder sb = new StringBuilder();
        sb.append("#").append(badge.getId());
        sb.append(" ").append(punchtype);
        sb.append(": ").append(formattedTimestamp);
        sb.append(" (").append(adjustmentType.toString()).append(")");
        
        return sb.toString();
    }

    //toString
    /**
     * Returns a simple string with basic punch details (used for debugging).
     *
     * @return formatted string with ID, terminal, badge, and punch type
     */
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append('#').append(id).append(' ');
        sb.append('#').append(terminalid).append(' ');
        sb.append("Badge:").append(badge).append(' ');
        sb.append("Punch Type:").append(punchtype).append(' ');
        

        return sb.toString();

    }
    
    /**
     * Adjusts the original punch timestamp according to the shift parameters.
     * Applies rounding, grace period, dock penalties, and lunch time adjustments.
     * Sets the adjusted timestamp and adjustment type based on rules defined in the {@link Shift}.
     *
     * @param s the shift rule set to apply for adjustment
     */
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
            //System.out.println("Punch ID: " + id + " (" + punchtype + ") Original: " + original + " Adjusted: " + adjustedtimestamp + " Type: " + adjustmentType);
            return;
        }

        // Adjustments for CLOCK_IN punches:
        if (punchtype == EventType.CLOCK_IN) {
            // Early clock-in
            LocalDateTime earliest = shiftStartTime.minusMinutes(interval);
            if ((original.isEqual(earliest) || original.isAfter(earliest))
                    && original.isBefore(shiftStartTime)) {
                setAdjustment(shiftStartTime, PunchAdjustmentType.SHIFT_START);
                //System.out.println("Punch ID: " + id + " (" + punchtype + ") Original: " + original + " Adjusted: " + adjustedtimestamp + " Type: " + adjustmentType);
                return;
            }
            // Exactly at shift start.
            if (original.equals(shiftStartTime)) {
                setAdjustment(shiftStartTime, PunchAdjustmentType.SHIFT_START);
                //System.out.println("Punch ID: " + id + " (" + punchtype + ") Original: " + original + " Adjusted: " + adjustedtimestamp + " Type: " + adjustmentType);
                return;
            }
            // If within the grace period after shift start, adjust to shift start.
            if (original.isAfter(shiftStartTime) && original.isBefore(shiftStartTime.plusMinutes(grace))) {
                setAdjustment(shiftStartTime, PunchAdjustmentType.SHIFT_START);
                //System.out.println("Punch ID: " + id + " (" + punchtype + ") Original: " + original + " Adjusted: " + adjustedtimestamp + " Type: " + adjustmentType);
                return;
            }
            // Lunch break: if the punch occurs during lunch, adjust to lunch stop.
            if (isDuringLunch(original, lunchStartTime, lunchStopTime)) {
                setAdjustment(lunchStopTime, PunchAdjustmentType.LUNCH_STOP);
                //System.out.println("Punch ID: " + id + " (" + punchtype + ") Original: " + original + " Adjusted: " + adjustedtimestamp + " Type: " + adjustmentType);
                return;
            }
            // Dock penalty: if the punch is later than the grace period but before shiftStart + dock.
            if (original.isAfter(shiftStartTime.plusMinutes(grace)) && original.isBefore(shiftStartTime.plusMinutes(dock))) {
                setAdjustment(shiftStartTime.plusMinutes(dock), PunchAdjustmentType.SHIFT_DOCK);
                //System.out.println("Punch ID: " + id + " (" + punchtype + ") Original: " + original + " Adjusted: " + adjustedtimestamp + " Type: " + adjustmentType);
                return;
            }
        }

        // Adjustments for CLOCK_OUT punches:
        if (punchtype == EventType.CLOCK_OUT) {
            // First, if the punch (when seconds are zeroed) exactly equals shift stop
            if (original.withSecond(0).equals(shiftStopTime)) {
                setAdjustment(shiftStopTime, PunchAdjustmentType.SHIFT_STOP);
                //System.out.println("Punch ID: " + id + " (" + punchtype + ") Original: " + original + " Adjusted: " + adjustedtimestamp + " Type: " + adjustmentType);
                return;
            }
            // Late clock-out: if after shift stop and within the interval, adjust to shift stop
            if (original.isAfter(shiftStopTime)) {
                long diff = java.time.Duration.between(shiftStopTime, original).toMinutes();
                if (diff <= interval) {
                    setAdjustment(shiftStopTime, PunchAdjustmentType.SHIFT_STOP);
                    //System.out.println("Punch ID: " + id + " (" + punchtype + ") Original: " + original + " Adjusted: " + adjustedtimestamp + " Type: " + adjustmentType);
                    return;
                }
            }
            // Lunch break: if the punch occurs during lunch, adjust to lunch start
            if (isDuringLunch(original, lunchStartTime, lunchStopTime)) {
                setAdjustment(lunchStartTime, PunchAdjustmentType.LUNCH_START);
                //System.out.println("Punch ID: " + id + " (" + punchtype + ") Original: " + original + " Adjusted: " + adjustedtimestamp + " Type: " + adjustmentType);
                return;
            }
            // For punches before shift stop, check:
            if (original.isBefore(shiftStopTime)) {
                // If the punch is very near shift stop (difference less than or equal to half the interval)
                // then round upward to shift stop
                long diff = java.time.Duration.between(original, shiftStopTime).toMinutes();
                if (diff <= (interval / 2.0)) {
                    setAdjustment(shiftStopTime, PunchAdjustmentType.SHIFT_STOP);
                    //System.out.println("Punch ID: " + id + " (" + punchtype + ") Original: " + original + " Adjusted: " + adjustedtimestamp + " Type: " + adjustmentType);
                    return;
                }
                // Otherwise, if the punch is within the grace period before shift stop, adjust to shift stop
                if (!original.isBefore(shiftStopTime.minusMinutes(grace))) {
                    setAdjustment(shiftStopTime, PunchAdjustmentType.SHIFT_STOP);
                    //System.out.println("Punch ID: " + id + " (" + punchtype + ") Original: " + original + " Adjusted: " + adjustedtimestamp + " Type: " + adjustmentType);
                    return;
                }
                // Or if within the dock penalty window, adjust to shift stop - dock
                if (original.isBefore(shiftStopTime.minusMinutes(grace)) && !original.isBefore(shiftStopTime.minusMinutes(dock))) {
                    setAdjustment(shiftStopTime.minusMinutes(dock), PunchAdjustmentType.SHIFT_DOCK);
                    //System.out.println("Punch ID: " + id + " (" + punchtype + ") Original: " + original + " Adjusted: " + adjustedtimestamp + " Type: " + adjustmentType);
                    return;
                }
            }
        }
        
        // if no condition is met, use IntervalRound
        applyIntervalRound(original, interval);
        //System.out.println("Punch ID: " + id + " (" + punchtype + ") Original: " + original + " Adjusted: " + adjustedtimestamp + " Type: " + adjustmentType);
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
            return punch.isAfter(lunchStart) && punch.isBefore(lunchStop);
        }
    /* --- --- end of adjust() helper methods --- --- */
}
