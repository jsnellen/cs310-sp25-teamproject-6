package edu.jsu.mcis.cs310.tas_sp25;
import java.time.LocalTime;

// created DailySchedule class - WW
/**
 * <p>
 * The {@code DailySchedule} class represents a complete set of shift rules for
 * a single day in the Time and Attendance System. These rules include the shift
 * start/stop times, rounding interval, grace period, dock penalty, lunch break
 * times, and the lunch deduction threshold.
 * </p>
 *
 * <p>
 * This class is part of the refactored Version 2 shift management system, where
 * each day in a shift may have its own set of rules. These schedules can serve
 * as default rules or be overridden via temporary or recurring adjustments per
 * employee or organization-wide.
 * </p>
 *
 * @author Weston Wyatt
 */
public class DailySchedule {
    
    /**
     * The unique ID of this daily schedule.
     */
    private final int id;
    /**
     * The time the scheduled shift starts.
     */
    private final LocalTime shiftstart;
    /**
     * The time the scheduled shift stops.
     */
    private final LocalTime shiftstop;
    /**
     * The number of minutes used for rounding punch times.
     */
    private final int roundinterval;
    /**
     * The number of minutes allowed for grace before penalties apply.
     */
    private final int graceperiod;
    /**
     * The number of minutes docked if grace period is exceeded.
     */
    private final int dockpenalty;
    /**
     * The start time of the scheduled lunch break.
     */
    private final LocalTime lunchstart;
    /**
     * The end time of the scheduled lunch break.
     */
    private final LocalTime lunchstop;
    /**
     * The number of minutes required to qualify for an automatic lunch deduction.
     */
    private final int lunchthreshold;

    // Constructor
    /**
     * Constructs a new {@code DailySchedule} object with all required shift parameters.
     *
     * @param id              the schedule ID
     * @param shiftstart      the shift start time
     * @param shiftstop       the shift end time
     * @param roundinterval   the rounding interval in minutes
     * @param graceperiod     the grace period in minutes
     * @param dockpenalty     the dock penalty in minutes
     * @param lunchstart      the lunch break start time
     * @param lunchstop       the lunch break end time
     * @param lunchthreshold  the minimum time required for lunch deduction
     */
    public DailySchedule(
        int id, 
        LocalTime shiftstart, 
        LocalTime shiftstop, 
        int roundinterval,
        int graceperiod, 
        int dockpenalty, 
        LocalTime lunchstart,
        LocalTime lunchstop, 
        int lunchthreshold
    ) {
        this.id = id;
        this.shiftstart = shiftstart;
        this.shiftstop = shiftstop;
        this.roundinterval = roundinterval;
        this.graceperiod = graceperiod;
        this.dockpenalty = dockpenalty;
        this.lunchstart = lunchstart;
        this.lunchstop = lunchstop;
        this.lunchthreshold = lunchthreshold;
    }

    // Getters
    /**
     * Returns the ID of this daily schedule.
     *
     * @return the schedule ID
     */
    public int getId() {
        return id;
    }
    /**
     * Returns the shift start time.
     *
     * @return the shift start as {@code LocalTime}
     */
    public LocalTime getShiftstart() {
        return shiftstart;
    }
    /**
     * Returns the shift stop time.
     *
     * @return the shift end as {@code LocalTime}
     */
    public LocalTime getShiftstop() {
        return shiftstop;
    }
    /**
     * Returns the rounding interval in minutes.
     *
     * @return the round interval
     */
    public int getRoundinterval() {
        return roundinterval;
    }
    /**
     * Returns the grace period in minutes.
     *
     * @return the grace period
     */
    public int getGraceperiod() {
        return graceperiod;
    }
    /**
     * Returns the dock penalty in minutes.
     *
     * @return the dock penalty
     */
    public int getDockpenalty() {
        return dockpenalty;
    }
    /**
     * Returns the lunch break start time.
     *
     * @return the lunch start as {@code LocalTime}
     */
    public LocalTime getLunchstart() {
        return lunchstart;
    }
    /**
     * Returns the lunch break stop time.
     *
     * @return the lunch stop as {@code LocalTime}
     */
    public LocalTime getLunchstop() {
        return lunchstop;
    }
    /**
     * Returns the threshold for automatic lunch deduction in minutes.
     *
     * @return the lunch threshold
     */
    public int getLunchthreshold() {
        return lunchthreshold;
    }
}
