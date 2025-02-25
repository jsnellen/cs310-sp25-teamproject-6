/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;
import java.sql.*;
import edu.jsu.mcis.cs310.tas_sp25.*;


/**
 *
 * @created EmployeeDAO, Nehemias Lucas 2-24-2025
 */
public class EmployeeDAO {
    
    private static final String QUERY_FIND = "SELECT *  FROM WHERE id =?";
    private final DAOFactory daoFactory;
    
    EmployeeDAO (DAOFactory daoFactory) {

        this.daoFactory = daoFactory;

    } 
    
    public Employee find(String id){
        Employee employee = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_FIND);
                ps.setString(1, id);

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
                        
                     
                        // Construct employee object
                        employee = new Employee (employeeId, firstname, middlename, lastname, badge, department, shift, employeetype);

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
}
