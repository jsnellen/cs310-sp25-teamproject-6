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
import edu.jsu.mcis.cs310.tas_sp25.dao.DAOException;
/**
*
* @author afrix
*/


public class ShiftDAO {
// 
    private static final String QUERY_FIND_BY_ID = "SELECT * FROM shift WHERE id = ?";

    private static final String QUERY_FIND_BY_BADGE = "SELECT shiftid FROM employee WHERE badgeid = ?";

    private final DAOFactory daoFactory;

    public ShiftDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

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
                    
                    while (rs.next()) {
                        HashMap<String, String> shiftData = new HashMap<>();
                        
                        shiftData.put("description", rs.getString("description"));
                        shiftData.put("shiftStart", rs.getString("shiftstart"));
                        shiftData.put("shiftStop", rs.getString("shiftstop"));
                        shiftData.put("lunchStart", rs.getString("lunchstart"));
                        shiftData.put("lunchStop", rs.getString("lunchstop"));
                        shiftData.put("roundInterval", rs.getString("roundinterval"));
                        shiftData.put("gracePeriod", rs.getString("graceperiod"));
                        shiftData.put("dockPenalty", rs.getString("dockpenalty"));
                        shiftData.put("lunchThreshold", rs.getString("lunchthreshold"));
                        
                        shift = new Shift(id, shiftData);
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
