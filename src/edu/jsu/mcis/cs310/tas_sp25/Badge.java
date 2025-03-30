package edu.jsu.mcis.cs310.tas_sp25;

/**
 * <p>The Badge class represents an employee's badge in the Time and Attendance
 * application. Each Badge is identified by an ID and a short
 * description.</p>
 *
 * @author Michael Frix
 */

public class Badge {

    /**
     * Badge ID and description.
     */
    private final String id, description;
    
    /**
     * Constructs a new Badge with parameters ID and description.
     *
     * @param id the badge ID
     * @param description a label for the badge
     */
    public Badge(String id, String description) {
        this.id = id;
        this.description = description;
    }
    
    /**
     * Returns the badge ID.
     *
     * @return the ID as a String
     */
    public String getId() {
        return id;
    }
    
    /**
     * Returns the badge description.
     *
     * @return the description as a String
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Returns a string representation of this Badge.
     *
     * @return a formatted string with both badge ID and description
     */
    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        s.append('#').append(id).append(' ');
        s.append('(').append(description).append(')');

        return s.toString();

    }

}
