package orm;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

import exceptions.DaoConnectionException;

public class DatabaseManager {

    private static DatabaseManager instance;

    private Connection connection;
	private static final String url = "jdbc:mysql://sql8.freesqldatabase.com:3306/sql8764243?connectTimeout=20000";
	private static final String user = "sql8764243";
	private static final String password = "1UkX8QxDFQ";

    private DatabaseManager() throws DaoConnectionException {
          try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			throw new DaoConnectionException("Cannot connect with ClassFlow. Please check internet connection or retry");
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