/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;

import com.github.cliftonlabs.json_simple.*;
import edu.jsu.mcis.cs310.tas_sp25.EmployeeType;
import java.sql.Connection;
import java.sql.*;


/**
 * Handles the generation of badge summary reports from the database.
 * @author nehemias Lucas , 4-15-2025
 */
public class ReportDAO {
    private final DAOFactory daoFactory;
    
    // Query for badge summary, query statement by Weston
    private static final String BADGE_INFO = "  SELECT b.id AS badgeid,\n" + // Select b.id and rename as badgeid
"               b.description AS name,\n" + // select badge description and rename as name
"               d.description AS department,\n" + // select department description and rename as department
"               e.employeetypeid AS employeetypeid\n" +
"          FROM employee e\n" + // all data obtained from table employee, badge, department
"          JOIN badge b ON e.badgeid = b.id\n" +
"          JOIN department d ON e.departmentid = d.id\n" +
"         WHERE d.id = COALESCE(?, d.id)\n" + 
"         ORDER BY b.description"; // order all this by name
   
    ReportDAO(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    /**
     * Returns a JSON string summarizing badge data for a specific department or all departments.
     *
     * @param deptId department ID to filter by (or null to include all)
     * @return a JSON string of badge summary data
     */
    public String getBadgeSummary(Integer deptId){ // get Badge Summary method returns String, Accept Integer deparment id as arg - NLL
    
        JsonArray badgeSummary = new JsonArray(); // JsonArray instead of Shift shift
        Connection conn = daoFactory.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            
            statement = conn.prepareStatement(BADGE_INFO); // Uses qeury L20
            
            if(deptId != null) { // if a department id arg is not empty sets.
                statement.setInt(1, deptId);
            }
            else { // for when it does all instead of by department -WW
                statement.setNull(1, Types.INTEGER);
            }
            
            rs = statement.executeQuery();
            
            while (rs.next()) { // referenced from JSONTest 1 and 2, DAOUtility
            JsonObject obj = new JsonObject(); // json obj for each record
            obj.put("name", rs.getString("name")); // obtains the full name  of employeefromm badge description
            obj.put("badgeid", rs.getString("badgeid")); // obtains badge id from badge
            obj.put("department", rs.getString("department")); // obtains department name from department?
            
            //reverted back to your logic, as it allows for us to use EmployeeType
            int typeId = rs.getInt("employeetypeid");

            EmployeeType employeeType;
            if (typeId == 1) {
                employeeType = EmployeeType.FULL_TIME;
            } else {
                employeeType = EmployeeType.PART_TIME;
            }

            obj.put("type", employeeType.toDescription());

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

