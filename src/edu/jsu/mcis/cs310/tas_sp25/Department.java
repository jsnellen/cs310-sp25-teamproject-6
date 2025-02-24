package edu.jsu.mcis.cs310.tas_sp25;

// Created Department Class - Weston Wyatt [2/22/2025]

public class Department {

    private final int id;
    private final String description;
    private final int terminalid;
    
    public Department(int id, String description, int terminalid) {
        this.id = id;
        this.description = description;
        this.terminalid = terminalid;
    }
    
    // Getters
    public int getId() {
        return id;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getTerminalid() {
        return terminalid;
    }
    
    @Override
    public String toString() {
        return String.format("#%d (%s), Terminal ID: %d", id, description, terminalid);
    }
}
