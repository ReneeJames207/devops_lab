
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
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect(String location, int delay)
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            return;
        }

        int retries = 10;

        for (int i = 0; i < retries; i++)
        {
            System.out.println("Connecting to database...");

            try
            {
                Thread.sleep(delay);

                con = DriverManager.getConnection(
                        "jdbc:mysql://" + location +
                                "/employees?allowPublicKeyRetrieval=true&useSSL=false",
                        "root",
                        "example"
                );

                System.out.println("Successfully connected");
                return;
            }
            catch (SQLException e)
            {
                System.out.println("Failed to connect to database attempt " + i);
                System.out.println(e.getMessage());
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread interrupted.");
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    /**
     * Get an employee from the database.
     */
    public Employee getEmployee(int ID)
    {
        String sql = "SELECT emp_no, first_name, last_name " +
                "FROM employees WHERE emp_no = " + ID;

        try (Statement stmt = con.createStatement();
             ResultSet rset = stmt.executeQuery(sql))
        {
            if (rset.next())
            {
                Employee emp = new Employee();

                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");

                return emp;
            }

            return null;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    /**
     * Get all current employee salaries.
     */
    public ArrayList<Employee> getAllSalaries()
    {
        AllSalaries allSalaries = new AllSalaries(con);
        return allSalaries.getAllSalaries();
    }

    /**
     * Print salary information.
     */
    public void printSalaries(ArrayList<Employee> employees)
    {
        if (employees == null)
        {
            System.out.println("No employees");
            return;
        }

        System.out.println(
                String.format("%-10s %-15s %-20s %-8s",
                        "Emp No", "First Name", "Last Name", "Salary")
        );

        for (Employee emp : employees)
        {
            if (emp == null)
                continue;

            System.out.println(
                    String.format("%-10s %-15s %-20s %-8s",
                            emp.emp_no,
                            emp.first_name,
                            emp.last_name,
                            emp.salary)
            );
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
                System.out.println("Database disconnected");
            }
            catch (SQLException e)
            {
                System.out.println("Error closing database connection");
            }
        }
    }

    /**
     * Main method.
     */
    public static void main(String[] args)
    {
        Main a = new Main();

        if (args.length >= 2)
        {
            a.connect(args[0], Integer.parseInt(args[1]));
        }
        else
        {
            a.connect("db:3306", 30000);
        }

        ArrayList<Employee> employees = a.getAllSalaries();

        a.printSalaries(employees);

        a.disconnect();
    }
}
