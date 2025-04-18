package edu.jsu.mcis.cs310.tas_sp25;

/**
 * <p>The {@code EmployeeType} enum represents the classification of an employee
 * within the Time and Attendance System. This classification distinguishes between
 * full-time employees and part-time employees.</p>
 *
 * <p>Each enum constant is associated with a human-readable description string that
 * may be used for display or reporting purposes.</p>
 * 
 * @author Nehemias Lucas
 */
public enum EmployeeType {

    /**
     * Represents a temporary or part-time employee.
     */
    PART_TIME("Temporary / Part-Time"),
    /**
     * Represents a full-time employee.
     */
    FULL_TIME("Full-Time");
    /**
     * Human-readable description of the employee type.
     */
    private final String description;

    /**
     * Constructs an {@code EmployeeType} enum constant with a custom description.
     *
     * @param d the description of the employee type
     */
    private EmployeeType(String d) {
        description = d;
    }
    
    /**
     * Returns a more detailed employee type description for use in reports or output.
     *
     * @return "Full-Time Employee" or "Temporary Employee"
     */
    public String toDescription() {
        if (this == FULL_TIME) {
            return "Full-Time Employee";
        } else {
            return "Temporary Employee";
        }
    }

    /**
     * Returns the general description associated with this employee type.
     *
     * @return a short label such as "Full-Time" or "Temporary / Part-Time"
     */
    @Override
    public String toString() {
        return description;
    }
}
