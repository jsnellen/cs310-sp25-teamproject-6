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
    private static final String BADGE_INFO = null; // maybe an query similar to QUERY_FIND_BY_ID in shiftDAO?
    
    ReportDAO(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }
    // referanced from Shift find(int id) in shiftDAO
    public JsonArray getBadgeSummary(){ // get Badge Summary method returns JSON Array - NLL
    
        JsonArray badgeSummary = new JsonArray(); // JsonArray instead of Shift shift
        Connection conn = daoFactory.getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;

        try {
            statement = conn.prepareStatement(BADGE_INFO);
            
            

           

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return badgeSummary;
    }
        
    }

