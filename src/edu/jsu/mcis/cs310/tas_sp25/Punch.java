/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    private LocalDateTime orginaltimestamp = null;
    
    // Constructor
    public Punch(int terminalid, Badge badge, EventType punchtype){ // for new punch objects
        this.terminalid = terminalid;
        this.badge = badge;
        this.punchtype = punchtype;
        this.id = null;
    }
    public Punch(int id, int terminalid, Badge badge,LocalDateTime orginaltimestamp, EventType punchtype){ // for existing punch objects
        this.id = null;
        this.terminalid = terminalid;
        this.badge = badge;
        this.orginaltimestamp = orginaltimestamp;
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

    public LocalDateTime getOrginaltimestamp() {
        return orginaltimestamp;
    }
    public int id(){
        return id;
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
