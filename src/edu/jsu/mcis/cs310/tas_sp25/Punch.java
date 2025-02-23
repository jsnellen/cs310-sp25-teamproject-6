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

/**
 *
 * @Creating Punch class Nehemias Loarca Lucas 2-21-2025
 */
public class Punch {
    // Intialize
    private final int terminalid;
    private final Badge badge;
    private final EventType punchtype;
    private final PunchAdjustmentType adjustmentType = null;
    private final Integer id;
    
    //changed orginaltimestamp to originaltimestamp - WW
    private LocalDateTime originaltimestamp = null;
    
    // Constructor
    public Punch(int terminalid, Badge badge, EventType punchtype){ // for new punch objects
        this.terminalid = terminalid;
        this.badge = badge;
        this.punchtype = punchtype;
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

        String formattedTimestamp = originaltimestamp.format(formatter);

        // Convert the three-letter day abbreviation to uppercase
        String dayAbbrev = formattedTimestamp.substring(0, 3).toUpperCase();
        String rest = formattedTimestamp.substring(3);
        formattedTimestamp = dayAbbrev + rest;

        // Map the EventType to a string
        String eventDescription;
        switch (punchtype) {
            case CLOCK_IN:
                eventDescription = "CLOCK IN";
                break;
            case CLOCK_OUT:
                eventDescription = "CLOCK OUT";
                break;
            case TIME_OUT:
                eventDescription = "TIME OUT";
                break;
            default:
                eventDescription = "UNKNOWN";
                break;
        }

        // Construct and return the formatted string
        return String.format("#%s %s: %s", badge.getId(), eventDescription, formattedTimestamp);
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
    
    
    
}
