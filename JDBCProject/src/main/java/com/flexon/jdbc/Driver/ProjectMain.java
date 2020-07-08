package com.flexon.jdbc.Driver;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.Scanner;

public class ProjectMain {
    static int option;
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://192.168.1.99:3306/company?allowPublicKeyRetrieval=true&useSSL=false"; //?user=root&password=supersecret&useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT&useSSL=false";
    static final String USER = "root";
    static final String PASS = "supersecret";
    //static final String connectionString = "jdbc:mysql://127.0.0.31:3306/company" + "?user=" + USER + "&password=" + PASS + "&useUnicode=true&characterEncoding=UTF-8";

    static Connection conn = null;
    static boolean flag = true;
    static Scanner sc = new Scanner(System.in);
    static Statement st;
    static PreparedStatement ps;
    public static void main(String[] args) throws UnknownHostException {

        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");

            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            System.out.println("Successfully connected");
            System.out.println("Welcome to my company database");

            while (flag) {
                System.out.println("Please select options:    ");
                System.out.println("    1) Display all employee information");
                System.out.println("    2) Add New Employee");
                System.out.println("    3) Delete Employee");
                System.out.println("    4) Update Employee's Email Address");
                System.out.println("    5) Exit");
                System.out.print("\nYour option: ");
                while (!sc.hasNextInt()) {
                    System.out.println("Please type a number to do the operation");
                    sc.next();
                }

                option = sc.nextInt();

                dealWithOption(option);
            }

            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void displayAll() throws SQLException{
        st = conn.createStatement();

        //String use = "Use company";
        String stmt = "SELECT * FROM employees";

        //st.execute(use);
        ResultSet rs = st.executeQuery(stmt);

        // 展开结果集数据库
        while(rs.next()){
            // 通过字段检索
            String firstName  = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String email = rs.getString("email");

            // 输出数据
            System.out.print("Name: " + firstName + " " + lastName);
            System.out.print(" || Email: " + email);
            System.out.print("\n");
        }
        System.out.println("\n");
        // 完成后关闭
        rs.close();
        st.close();
    }

    public static void addEmployee() throws SQLException {
        st = conn.createStatement();

        String stmt = "INSERT INTO employees VALUES(?,?,?,?)";
        ps = conn.prepareStatement(stmt);
        System.out.println("Please type new employee's firstName");
        String firstName = sc.next();
        ps.setString(1,firstName);
        System.out.println("Please type new employee's lastName:");
        String lastName = sc.next();
        ps.setString(2,lastName);
        System.out.println("Please type new employee's department:");
        String department = sc.next();
        ps.setString(3,department);
        System.out.println("Please type new employee's email:");
        String email = sc.next();
        ps.setString(4,email);

        int response = ps.executeUpdate();
        if (response > 0) {
            System.out.println("New Employee added\n");
        } else {
            System.out.println("Something went wrong, please try again\n");
        }
        ps.close();
    }

    public static void deleteEmployee() throws SQLException {

        String stmt = "Delete From employees WHERE first_name=? AND last_name=?";
        ps = conn.prepareStatement(stmt);

        System.out.println("Please type employee's firstName you want to delete:");
        String firstName = sc.next();
        ps.setString(1, firstName);
        System.out.println("Please type employee's lastName you want to delete:");
        String lastName = sc.next();
        ps.setString(2,lastName);

        int response = ps.executeUpdate();
        if (response > 0) {
            System.out.println("Employee Deleted\n");
        } else {
            System.out.println("No certain employee, please recheck and try again\n");
        }
        ps.close();
    }

    public static void updateEmployeeEmail() throws SQLException {
        String stmt = "Update employees SET email=? WHERE first_name=? AND last_name=?";
        ps = conn.prepareStatement(stmt);

        System.out.println("Please type employee's firstName you want to update:");
        String firstName = sc.next();
        ps.setString(2, firstName);
        System.out.println("Please type employee's lastName you want to update:");
        String lastName = sc.next();
        ps.setString(3,lastName);
        System.out.println("Please type new email address:");
        String newEmail = sc.next();
        ps.setString(1,newEmail);

        int response = ps.executeUpdate();
        if (response > 0) {
            System.out.println("Email Updated\n");
        } else {
            System.out.println("Sorry, no this employee, please recheck\n");
        }
        ps.close();
    }

    public static void dealWithOption(int num) throws SQLException {
        if (num == 1) {
            displayAll();
        } else if (num == 2) {
            addEmployee();
        } else if (num == 3) {
            deleteEmployee();
        } else if (num == 4) {
            updateEmployeeEmail();
        } else if (num == 5) {
            flag = false;
            System.out.println("Thank for using Flexon Database");
        } else {
            System.out.println("Please select from the list!");
        }
    }
}
