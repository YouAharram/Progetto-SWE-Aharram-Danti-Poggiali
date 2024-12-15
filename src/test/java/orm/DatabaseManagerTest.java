package orm;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import exceptions.DaoConnectionException;

public class DatabaseManagerTest {

	@Test
	public void testConnection() throws DaoConnectionException, SQLException {
		DatabaseManager dbManager = DatabaseManager.getInstance();
        Connection conn = dbManager.getConnection();
        assertNotNull(conn);
        assertTrue(conn.isValid(20));
        dbManager.closeConnection();
        assertTrue(dbManager.getConnection().isClosed()); 
	}

}
