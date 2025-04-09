package edu.jsu.mcis.cs310.tas_sp25;
import java.time.LocalTime;

// created DailySchedule class - WW
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

    // Constructor
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
    public int getId() {
        return id;
    }

    public LocalTime getShiftstart() {
        return shiftstart;
    }

    public LocalTime getShiftstop() {
        return shiftstop;
    }

    public int getRoundinterval() {
        return roundinterval;
    }

    public int getGraceperiod() {
        return graceperiod;
    }

    public int getDockpenalty() {
        return dockpenalty;
    }

    public LocalTime getLunchstart() {
        return lunchstart;
    }

    public LocalTime getLunchstop() {
        return lunchstop;
    }

    public int getLunchthreshold() {
        return lunchthreshold;
    }
}
