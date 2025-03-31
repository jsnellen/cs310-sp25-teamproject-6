package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.Department;
import java.sql.*;

// Created Department DAO - Weston Wyatt [2/22/2025]
/**
 * <p>The DepartmentDAO class handles the retrieval of 
 * {@link Department} objects from the database.</p>
 *
 * <p>This includes a single "find" method, which looks up 
 * a department record by its numeric ID and returns a 
 * matching {@link Department} object if it exists in the 
 * "department" table. This class relies on a {@link DAOFactory} 
 * to provide and manage database connections.</p>
 *
 * @author Weston Wyatt
 */
public class DepartmentDAO {
    
    /**
     * The SQL query to select a department record by its ID.
     */
    private static final String QUERY_FIND = "SELECT * FROM department WHERE id = ?";
    /**
     * A reference to the {@link DAOFactory} that supplies database connectivity.
     */
    private final DAOFactory daoFactory;
    
    /**
     * Constructs a new DepartmentDAO, storing the provided 
     * {@link DAOFactory} for allowing database connectivity.
     *
     * @param daoFactory the DAOFactory used to create and manage DB connectivity
     */
    DepartmentDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // find method to retrieve a Department from the database using its id - WW
    /**
     * Finds and returns a {@link Department} object from the database, 
     * given a numeric department ID.
     *
     * <p>IF a matching record is found in the "department" table, THEN 
     * this method constructs a {@link Department} with the given 
     * data. If no matching record is found, it returns {@code null}.</p>
     *
     * @param departmentId the ID of the department to retrieve
     * @return a Department object if the record exists; 
     *         {@code null} OTHERWISE
     * @throws DAOException if encountering a database access error
     */
    public Department find(int departmentId) {
        
        Department department = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            // Get the connection to the database from the DAOFactory.
            Connection conn = daoFactory.getConnection();
            if (conn.isValid(0)) {
                
                ps = conn.prepareStatement(QUERY_FIND);
                ps.setInt(1, departmentId);
                
                boolean hasResults = ps.execute();
                if(hasResults) {
                    rs = ps.getResultSet();
                    if (rs.next()) {
                        
                        int id = rs.getInt("id");
                        String description = rs.getString("description");
                        int terminalid = rs.getInt("terminalid");
                        
                        department = new Department(id, description, terminalid);
                    }
                }
            }
            
        // copy and pasted catch Exception from BadgeDAO - WW
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
        
        return department;
    }
}
