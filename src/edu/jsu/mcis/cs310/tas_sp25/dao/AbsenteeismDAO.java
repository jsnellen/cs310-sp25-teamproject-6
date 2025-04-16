/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;

import java.sql.*;
import edu.jsu.mcis.cs310.tas_sp25.*; // import class needed
import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * Data Access Object (DAO) for managing Absenteeism records in the database.
 * <p>
 * Provides methods to find and create absenteeism records associated with an employee
 * for a specific pay period.
 * </p>
 * 
 * @author afrix
 * @author Nehemias Lucas
 * @version 3/13/2025
 */
public class AbsenteeismDAO {

    /** Reference to the DAOFactory to obtain database connections */
    private final DAOFactory daoFactory;

    /** SQL query for finding an absenteeism record by employee and pay period */
    private static final String QUERY_FIND = "SELECT * FROM absenteeism WHERE employeeid = ? AND payperiod = ?";

    /** SQL query for inserting or updating an absenteeism record */
    private static final String QUERY_CREATE = "REPLACE INTO absenteeism (employeeid, payperiod, percentage) VALUES (?, ?, ?)";

    /**
     * Constructor for AbsenteeismDAO.
     * 
     * @param daoFactory the DAOFactory used to access the database connection
     */
    AbsenteeismDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Finds an absenteeism record for a given employee and pay period.
     * 
     * @param employee the employee whose absenteeism record is being retrieved
     * @param payPeriod the pay period for which to retrieve the absenteeism data
     * @return the corresponding Absenteeism object, or null if not found
     */
    public Absenteeism find(Employee employee, LocalDate payPeriod) {
        Absenteeism absenteeism = null;
        Connection conn = daoFactory.getConnection();

        try {
            PreparedStatement stmt = conn.prepareStatement(QUERY_FIND);
            LocalDate normalized = payPeriod.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.SUNDAY));
            stmt.setInt(1, employee.getId());
            stmt.setDate(2, Date.valueOf(normalized));

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

    /**
     * Inserts or updates an absenteeism record in the database.
     * 
     * @param absenteeism the Absenteeism object to be persisted
     */
    public void create(Absenteeism absenteeism) {
        Connection conn = daoFactory.getConnection();

        try {
            PreparedStatement stmt = conn.prepareStatement(QUERY_CREATE);
            stmt.setInt(1, absenteeism.getEmployee().getId());
            stmt.setDate(2, Date.valueOf(absenteeism.getPayPeriod()));
            stmt.setDouble(3, absenteeism.getPercentage().doubleValue());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
