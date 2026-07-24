import java.sql.*;

/**
 * StudentJDBC.java
 * ------------------------------------------------------------
 * JDBC program that:
 *  a. Creates a Student table (RollNo, Name, Address) and inserts
 *     a few initial records.
 *  b. Displays records using a PreparedStatement.
 *  c. Inserts two more records using a PreparedStatement.
 *  d. Updates one record using a PreparedStatement.
 *  e. Deletes one record using a PreparedStatement.
 *  f. Displays the records again using a PreparedStatement.
 *
 * Run the parts in order top to bottom inside main() so you can
 * see the table change after each step.
 * ------------------------------------------------------------
 */
public class StudentJDBC {

    // ---------- DATABASE CONFIGURATION ----------
    // Change these to match your MySQL setup.
    private static final String DB_URL  =
        "jdbc:mysql://localhost:3306/companydb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "javauser";
    private static final String DB_PASS = "JavaPass@123";
    private static final String DRIVER  = "com.mysql.cj.jdbc.Driver";

    public static void main(String[] args) {

        try {
            Class.forName(DRIVER);
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("[OK] Connected to database successfully!\n");

            // ---------- a. CREATE TABLE + INSERT INITIAL RECORDS ----------
            createStudentTable(con);
            insertInitialRecords(con);

            // ---------- b. DISPLAY RECORDS (PreparedStatement) ----------
            System.out.println(">>> b. Displaying initial records:");
            displayRecords(con);

            // ---------- c. INSERT TWO RECORDS (PreparedStatement) ----------
            System.out.println("\n>>> c. Inserting two new records:");
            insertRecord(con, 104, "David", "Vijayawada");
            insertRecord(con, 105, "Emma", "Vizag");

            // ---------- d. UPDATE ONE RECORD (PreparedStatement) ----------
            System.out.println("\n>>> d. Updating record with RollNo 102:");
            updateRecord(con, "Guntur (Updated)", 102);

            // ---------- e. DELETE ONE RECORD (PreparedStatement) ----------
            System.out.println("\n>>> e. Deleting record with RollNo 103:");
            deleteRecord(con, 103);

            // ---------- f. DISPLAY RECORDS AGAIN (PreparedStatement) ----------
            System.out.println("\n>>> f. Displaying final records:");
            displayRecords(con);

            con.close();
            System.out.println("\nDatabase connection closed.");

        } catch (ClassNotFoundException e) {
            System.out.println("[ERROR] MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("[ERROR] Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ---------- a. CREATE TABLE ----------
    private static void createStudentTable(Connection con) throws SQLException {
        try (Statement stmt = con.createStatement()) {
            // Start fresh every run so the demo is repeatable.
            stmt.executeUpdate("DROP TABLE IF EXISTS student");

            String sql = "CREATE TABLE student (" +
                         "roll_no INT PRIMARY KEY, " +
                         "name VARCHAR(50) NOT NULL, " +
                         "address VARCHAR(100))";
            stmt.executeUpdate(sql);
            System.out.println("[OK] Table 'student' created fresh.");
        }
    }

    // ---------- a. INSERT INITIAL RECORDS ----------
    private static void insertInitialRecords(Connection con) throws SQLException {
        String sql = "INSERT INTO student (roll_no, name, address) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, 101);
            ps.setString(2, "Arun");
            ps.setString(3, "Hyderabad");
            ps.executeUpdate();

            ps.setInt(1, 102);
            ps.setString(2, "Bhavana");
            ps.setString(3, "Guntur");
            ps.executeUpdate();

            ps.setInt(1, 103);
            ps.setString(2, "Charan");
            ps.setString(3, "Chennai");
            ps.executeUpdate();

            System.out.println("[OK] Initial records inserted.\n");
        }
    }

    // ---------- b. & f. DISPLAY RECORDS (PreparedStatement) ----------
    private static void displayRecords(Connection con) throws SQLException {
        String sql = "SELECT * FROM student ORDER BY roll_no";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("-----------------------------------------------------");
            System.out.printf("%-10s %-20s %-20s%n", "ROLL NO", "NAME", "ADDRESS");
            System.out.println("-----------------------------------------------------");

            while (rs.next()) {
                int rollNo = rs.getInt("roll_no");
                String name = rs.getString("name");
                String address = rs.getString("address");
                System.out.printf("%-10d %-20s %-20s%n", rollNo, name, address);
            }
            System.out.println("-----------------------------------------------------");
        }
    }

    // ---------- c. INSERT RECORD (PreparedStatement) ----------
    private static void insertRecord(Connection con, int rollNo, String name, String address) throws SQLException {
        String sql = "INSERT INTO student (roll_no, name, address) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, rollNo);
            ps.setString(2, name);
            ps.setString(3, address);
            int rows = ps.executeUpdate();
            System.out.println("[OK] " + rows + " record inserted (RollNo " + rollNo + ").");
        }
    }

    // ---------- d. UPDATE RECORD (PreparedStatement) ----------
    private static void updateRecord(Connection con, String newAddress, int rollNo) throws SQLException {
        String sql = "UPDATE student SET address = ? WHERE roll_no = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newAddress);
            ps.setInt(2, rollNo);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("[OK] Record updated for RollNo " + rollNo + ".");
            } else {
                System.out.println("[ERROR] No record found with RollNo " + rollNo);
            }
        }
    }

    // ---------- e. DELETE RECORD (PreparedStatement) ----------
    private static void deleteRecord(Connection con, int rollNo) throws SQLException {
        String sql = "DELETE FROM student WHERE roll_no = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, rollNo);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("[OK] Record deleted for RollNo " + rollNo + ".");
            } else {
                System.out.println("[ERROR] No record found with RollNo " + rollNo);
            }
        }
    }
}
