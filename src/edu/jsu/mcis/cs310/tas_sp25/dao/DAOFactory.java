package edu.jsu.mcis.cs310.tas_sp25.dao;

import java.sql.*;

/**
 * Creates DAO instances and manages a shared database connection
 * using configuration properties.
 * 
 * @author Nehemias Loarca Lucas
 */
public final class DAOFactory {

    private static final String PROPERTY_URL = "url";
    private static final String PROPERTY_USERNAME = "username";
    private static final String PROPERTY_PASSWORD = "password";

    private final String url, username, password;
    
    private Connection conn = null;

    /**
     * Initializes a DAOFactory using database credentials defined by a prefix.
     *
     * @param prefix the property prefix to load database config values
     * @throws DAOException if the connection fails
     */
    public DAOFactory(String prefix) {
        DAOProperties properties = new DAOProperties(prefix);

        this.url = properties.getProperty(PROPERTY_URL);
        this.username = properties.getProperty(PROPERTY_USERNAME);
        this.password = properties.getProperty(PROPERTY_PASSWORD);

        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    /**
     * Returns the shared database {@link Connection}.
     *
     * @return the database connection instance
     */
    Connection getConnection() {
        return conn;
    }

    /**
     * Returns a new instance of {@link BadgeDAO}.
     *
     * @return BadgeDAO instance
     */
    public BadgeDAO getBadgeDAO() {
        return new BadgeDAO(this);
    }

    // Added method to get PunchDAO - Weston Wyatt [2/23/2025]
    /**
     * Returns a new instance of {@link PunchDAO}.
     *
     * @return PunchDAO instance
     */
    public PunchDAO getPunchDAO() {
        return new PunchDAO(this);
    }

    /**
     * Returns a new instance of {@link ShiftDAO}.
     *
     * @return ShiftDAO instance
     */
    public ShiftDAO getShiftDAO() {
        return new ShiftDAO(this);
    }

    // Added method to get DepartmentDAO - Weston Wyatt [2/22/2025]
    /**
     * Returns a new instance of {@link DepartmentDAO}.
     *
     * @return DepartmentDAO instance
     */
    public DepartmentDAO getDepartmentDAO() {
        return new DepartmentDAO(this);
    }

    // Added getter method for EmployeeDAO, Nehemias Lucas 2-24-2025
    /**
     * Returns a new instance of {@link EmployeeDAO}.
     *
     * @return EmployeeDAO instance
     */
    public EmployeeDAO getEmployeeDAO(){ 
        return new EmployeeDAO(this);
    }

    // Added method to get getAbsenteeismDAO - Nehemias Lucas 3-12-2025
    /**
     * Returns a new instance of {@link AbsenteeismDAO}.
     *
     * @return AbsenteeismDAO instance
     */
    public AbsenteeismDAO getAbsenteeismDAO(){
        return new AbsenteeismDAO(this);
    }

    // Added method to get ReportDAO - Nehemias Lucas 3-12-2025
    /**
     * Returns a new instance of {@link ReportDAO}.
     *
     * @return ReportDAO instance
     */
    public ReportDAO getReportDAO(){
        return new ReportDAO(this);
    }

}
