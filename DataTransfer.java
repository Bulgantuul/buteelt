import java.sql.*;

public class DataTransfer {
    public static void main(String[] args) {
        Connection connectionA = null;
        Connection connectionB = null;

        try {
            connectionA = DriverManager.getConnection("jdbc:mysql://localhost:3306/databaseA", "username", "password");
            connectionB = DriverManager.getConnection("jdbc:mysql://localhost:3306/databaseB", "username", "password");

            while (true) {
                // А өгөгдлийн сангаас өгөгдлүүдийг авах
                Statement stmtA = connectionA.createStatement();
                ResultSet rsA = stmtA.executeQuery("SELECT * FROM employees ORDER BY type");

                // Б өгөгдлийн сан руу хуулах
                PreparedStatement pstmtB = connectionB.prepareStatement("INSERT INTO employees (id, name, type) VALUES (?, ?, ?)");
                while (rsA.next()) {
                    pstmtB.setInt(1, rsA.getInt("id"));
                    pstmtB.setString(2, rsA.getString("name"));
                    pstmtB.setString(3, rsA.getString("type"));
                    pstmtB.executeUpdate();
                }

                // Close statement and result set for database A
                rsA.close();
                stmtA.close();

                // Check if all data is copied
                // Assuming you have some logic to check this

                // If all data is copied, end the program
                // Assuming you have some condition to check this
                if (allDataCopied()) {
                    System.out.println("All data copied. Exiting program.");
                    break;
                } else {
                    // If not all data is copied, continue the loop
                    System.out.println("Not all data copied. Continuing...");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close connections
            try {
                if (connectionA != null) connectionA.close();
                if (connectionB != null) connectionB.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Dummy method, replace with your actual logic
    private static boolean allDataCopied() {
        // Assuming some condition to check if all data is copied
        return false;
    }
}
