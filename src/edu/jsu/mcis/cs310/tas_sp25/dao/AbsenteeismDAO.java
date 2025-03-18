/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;
import java.sql.*;
import edu.jsu.mcis.cs310.tas_sp25.*; // import class needed
import java.time.LocalDate;
import java.math.BigDecimal; // Imported for percentage

/**
 *
 * @author afrix
 *  assisting, NLL 3-13-2025
 */
public class AbsenteeismDAO {
    
    private final DAOFactory daoFactory;

    AbsenteeismDAO(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }
  
    // FIND METHOD- NLL
   /*public Absenteeism find( Employee employee, LocalDate localDate){
       Absenteeism absentee = null;
       // Gets Employee object  and localdate object  from absenteeism class and make sure it is the same as the argument in find method
       try{
           if (absentee.getEmployee().equals(employee) && absentee.getStartDateofPayPeriod().equals(localDate)) {
               return absentee;
            }
        }catch(Exception e){
            throw new DAOException(e.getMessage());
        }
       return absentee;
   }*/

    public Absenteeism find(Employee employee, LocalDate payPeriod) {
        Absenteeism absenteeism = null;
        Connection conn = daoFactory.getConnection();

        try {
            String query = "SELECT * FROM absenteeism WHERE employeeid = ? AND payperiod = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, employee.getId());
            stmt.setDate(2, Date.valueOf(payPeriod));

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                BigDecimal percentage = BigDecimal.valueOf(rs.getDouble("percentage"));
                absenteeism = new Absenteeism(employee, payPeriod, percentage);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return absenteeism;
    }

    // Create method
    public void create(Absenteeism absenteeism) {
        Connection conn = daoFactory.getConnection(); // Revise code and added connection- nll

        try {
            String query = "REPLACE INTO absenteeism (employeeid, payperiod, percentage) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, absenteeism.getEmployee().getId());
            stmt.setDate(2, Date.valueOf(absenteeism.getPayPeriod())); 
            stmt.setDouble(3, absenteeism.getPercentage().doubleValue());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
}