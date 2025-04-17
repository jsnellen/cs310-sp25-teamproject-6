/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;

import com.github.cliftonlabs.json_simple.*;
import java.sql.Connection;
import java.sql.*;


/**
 *
 * @author nehemias Lucas , 4-15-2025
 */
public class ReportDAO {
    private final DAOFactory daoFactory;
    
    // Query for badge summary
    private static final String BADGE_INFO = "  SELECT b.id AS badgeid,\n" +
"               b.description AS name,\n" +
"               d.description AS department,\n" +
"               CASE e.employeetypeid\n" +
"                   WHEN 0 THEN 'Temporary Employee'\n" +
"                   WHEN 1 THEN 'Full-Time Employee'\n" +
"               END AS type\n" +
"          FROM employee e\n" +
"          JOIN badge b ON e.badgeid = b.id\n" +
"          JOIN department d ON e.departmentid = d.id\n" +
"         WHERE d.id = COALESCE(?, d.id)\n" +
"         ORDER BY b.description"; // maybe an query similar to QUERY_FIND_BY_ID in shiftDAO?
    
    ReportDAO(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }
    // referanced from Shift find(int id) in shiftDAO
    public String getBadgeSummary(Integer deptId){ // get Badge Summary method returns JSON Array - NLL
    
        JsonArray badgeSummary = new JsonArray(); // JsonArray instead of Shift shift
        Connection conn = daoFactory.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;
        
        


        try {
        
            statement = conn.prepareStatement(BADGE_INFO);
            if (deptId != null) {
                statement.setInt(1, deptId);
            }
            rs = statement.executeQuery();
            while (rs.next()) {
            JsonObject obj = new JsonObject();
            obj.put("name", rs.getString("name"));
            obj.put("badgeid", rs.getString("badgeid"));
            obj.put("department", rs.getString("department"));

            String employeeType = rs.getInt("employeetypeid") == 0 ?
                                  "Full-Time Employee" : "Temporary Employee";
            obj.put("type", employeeType);

            badgeSummary.add(obj);
        }
            

           

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (statement != null) statement.close(); } catch (Exception e) {}
        try { if (conn != null) conn.close(); } catch (Exception e) {}
    }
        return badgeSummary.toJson();
    }
        
}

