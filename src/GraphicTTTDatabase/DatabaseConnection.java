package GraphicTTTDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String HOST = "mysql-31a22aa8-gawedhaps-083.f.aivencloud.com";
    private static final String PORT = "24783";
    private static final String DATABASE = "defaultdb";
    private static final String USERNAME = "avnadmin";
    private static final String PASSWORD = "AVNS_Mzeng7WIPOVTneHLmPz";

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?sslmode=require",
                USERNAME, PASSWORD
        );
    }

    public static boolean verifyLogin(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             var stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            var rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean registerUser(String username, String password) {
        String checkQuery = "SELECT * FROM users WHERE username = ?";
        String insertQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = getConnection();
             var checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setString(1, username);
            var rs = checkStmt.executeQuery();
            if (rs.next()) {
                return false;
            }
            try (var insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);
                insertStmt.executeUpdate();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
