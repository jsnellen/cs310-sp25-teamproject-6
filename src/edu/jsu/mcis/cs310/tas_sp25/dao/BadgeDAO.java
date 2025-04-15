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
     // SQL query to create new badge into database 
    private static final String CREATE_BADGE   = "INSERT INTO badge (id, description) VALUES (?,?)"; 
    // SQL  query to delete corressponding records from the badge table
    private static final String DELETE_BADGE   = "DELETE FROM badge WHERE id = ?"; 



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
        PreparedStatement statement = null;
        ResultSet rs = null;


        try {
            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {


                statement = conn.prepareStatement(CREATE_BADGE); // Uses create query (L22)

                statement.setString(1, badge.getId()); 
                statement.setString(2, badge.getDescription());
                 // commenting this out allows for test to pass, executeQuery() is not need for create, delete, and update methods
                //rs = statement.executeQuery();
                
                int numofChanges = statement.executeUpdate(); //returns number of rows in the database that was changed
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
            if (rs != null) try { rs.close(); } catch (SQLException e) { throw new DAOException(e.getMessage()); }
            if (statement != null) try { statement.close(); } catch (SQLException e) { throw new DAOException(e.getMessage()); }
        }
        
        return result;
    }

     // Delete method for BadgeDAO - NLL
    
     public boolean delete (String id){ //return value is boolean as required
        boolean result = false; // initialize result
        PreparedStatement statement = null; // initialize prepared statement


        try {
            Connection conn = daoFactory.getConnection();
         

            if (conn.isValid(0)) {


                statement = conn.prepareStatement(DELETE_BADGE); // uses delete query (L24)

                statement.setString(1, id); 
                                
                int numofChanges = statement.executeUpdate(); // Executes query any return the number of rows affected
                if (numofChanges == 1) { // returns true if update it successful otherwise, returns false
                    result = true;
                    System.out.println("Successful in deleting badge");

                }
                else {
                    result = false;
                    System.out.println("Failed to delete badge or badge does not exist to delete.");
                }
                
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) { throw new DAOException(e.getMessage()); }
        }
        
        return result;
    }
}
