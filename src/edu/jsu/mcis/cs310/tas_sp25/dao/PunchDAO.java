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
import java.time.LocalDate;
import java.util.ArrayList;
import edu.jsu.mcis.cs310.tas_sp25.EventType;
import edu.jsu.mcis.cs310.tas_sp25.Badge;



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
    private static final String QUERY_LIST = "SELECT * FROM event WHERE badgeid = ? AND DATE(timestamp) = ? ORDER BY timestamp";
    
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
    
    /*
    public int create(Punch punch){
        int newId = 0;
        
        try {
            
        } catch (SQLException e) {
            // In case of error, return default value of 0
            return 0;
        } finally {
            if (keys != null) {
                try {
                    keys.close();
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
        
        return newId;
    }
    */
    
    /**
     * Retrieves a list of punches for a specific badge on a particular date.
     * Parinita Sedai 2/25/2025
     * @param badge The Badge object for which to retrieve the punch records.
     * @param date The specific date to query punches for.
     * @return An ArrayList of Punch objects for the given badge and date.
     */
    public ArrayList <Punch> list (Badge badge, LocalDate date) {
        ArrayList <Punch> punches = new ArrayList<>();
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            Connection conn = daoFactory.getConnection();
            if (conn.isValid(0)) {
                ps = conn.prepareStatement(QUERY_LIST);
                ps.setString(1, badge.getId());
                ps.setDate(2,Date.valueOf(date));
                
                boolean hasResults = ps.execute();
                
                if (hasResults) {
                    rs = ps.getResultSet();
                    
                    while (rs.next()) {
                        int terminalId = rs.getInt("terminalid");
                        int id = rs.getInt("id");
                        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
                        Badge punchBadge = badgeDAO.find(rs.getString("badgeid"));
                        LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
                        EventType eventType = EventType.values() [rs.getInt("eventtypeid")];
                        
                        // Badge variable declaration
                        Punch punch = new Punch (id, terminalId, punchBadge, timestamp, eventType);
                        punches.add (punch);
                        
                    }
                }
                // Check for the last punch of the day and add an extra punch (if needed)
                if(punches.size() > 0) {
                    Punch lastPunch = punches.get(punches.size() - 1);

                    if (lastPunch.getPunchtype() == EventType.CLOCK_IN){
                        LocalDate nextFirstPunch = lastPunch.getOriginaltimestamp().toLocalDate().plusDays(1);

                        ps = conn.prepareStatement(QUERY_LIST);

                        ps.setString(1, badge.getId());
                        ps.setDate(2, Date.valueOf(nextFirstPunch));
                        hasResults = ps.execute();

                        if (hasResults){
                            rs = ps.getResultSet();
                            rs.next();

                            EventType nextPunchtype = EventType.values()[rs.getInt("eventtypeid")];

                            if (nextPunchtype == EventType.CLOCK_OUT || nextPunchtype == EventType.TIME_OUT){
                                Punch punch = find(rs.getInt("id"));

                                punches.add(punch);
                            }
                        }
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
                    throw new DAOException (e.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DAOException (e.getMessage());
                }
            }
        }
        return punches;
    }    
    
    // Adding List method for punches from a range of Dates
    
    public ArrayList <Punch> entries (Badge badge, LocalDate begin, LocalDate end) {
        ArrayList <Punch> resultEntries = new ArrayList ();
        LocalDate date = begin;
        
        while (date.isBefore(end) || date.equals(end)) {
            
            ArrayList<Punch> dailyEntries = new ArrayList<>();
   
            try {
                dailyEntries = list (badge, date);
            } catch (IndexOutOfBoundsException e) {
            }
            
            if (!dailyEntries.isEmpty() && !resultEntries.isEmpty()) {
                if (resultEntries.get(resultEntries.size() - 1).toString().equals(dailyEntries.get(0).toString())) {
                    resultEntries.remove(resultEntries.size() - 1);
                }
                
            }
            resultEntries.addAll(dailyEntries);
            date = date.plusDays(1);
        }
        return resultEntries;
    }
}