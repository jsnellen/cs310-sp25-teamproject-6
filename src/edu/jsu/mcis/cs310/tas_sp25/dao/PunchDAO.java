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
 * The PunchDAO class provides methods for creating, retrieving,
 * and listing {@link Punch} records from the database.
 *
 * <p>Supports punch creation, retrieval by ID, single-day punch list,
 * and multi-day punch history. This class uses other DAOs like
 * BadgeDAO and DepartmentDAO for cross-table dependencies.</p>
 *
 * @author Nehemias Lucas
 * @author Weston Wyatt (completed find method)
 * @author Parinita Sedai (list by date)
 */
public class PunchDAO {    
    private final DAOFactory daoFactory;
    
    private static final String QUERY_FIND      = "SELECT * FROM event WHERE id = ?";
    private static final String QUERY_LIST      = "SELECT * FROM event WHERE badgeid = ? AND DATE(timestamp) = ? ORDER BY timestamp";
    private static final String QUERY_CREATE    = "SELECT departmentid FROM employee WHERE badgeid = ?";
    private static final String INSERT_PUNCH    = "INSERT INTO event (id, terminalid, badgeid, timestamp, eventtypeid) VALUES (?,?,?,?,?)"; 
     
     /**
     * Constructs a PunchDAO using a reference to the DAOFactory.
     *
     * @param daoFactory the DAOFactory used for accessing shared DB connections
     */
    PunchDAO(DAOFactory daoFactory)
    { 
        this.daoFactory = daoFactory;
    }
    
       /**
     * Retrieves a {@link Punch} from the database by its numeric ID.
     *
     * @param id the unique punch ID
     * @return a Punch object if found, otherwise {@code null}
     * @throws DAOException if a database error occurs
     */
    
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
    
    /**
     * Creates a new {@link Punch} record in the database if the terminal is authorized.
     * 
     * <p>Checks whether the terminal ID matches the employee's department. If authorized,
     * inserts the punch and returns the new ID.</p>
     *
     * @param punch the Punch object to insert
     * @return the newly generated punch ID, or 0 if not authorized
     * @throws DAOException if a database error occurs
     */
    public int create(Punch punch){
        int newId = 0;
    PreparedStatement ps = null;
    ResultSet keys = null;

        try {
            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                Badge badge = punch.getBadge();
                String badgeId = badge.getId();

                PreparedStatement empPs = conn.prepareStatement(QUERY_CREATE);
                empPs.setString(1, badgeId);

                ResultSet empRs = empPs.executeQuery();

                int departmentId = -1;

                if (empRs.next()) {
                    departmentId = empRs.getInt("departmentid");
                }

                empRs.close();
                empPs.close();

                DepartmentDAO departmentDAO = new DepartmentDAO(daoFactory);
                Department department = departmentDAO.find(departmentId);

                int departmentTerminalId = department.getTerminalid();
                int punchTerminalId = punch.getTerminalid();

                boolean isAuthorized = (punchTerminalId == departmentTerminalId) || (punchTerminalId == 0);

                if (isAuthorized) {

                    ps = conn.prepareStatement(INSERT_PUNCH, Statement.RETURN_GENERATED_KEYS);

                    ps.setInt(1,0);
                    ps.setInt(2, punchTerminalId);
                    ps.setString(3, badgeId);
                    // this is off by 1 second randomly, sometimes it is correct sometimes its not
                    // Using either Timestamp.valueOf(LocalDateTime.now() or punch.getOriginaltimestamp() the issue is still there
                    ps.setTimestamp(4, Timestamp.valueOf(punch.getOriginaltimestamp())); // LocalDateTime.now() punch.getOriginaltimestamp()
                    ps.setInt(5, punch.getPunchtype().ordinal());


                    ps.executeUpdate();

                    keys = ps.getGeneratedKeys();
                    if (keys.next()) {
                        newId = keys.getInt(1);
                    }

                } else {
                    System.err.println("Authorization failed: Terminal ID does not match department terminal.");
                    newId = 0; // Unauthorized punch
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        } finally {
            if (keys != null) try { keys.close(); } catch (SQLException e) { throw new DAOException(e.getMessage()); }
            if (ps != null) try { ps.close(); } catch (SQLException e) { throw new DAOException(e.getMessage()); }
        }
        
        return newId;
    }
    
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
    
    /**
     * Retrieves a list of punches for a specific badge across a range of dates.
     *
     * <p>Calls the single-day list method internally and compiles the results.
     * Ensures no duplicate consecutive entries appear in the final list.</p>
     *
     * @param badge the Badge object to retrieve punches for
     * @param begin the start date of the range
     * @param end the end date of the range
     * @return a combined ArrayList of Punch objects across the date range
     */
    
    public ArrayList <Punch> list (Badge badge, LocalDate begin, LocalDate end) { // changed "entries" to "list" to match with test 
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
