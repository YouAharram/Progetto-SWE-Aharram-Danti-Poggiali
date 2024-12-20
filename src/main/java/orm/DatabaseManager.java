package orm;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

import exceptions.DaoConnectionException;

public class DatabaseManager {

    private static DatabaseManager instance;

    private Connection connection;
	private static final String url = "jdbc:mysql://34.173.200.107:3306/classFlowDB";
	private static final String user = "db_manager";
	private static final String password = "classFlow@2024";

    private DatabaseManager() throws DaoConnectionException {
          try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			throw new DaoConnectionException("Cannot connect with ClassFlow. Please check internet connection");
		}
    }

    public static synchronized DatabaseManager getInstance() throws DaoConnectionException {
		if (instance == null) {
			instance = new DatabaseManager();
		}
		return instance;
    }

    public Connection getConnection() {
        return connection;
    }

	public void closeConnection() throws SQLException {
		if (connection != null) {
			connection.close();
		}
    }

}