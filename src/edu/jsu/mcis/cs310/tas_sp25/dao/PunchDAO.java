/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;
import java.sql.*;
import edu.jsu.mcis.cs310.tas_sp25.*;

/**
 *
 * @Creating DAO for punch, Nehemias Loarca Lucas 2-21-2025
 */
public class PunchDAO {
    private static final String QUERY_FIND = "SELECT * FROM badge WHERE terminalid = ?";
    private final DAOFactory daoFactory;
    
    PunchDAO(DAOFactory daoFactory)
    { 
        this.daoFactory = daoFactory;
    }
    
    public Punch find(int terminalid){
        Punch punch = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_FIND);
                ps.setInt(1, terminalid);

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    while (rs.next()) {

                       
                        // badge or punch type is not initialized anywhere in the code - Weston
                        punch = new Punch(terminalid, badge, punchtype); // ???

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