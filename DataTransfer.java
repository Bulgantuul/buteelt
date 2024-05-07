import java.sql.*;

public class DataTransfer {
    public static void main(String[] args) {
        Connection connectionA = null;
        Connection connectionB = null;

        try {
            connectionA = DriverManager.getConnection("jdbc:mysql://localhost:3306/databaseA", "username", "password");
            connectionB = DriverManager.getConnection("jdbc:mysql://localhost:3306/databaseB", "username", "password");


            if (connectionA != null && connectionB != null) {
                // А өгөгдлийн сангаас өгөгдлүүдийг авах
                Statement stmtA = connectionA.createStatement();
                ResultSet rsA = stmtA.executeQuery("SELECT * FROM employees ORDER BY type");

                rsA.last();
                int rowCount = rsA.getRow();
                rsA.beforeFirst();

                for (int i = 1; i <= rowCount; i++) {

                // Б өгөгдлийн сан руу хуулах
                PreparedStatement pstmtB = connectionB.prepareStatement("INSERT INTO employees (id, name, type) VALUES (?, ?, ?)");
                while (rsA.next()) {
                    pstmtB.setInt(1, rsA.getInt("id"));
                    pstmtB.setString(2, rsA.getString("name"));
                    pstmtB.setString(3, rsA.getString("type"));
                    pstmtB.executeUpdate();
                }
             
                rsA.close();
                stmtA.close();

                if (allDataCopied(rsA)) {
                    System.out.println("Датаг хуулж дууслаа.");
                    break;
                } else {
                    System.out.println("Датаг хуулж дуусаагүй байна. Үргэжлүүлж байна..."); 
                }

            }
        } else {
            System.out.println("Өгөгдлийн сангаас холбоо тасарлаа. Дахин оролдно уу!");
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

    
    
    private static boolean allDataCopied(ResultSet rsA) throws SQLException {
    return !rsA.next();
}

}
