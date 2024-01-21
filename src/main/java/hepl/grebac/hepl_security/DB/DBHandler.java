package hepl.grebac.hepl_security.DB;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DBHandler {
    private static final Connection con = connectToSqlite();

    public DBHandler() {
    }

    public boolean handleConnexionRequest(String username, String password) {
        var hasedPassword = hashWithSHA256(password);

        return checkSQliteUserPassword(con, username, hasedPassword);
    }

    public static Connection connectToSqlite() {
        try {
            String url = "jdbc:sqlite:clients.sqlite";

            // create a connection to the database
            Connection conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            return conn;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return null;
    }

    public static boolean checkSQliteUserPassword(Connection con, String username, String password) {
        try {
            // get a user password from the database
            String sql = "SELECT password FROM user WHERE username = '" + username + "';";

            var result = con.createStatement().executeQuery(sql);

            var dbPassword = result.getString("password");

            return dbPassword.equals(password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static String hashWithSHA256(String input) {
        try {
            // Create a SHA-256 message digest
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Get the bytes of the input string
            byte[] encodedhash = digest.digest(
                    input.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
