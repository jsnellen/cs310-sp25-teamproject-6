package edu.jsu.mcis.cs310.tas_sp25;
import java.time.LocalTime;

/**
 * Represents a daily schedule used for employee shift management
 * 
 * Created by Weston Wyatt (WW)
 */
public class DailySchedule {

    private final int id;
    private final LocalTime shiftstart;
    private final LocalTime shiftstop;
    private final int roundinterval;
    private final int graceperiod;
    private final int dockpenalty;
    private final LocalTime lunchstart;
    private final LocalTime lunchstop;
    private final int lunchthreshold;

    /**
     * Constructor that makes a new {@code DailySchedule} with the specified parameters
     *
     * @param id             the unique ID of the schedule
     * @param shiftstart     the start time of the shift
     * @param shiftstop      the stop time of the shift
     * @param roundinterval  the rounding interval in minutes for punch rounding
     * @param graceperiod    the grace period (in minutes) before late punches are penalized
     * @param dockpenalty    the penalty (in minutes) docked if outside grace period
     * @param lunchstart     the start time of the scheduled lunch period
     * @param lunchstop      the stop time of the scheduled lunch period
     * @param lunchthreshold the minimum minutes worked before lunch is deducted
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

    /**
     * Gets the unique ID of the schedule
     *
     * @return the schedule ID
     */
    public int getId() {
        return id;
    }
    
    /**
     * Gets the start time of the shift
     *
     * @return the shift start time
     */
    public LocalTime getShiftstart() {
        return shiftstart;
    }

    /**
     * Gets the stop time of the shift
     *
     * @return the shift stop time
     */
    public LocalTime getShiftstop() {
        return shiftstop;
    }

    /**
     * Gets the rounding interval for punch rounding
     *
     * @return the rounding interval in minutes
     */
    public int getRoundinterval() {
        return roundinterval;
    }

    /**
     * Gets the grace period
     *
     * @return the grace period in minutes
     */
    public int getGraceperiod() {
        return graceperiod;
    }

    /**
     * Gets the dock penalty in minutes
     *
     * @return the number of minutes docked if late beyond the grace period
     */
    public int getDockpenalty() {
        return dockpenalty;
    }

    /**
     * Gets the lunch start time
     *
     * @return the start time of lunch
     */
    public LocalTime getLunchstart() {
        return lunchstart;
    }

    /**
     * Gets the lunch stop time.
     *
     * @return the stop time of lunch
     */
    public LocalTime getLunchstop() {
        return lunchstop;
    }

    /**
     * Gets the minimum number of minutes that must be worked before lunch is deducted.
     *
     * @return the lunch threshold in minutes
     */
    public int getLunchthreshold() {
        return lunchthreshold;
    }
}
