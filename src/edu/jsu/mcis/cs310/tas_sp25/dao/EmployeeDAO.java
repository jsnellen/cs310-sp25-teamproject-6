/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;
import java.sql.*;
import edu.jsu.mcis.cs310.tas_sp25.*;
import java.time.LocalDateTime;

/**
 * The EmployeeDAO class provides methods for retrieving
 * {@link Employee} objects from the database using either
 * employee ID or {@link Badge}.
 *
 * <p>This class uses the {@link DAOFactory} to access
 * related DAO objects such as BadgeDAO, DepartmentDAO, and ShiftDAO
 * for building a complete Employee object.</p>
 *
 * @author Nehemias Lucas
 */
public class EmployeeDAO {
    
    private static final String QUERY_FIND          = "SELECT * FROM employee WHERE id = ?";
    private static final String QUERY_FIND_BY_BADGE = "SELECT * FROM employee WHERE badgeid = ?";

    private final DAOFactory daoFactory;
    
    EmployeeDAO (DAOFactory daoFactory) {

        this.daoFactory = daoFactory;

    } 
    

    /**
     * Retrieves an {@link Employee} from the database using their numeric ID.
     *
     * @param id the employee's numeric ID
     * @return the corresponding Employee object, or {@code null} if not found
     * @throws DAOException if a database error occurs
     */
    public Employee find(int id){
        Employee employee = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_FIND);
                ps.setInt(1, id);

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    while (rs.next()) {
                        int employeeId = rs.getInt("id");
                        String firstname = rs.getString("firstname");
                        String middlename = rs.getString("middlename");
                        String lastname = rs.getString("lastname");
                        
                        // BadgeDAO, DepartmentDAO, and ShiftDAO used to retrieve desired badge, department, and shift respectively 
                        String badgeId = rs.getString("badgeid");
                        BadgeDAO badgeDAO = new BadgeDAO (daoFactory);
                        Badge badge = badgeDAO.find(badgeId);
                        
                        int departmentId = rs.getInt("departmentid");
                        DepartmentDAO DepartmentDAO = new DepartmentDAO (daoFactory);
                        Department department = DepartmentDAO.find(departmentId);
                        
                        int shiftId = rs.getInt("shiftid");
                        ShiftDAO ShiftDAO = new ShiftDAO (daoFactory);
                        Shift shift = ShiftDAO.find(shiftId);
                        
                        // Get employeetype and change from numeric to enum
                        int employeetypeid = rs.getInt("employeetypeid");// referenced from punchDAO, not sure if it works 
                        EmployeeType employeetype = EmployeeType.values() [employeetypeid];
                        
                        Timestamp activeTimestamp = rs.getTimestamp("active");
                        LocalDateTime active = (activeTimestamp != null) ? activeTimestamp.toLocalDateTime() : null;
                        
                     
                        // Construct employee object
                        employee = new Employee (employeeId, firstname, middlename, lastname, badge, department, shift, employeetype, active);

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

        return employee;
    }


    /**
     * Retrieves an {@link Employee} from the database using their {@link Badge}.
     *
     * @param badge the Badge object used to look up the employee
     * @return the corresponding Employee object, or {@code null} if not found
     * @throws DAOException if a database error occurs
     */

    public Employee find(Badge badge){
        //Initializing variables
        Employee employee = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Get connection from daoFactory
            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_FIND_BY_BADGE); // Query statement using badge
                ps.setString(1, badge.getId());

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    while (rs.next()) {
                        int employeeId = rs.getInt("id"); // Gets employee id
                        employee = find(employeeId); // Uses employee id to call method find(int id)
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

        return employee; // returns
    }
}