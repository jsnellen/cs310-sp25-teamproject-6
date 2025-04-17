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
    
    // Query for badge summary, query statement by Weston
    private static final String BADGE_INFO = "  SELECT b.id AS badgeid,\n" + // Select b.id and rename as badgeid
"               b.description AS name,\n" + // select badge description and rename as name
"               d.description AS department,\n" + // select department description and rename as department
"               CASE e.employeetypeid\n" +  // Case used for conditional statement of emplyee tpe
"                   WHEN 0 THEN 'Temporary Employee'\n" + // tldr; if employee type id is 0, they are temporary and vice versa
"                   WHEN 1 THEN 'Full-Time Employee'\n" +
"               END AS type\n" + // end closes case and AS rename as type
"          FROM employee e\n" + // all data obtained from table employee, badge, department
"          JOIN badge b ON e.badgeid = b.id\n" +
"          JOIN department d ON e.departmentid = d.id\n" +
"         WHERE d.id = COALESCE(?, d.id)\n" + 
"         ORDER BY b.description"; // order all this by name
   
    ReportDAO(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }
    // referenced from Shift find(int id) in shiftDAO
    public String getBadgeSummary(Integer deptId){ // get Badge Summary method returns String, Accept Integer deparment id as arg - NLL
    
        JsonArray badgeSummary = new JsonArray(); // JsonArray instead of Shift shift
        Connection conn = daoFactory.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;
        
        


        try {
            
            statement = conn.prepareStatement(BADGE_INFO); // Uses qeury L20
            
            if (deptId != null) { // if a department id arg is not empty sets.
                statement.setInt(1, deptId);
            }
            
            rs = statement.executeQuery();
            
            while (rs.next()) { // referenced from JSONTest 1 and 2, DAOUtility
            JsonObject obj = new JsonObject(); // json obj for each record
            obj.put("name", rs.getString("name")); // obtains the full name  of employeefromm badge description
            obj.put("badgeid", rs.getString("badgeid")); // obtains badge id from badge
            obj.put("department", rs.getString("department")); // obtains department name from department?

            String empType; // used for part-ime or full-time
            if (rs.getInt("employeetypeid") == 1) { //if employeetype id is 1, they are full-time
                empType = "Full-Time Employee";
            } 
            else { //if employeetype id is 0, they are part-time
              empType = "Temporary Employee";
            }
            
            obj.put("type", empType);

            badgeSummary.add(obj); // Adds Jsonobject to the Json Array badgeSummary
        }
            

           

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (statement != null) statement.close(); } catch (Exception e) {}
        try { if (conn != null) conn.close(); } catch (Exception e) {}
    }
        return badgeSummary.toJson(); // reutrn Json Array as String
    }
        
}

