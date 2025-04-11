package edu.jsu.mcis.cs310.tas_sp25;
import java.util.zip.CRC32; // Used for create method of Badge

/**
 * <p>The Badge class represents an employee's badge in the Time and Attendance
 * application. Each Badge is identified by an ID and a short
 * description.</p>
 *
 * @author Mr. Snellen
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
    
    // Constructor to crate new badge - nll
    public Badge(String description) {
        this.description = description;
        this.id =  generateId(description); // Calls generateId method - nll
   
    }
    // Method implemented to generate a unique id using the description and returns as String. - nll
    public String generateId(String description) {
     CRC32 crc = new CRC32(); // Creates CRC32 object
     crc.update(description.getBytes()); // uses the description to create a unique value
     long newID = crc.getValue(); // assigns the value to newID
     return String.format("%08X", newID); // returns as string thats formatted to requirements ( hexadecimal string,8 decimals, with 0 added if needed)
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
