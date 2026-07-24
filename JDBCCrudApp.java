import java.sql.*;
import java.util.Scanner;

public class JDBCCrudApp {

    // ---------- 1. DATABASE CONFIGURATION ----------
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/companydb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "javauser";
    private static final String DB_PASS = "JavaPass@123";
    private static final String DRIVER  = "com.mysql.cj.jdbc.Driver";

    private static Connection connection;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("[OK] Connected to database successfully!\n");

            boolean exit = false;
            while (!exit) {
                printMenu();
                System.out.print("Enter your choice: ");
                int choice = readInt(sc);

                switch (choice) {
                    case 1 -> createTable();
                    case 2 -> insertRecord(sc);
                    case 3 -> displayRecords();
                    case 4 -> updateRecord(sc);
                    case 5 -> deleteRecord(sc);
                    case 6 -> exit = true;
                    default -> System.out.println("Invalid choice! Please enter 1-6.\n");
                }
            }
            System.out.println("Exiting application. Goodbye!");

        } catch (ClassNotFoundException e) {
            System.out.println("[ERROR] MySQL JDBC Driver not found. Check your classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("[ERROR] Database error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection();
            sc.close();
        }
    }

    private static void printMenu() {
        System.out.println("========== EMPLOYEE DATABASE MENU ==========");
        System.out.println("1. Create Table");
        System.out.println("2. Insert Record");
        System.out.println("3. Display All Records");
        System.out.println("4. Update Record");
        System.out.println("5. Delete Record");
        System.out.println("6. Exit");
        System.out.println("==============================================");
    }

    private static void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS employee (" +
                     "emp_id INT PRIMARY KEY AUTO_INCREMENT, " +
                     "emp_name VARCHAR(50) NOT NULL, " +
                     "emp_dept VARCHAR(30), " +
                     "emp_salary DOUBLE)";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("[OK] Table 'employee' created successfully (or already exists).\n");
        }
    }

    private static void insertRecord(Scanner sc) throws SQLException {
        System.out.print("Enter Employee Name : ");
        String name = sc.next();
        System.out.print("Enter Department    : ");
        String dept = sc.next();
        System.out.print("Enter Salary         : ");
        double salary = readDouble(sc);

        String sql = "INSERT INTO employee (emp_name, emp_dept, emp_salary) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, dept);
            ps.setDouble(3, salary);
            int rows = ps.executeUpdate();
            System.out.println("[OK] " + rows + " record(s) inserted successfully.\n");
        }
    }

    private static void displayRecords() throws SQLException {
        String sql = "SELECT * FROM employee";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("-----------------------------------------------------");
            System.out.printf("%-8s %-20s %-15s %-10s%n", "ID", "NAME", "DEPARTMENT", "SALARY");
            System.out.println("-----------------------------------------------------");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                int id = rs.getInt("emp_id");
                String name = rs.getString("emp_name");
                String dept = rs.getString("emp_dept");
                double salary = rs.getDouble("emp_salary");
                System.out.printf("%-8d %-20s %-15s %-10.2f%n", id, name, dept, salary);
            }
            if (!hasData) {
                System.out.println("No records found.");
            }
            System.out.println("-----------------------------------------------------\n");
        }
    }

    private static void updateRecord(Scanner sc) throws SQLException {
        System.out.print("Enter Employee ID to update : ");
        int id = readInt(sc);
        System.out.print("Enter New Salary             : ");
        double salary = readDouble(sc);

        String sql = "UPDATE employee SET emp_salary = ? WHERE emp_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, salary);
            ps.setInt(2, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("[OK] Record updated successfully.\n");
            } else {
                System.out.println("[ERROR] No record found with ID " + id + "\n");
            }
        }
    }

    private static void deleteRecord(Scanner sc) throws SQLException {
        System.out.print("Enter Employee ID to delete : ");
        int id = readInt(sc);

        String sql = "DELETE FROM employee WHERE emp_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("[OK] Record deleted successfully.\n");
            } else {
                System.out.println("[ERROR] No record found with ID " + id + "\n");
            }
        }
    }

    private static int readInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }
        return sc.nextInt();
    }

    private static double readDouble(Scanner sc) {
        while (!sc.hasNextDouble()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }
        return sc.nextDouble();
    }

    private static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}