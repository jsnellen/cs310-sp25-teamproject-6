/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;
import java.sql.*;
import edu.jsu.mcis.cs310.tas_sp25.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.Instant;

/**
 *
 * @Creating DAO for punch, Nehemias Loarca Lucas 2-21-2025
 * completed find method, Weston Wyatt 2/23/2025
 */
public class PunchDAO {
    // private static final String QUERY_FIND = "SELECT * FROM badge WHERE terminalid = ?";
    // fixed query to use event and id 
    private static final String QUERY_FIND = "SELECT * FROM event WHERE id = ?";
    private final DAOFactory daoFactory;
    
    PunchDAO(DAOFactory daoFactory)
    { 
        this.daoFactory = daoFactory;
    }
    
    public Punch find(int id) {
        Punch punch = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_FIND);
                ps.setInt(1, id);  // Use the passed-in ID in the query
                
                boolean hasResults = ps.execute();
                
                if (hasResults) {
                    
                    rs = ps.getResultSet();
                    
                    while (rs.next()) {
                        
                        int punchId = rs.getInt("id");
                        int terminalid = rs.getInt("terminalid");
                        String badgeId = rs.getString("badgeid");

                        // Use BadgeDAO to retrieve the Badge
                        BadgeDAO badgeDAO = new BadgeDAO(daoFactory);
                        Badge badge = badgeDAO.find(badgeId);

                        // Retrieve timestamp from the database and convert to LocalDateTime using the system's local time zone
                        Timestamp ts = rs.getTimestamp("timestamp");
                        LocalDateTime originalTimestamp = ts.toLocalDateTime();

                        // Retrieve eventtypeid and convert to an enum
                        int eventTypeId = rs.getInt("eventtypeid");
                        EventType punchtype = EventType.values()[eventTypeId];

                        // Construct the Punch object
                        punch = new Punch(punchId, terminalid, badge, originalTimestamp, punchtype);
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

        return punch;
    
    }
    
           
    
}