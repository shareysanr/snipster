package snipster;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String URL = "jdbc:sqlite:snipster.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
