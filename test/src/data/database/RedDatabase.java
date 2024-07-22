package data.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static config.Config.getProperty;


public class RedDatabase implements Database {
    private static Connection connection;

    public RedDatabase() {
        try {
            Class.forName(getProperty("db.driver"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection createConnection() throws SQLException {
        String url = getProperty("db.url");
        String username = getProperty("db.username");
        String password = getProperty("db.password");
        return connection == null ? DriverManager.getConnection(url, username, password) : connection;
    }

    public static void closeConnection() throws SQLException {
        if(connection != null && !connection.isClosed() ) {
            connection.close();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connection == null || connection.isClosed() ? createConnection() : connection;
    }
}
