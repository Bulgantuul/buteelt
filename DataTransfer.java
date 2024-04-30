
import java.sql.*;

public class DataTransfer {

    public static void main(String[] args) {
        Connection connectionA = null;
        Connection connectionB = null;

        try {
            connectionA = DriverManager.getConnection("jdbc:mysql://localhost:3306/databaseA", "username", "password");

            connectionB = DriverManager.getConnection("jdbc:mysql://localhost:3306/databaseB", "username", "password");

            while (true) {
                // Мэдээллийн сангаас ажилтны мэдээллийг авах А
                Statement stmtA = connectionA.createStatement();
                ResultSet rsA = stmtA.executeQuery("SELECT * FROM employees");

                // Ажилтны өгөгдлийг төрлөөр нь ангилах
                // Ажилчдын эрэмбэлсэн мэдээллийг мэдээллийн санд хуулах Б
                PreparedStatement pstmtB = connectionB.prepareStatement("INSERT INTO employees (id, name, type) VALUES (?, ?, ?)");
                while (rsA.next()) {
                    pstmtB.setInt(1, rsA.getInt("id"));
                    pstmtB.setString(2, rsA.getString("name"));
                    pstmtB.setString(3, rsA.getString("type"));
                    pstmtB.executeUpdate();
                }

                // Өгөгдлийн сангийн хаах мэдэгдэл болон үр дүнгийн багц А
                rsA.close();
                stmtA.close();

                // Бүх өгөгдөл хуулсан эсэхийг шалгана 
                // Хэрэв бүх өгөгдлийг хуулсан бол програмыг дуусгана 
                if (allDataCopied()) {
                    System.out.println("All data copied. Exiting program.");
                    break;
                } else {
                    // Хэрэв бүх өгөгдлийг хуулж аваагүй бол давталтыг үргэлжлүүлнэ 
                    System.out.println("Not all data copied. Continuing...");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Холболтуудыг хаах
            try {
                if (connectionA != null) {
                    connectionA.close();
                }
                if (connectionB != null) {
                    connectionB.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean allDataCopied() {
        // Бүх өгөгдлийг хуулсан эсэхийг шалгах нөхцөлийг авна
        return false;
    }
}
