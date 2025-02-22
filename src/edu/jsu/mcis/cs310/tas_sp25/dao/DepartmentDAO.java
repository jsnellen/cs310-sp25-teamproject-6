package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.Department;
import java.sql.*;

// Created Department DAO - Weston Wyatt [2/22/2025]

public class DepartmentDAO {

    private static final String QUERY_FIND = "SELECT * FROM department WHERE id = ?";
    private final DAOFactory daoFactory;
    
    DepartmentDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // find method to retrieve a Department from the database using its id - WW
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
