/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import edu.jsu.mcis.cs310.tas_sp25.Shift;
import java.util.HashMap;
import edu.jsu.mcis.cs310.tas_sp25.Badge;
import edu.jsu.mcis.cs310.tas_sp25.DailySchedule;
import edu.jsu.mcis.cs310.tas_sp25.dao.DAOException;


/**
 * <p>The ShiftDAO class manages the retrieval of {@link Shift} objects 
 * from the database, using its ID or an associated {@link Badge} 
 * (which references a shift ID in the employee record).</p>
 *
 * <p>Two main "find" methods are given: one locates a shift rule set 
 * by a numeric shift ID and the other uses an employee's Badge object 
 * to return the correct shift.</p>
 *
 * @author Michael Frix
 */
public class ShiftDAO {
    
    /**
     * The SQL query to find a shift record by its ID.
     */
    private static final String QUERY_FIND_BY_ID = 
        "SELECT s.id AS shiftid, s.description, s.dailyscheduleid, " +
        "d.shiftstart, d.shiftstop, d.roundinterval, d.graceperiod, " +
        "d.dockpenalty, d.lunchstart, d.lunchstop, d.lunchthreshold " +
        "FROM shift s " +
        "JOIN dailyschedule d ON s.dailyscheduleid = d.id " +
        "WHERE s.id = ?";
    /**
     * The SQL query findS the shift ID associated with a specific badge ID in the employee table.
     */
    private static final String QUERY_FIND_BY_BADGE  = "SELECT shiftid FROM employee WHERE badgeid = ?";
    /**
     * A reference to the DAOFactory for obtaining database connectivity.
     */
    private final DAOFactory daoFactory;
    
    /**
     * Constructs a ShiftDAO with {@link DAOFactory}.
     *
     * @param daoFactory a DAOFactory instance for creating DB connectivity
     */
    public ShiftDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    /**
     * Finds then returns a {@link Shift} from its numeric ID.
     *
     * <p>IF a matching shift record is found, THEN this method constructs a
     * Shift object by parsing the retrieved fields into the appropriate
     * data types (e.g., times for shift start/stop) and integer values
     * for intervals and thresholds.</p>
     *
     * @param id the numeric shift ID used to locate the shift record
     * @return a {@link Shift} object if a matching record is found;
     *         {@code null} otherwise
     * @throws DAOException if a database access error occurs
     */
    public Shift find(int id) {
        Shift shift = null;       
        PreparedStatement ps = null; 
        ResultSet rs = null;         
        
        try {
            Connection conn = daoFactory.getConnection();
            if (conn.isValid(0)) {
                ps = conn.prepareStatement(QUERY_FIND_BY_ID);
                ps.setInt(1, id);
                
                boolean hasResults = ps.execute();
                if (hasResults) {
                    rs = ps.getResultSet();
                    
                    if (rs.next()) {
                        String description = rs.getString("description");

                        DailySchedule schedule = new DailySchedule(
                            rs.getInt("dailyscheduleid"),
                            rs.getTime("shiftstart").toLocalTime(),
                            rs.getTime("shiftstop").toLocalTime(),
                            rs.getInt("roundinterval"),
                            rs.getInt("graceperiod"),
                            rs.getInt("dockpenalty"),
                            rs.getTime("lunchstart").toLocalTime(),
                            rs.getTime("lunchstop").toLocalTime(),
                            rs.getInt("lunchthreshold")
                        );
                        
                        shift = new Shift(id, description, schedule);
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
        return shift;
    }
    
    /**
     * Finds and returns a {@link Shift} based on a {@link Badge} object.
     *
     * <p>This method looks up the shiftid associated with the
     * given badge. If found it calls {@link #find(int) find(int)} 
     * to retrieve the {@link Shift} for that shift ID.</p>
     *
     * @param badge the Badge object representing the employee's badge
     * @return a {@link Shift} object if a matching shift ID is found;
     *         {@code null} otherwise
     * @throws DAOException if a database access error occurs
     */
    public Shift find(Badge badge) {
        Shift shift = null;       
        PreparedStatement ps = null; 
        ResultSet rs = null;         
        
        try {
            Connection conn = daoFactory.getConnection();
            if (conn.isValid(0)) {
                ps = conn.prepareStatement(QUERY_FIND_BY_BADGE);
                ps.setString(1, badge.getId());
                
                boolean hasResults = ps.execute();
                if (hasResults) {
                    rs = ps.getResultSet();
                    
                    if (rs.next()) {
                        int shiftId = rs.getInt("shiftid");
                        shift = find(shiftId);
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
        return shift;
    }
}
