package edu.jsu.mcis.cs310.tas_sp25;

public enum EmployeeType {

    PART_TIME("Temporary / Part-Time"),
    FULL_TIME("Full-Time");
    private final String description;

    private EmployeeType(String d) {
        description = d;
    }
    
    public String toDescription() {
        if (this == FULL_TIME) {
            return "Full-Time employee";
        } else {
            return "Temporary employee";
        }
    }

    @Override
    public String toString() {
        return description;
    }
}
