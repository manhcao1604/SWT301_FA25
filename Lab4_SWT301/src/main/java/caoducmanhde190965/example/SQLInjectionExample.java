package caoducmanhde190965.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLInjectionExample {
    public static void executeSafeQuery(Connection conn, String userInput) throws SQLException {

        String sql = "SELECT * FROM users WHERE username = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userInput);

            System.out.println("Executing query safely...");
        }
    }
}