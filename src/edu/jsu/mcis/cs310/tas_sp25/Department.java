package edu.jsu.mcis.cs310.tas_sp25;

// Created Department Class - Weston Wyatt [2/22/2025]
/**
 * <p>The Department class models a department within the Time and 
 * Attendance System, aligning each department to a unique ID, 
 * a description (label) and a numeric terminal ID representing the 
 * clock terminal that employees in this department can use.</p>
 *
 * <p>This information is stored in the {@code department} 
 * table of the database and retrieved with a corresponding 
 * {@code DepartmentDAO} class.</p>
 *
 * @author Weston Wyatt
 */

public class Department {
    
    /**
     * The numeric identifier for this department, corresponding to a 
     * record in the database.
     */
    private final int id;
    /**
     * A descriptive label for this department.
     */
    private final String description;
    /**
     * The numeric ID of the terminal associated with this department, 
     * indicating which clock terminal employees assigned to this 
     * department should use.
     */
    private final int terminalid;
    
    /**
     * Constructs a new Department with the given ID, description 
     * and terminal ID.
     *
     * @param id          the unique numeric department ID
     * @param description a short descriptive label for the department
     * @param terminalid  the numeric ID of the permitted clock terminal
     */
    public Department(int id, String description, int terminalid) {
        this.id = id;
        this.description = description;
        this.terminalid = terminalid;
    }
    
    // Getters
    /**
     * Returns the department ID.
     *
     * @return the department ID
     */
    public int getId() {
        return id;
    }
    /**
     * Returns the description for this department.
     *
     * @return the department's description
     */
    public String getDescription() {
        return description;
    }
    /**
     * Returns the numeric ID of the permitted clock terminal 
     * for employees in this department.
     *
     * @return the department's terminal ID
     */
    public int getTerminalid() {
        return terminalid;
    }
    /**
     * Returns a string representation of this Department, 
     * displaying its ID, description, and terminal ID.
     *
     * @return a formatted string 
     */
    @Override
    public String toString() {
        return String.format("#%d (%s), Terminal ID: %d", id, description, terminalid);
    }
}
