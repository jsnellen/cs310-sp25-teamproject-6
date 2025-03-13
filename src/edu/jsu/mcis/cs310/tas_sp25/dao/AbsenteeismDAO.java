/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;
import java.sql.*;
import edu.jsu.mcis.cs310.tas_sp25.*; // import class needed
import java.time.LocalDate;

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
   public Absenteeism find( Employee employee, LocalDate localDate){
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
   }
    
    
}
