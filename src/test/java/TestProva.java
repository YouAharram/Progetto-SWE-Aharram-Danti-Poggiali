import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import DaoExceptions.DaoConnectionException;
import orm.DatabaseManager;

public class TestProva {

	@Test
	public void test() throws SQLException, DaoConnectionException {
		DatabaseManager dbManager = DatabaseManager.getInstance();
        Connection conn = dbManager.getConnection();
        assertNotNull(conn);
        assertTrue(conn.isValid(20));
        dbManager.closeConnection();
        assertTrue(dbManager.getConnection().isClosed()); 
	}

}
