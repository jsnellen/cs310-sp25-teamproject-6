package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.*;
import java.sql.*;
import java.util.zip.CRC32;
/**
 * <p>The BadgeDAO class gives data access methods for getting
 * {@link Badge} objects from the database.</p>
 *
 * <p>Instances of this class should be obtained through
 * {@link DAOFactory#getBadgeDAO() DAOFactory.getBadgeDAO()}.</p>
 *
 * @author Mr. Snellen
 */
public class BadgeDAO {
    
    /**
     * SQL query to select a badge relative to its ID.
     */
    private static final String QUERY_FIND = "SELECT * FROM badge WHERE id = ?";
     // SQL queries to create new badge into database 
    private static final String INSERT_BADGE   = "INSERT INTO badge (id, description) VALUES (?,?)"; 


    /**
     * Reference to {@link DAOFactory} FOR database connectivity.
     */
    private final DAOFactory daoFactory;

    /**
     * Constructs BadgeDAO, stores a reference to the
     * {@link DAOFactory} for getting database connections.
     *
     * @param daoFactory the DAOFactory used for obtaining a valid
     *                   {@link Connection} to the database
     */
    BadgeDAO(DAOFactory daoFactory) {

        this.daoFactory = daoFactory;

    }

    /**
     * Retrieves a {@link Badge} object from the database for
     * the corresponding badge ID.
     *
     * <p>IF ID is found in the "badge" table, THEN this method
     * constructs/returns a {@link Badge} with the
     * retrieved data. ELSE, it returns {@code null}.</p>
     *
     * @param id the badge ID String to locate the badge record
     * @return a {@link Badge} object for badge data or
     *         {@code null} IF no matching record is found
     * @throws DAOException if there is a database access error
     */
    public Badge find(String id) {

        Badge badge = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_FIND);
                ps.setString(1, id);

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    while (rs.next()) {

                        String description = rs.getString("description");
                        badge = new Badge(id, description);

                    }

                }

            }

        } catch (SQLException e) {

            throw new DAOException(e.getMessage());

        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }

        }

        return badge;

    }
    
    // Create method for BadgeDAO - NLL
    
     public boolean create(Badge badge){ //retunr value is boolean as required
        boolean result = false; // initialize result
        PreparedStatement ps = null;
        ResultSet keys = null;


        try {
            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {


                PreparedStatement statement = conn.prepareStatement(INSERT_BADGE);

                statement.setString(1, badge.getId()); 
                statement.setString(2, badge.getDescription());

                ResultSet rs = statement.executeQuery();
                
                int numofChanges = statement.executeUpdate(); //returns number of rows in the database that was changed
                // Maybe use badge find method to like as in punch? idk how tho (no comments in punch create method)
                if (numofChanges == 1) { // if num of chanages is 1, result = true (create method was successful) else reuslt = false (fail)
                    result = true;
                    System.out.println("Successful in creating new badge");

                }
                else {
                    result = false;
                    System.out.println("Failed to create new badge");
                }
                
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        } finally {
            if (keys != null) try { keys.close(); } catch (SQLException e) { throw new DAOException(e.getMessage()); }
            if (ps != null) try { ps.close(); } catch (SQLException e) { throw new DAOException(e.getMessage()); }
        }
        
        return result;
    }

}
