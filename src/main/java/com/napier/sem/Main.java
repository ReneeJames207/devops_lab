package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Main application.
 */
public class Main
{
    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect()
    {
        try
        {
            // Load database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;

        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database...");

            try
            {
                // Wait a bit for database to start
                Thread.sleep(30000);

                // Connect to database
                con = DriverManager.getConnection(
                        "jdbc:mysql://db:3306/employees?allowPublicKeyRetrieval=true&useSSL=false",
                        "root",
                        "example"
                );

                System.out.println("Successfully connected");
                break;
            }
            catch (SQLException sqle)
            {
                System.out.println(
                        "Failed to connect to database attempt " + i
                );
                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
                System.out.println("Thread interrupted.");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Get an employee from the database.
     *
     * @param ID Employee number
     * @return Employee object, or null if not found.
     */
    public Employee getEmployee(int ID)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();

            // Create SQL query
            String strSelect =
                    "SELECT emp_no, first_name, last_name "
                            + "FROM employees "
                            + "WHERE emp_no = " + ID;

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            // Check whether an employee was returned
            if (rset.next())
            {
                Employee emp = new Employee();

                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");

                return emp;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    /**
     * Print parameters for a list of employees.
     * @param employees List of employees to print
     */
    public void printSalaries(ArrayList<Employee> employees)
    {
        // Check employees is not null
        if (employees == null)
        {
            System.out.println("No employees");
            return;
        }
        // Print header
        System.out.println(String.format("%-10s %-15s %-20s %-8s", "Emp No", "First Name", "Last Name", "Salary"));
        // Loop over all employees in the list
        for (Employee emp : employees)
        {
            if (emp == null)
                continue;
            String emp_string =
                    String.format("%-10s %-15s %-20s %-8s",
                            emp.emp_no, emp.first_name, emp.last_name, emp.salary);
            System.out.println(emp_string);
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                con.close();
            }
            catch (SQLException e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }

    /**
     * Main method.
     */
    public static void main(String[] args)
    {
        // Create new application
        Main a = new Main();

        // Connect to database
        a.connect();

        // Extract employee salary information
        AllSalaries allSalaries = new AllSalaries(a.con);

        ArrayList<Employee> employees = allSalaries.getAllSalaries();

        // Test the size of the returned data
        if (employees != null)
        {
            System.out.println(employees.size());
        }

        // Disconnect from database
        a.disconnect();
    }
}