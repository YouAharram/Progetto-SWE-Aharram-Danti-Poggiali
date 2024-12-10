package orm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import DaoExceptions.ParentDaoException;
import domainModel.Parent;
import domainModel.SchoolClass;
import domainModel.Student;

public class ParentDaoDatabaseTest {

    private Connection conn;
    private ParentDaoDatabase parentDao;
    private String url = "jdbc:sqlite:database/testDB.db";
    private Parent parent;

    @Before
    public void setUp() throws SQLException {
        conn = DriverManager.getConnection(url);
        parentDao = new ParentDaoDatabase(conn);
        createTestData();
    }

	private void createTestData() throws SQLException {
		deleteTestData();

		String insertStudentQuery = "INSERT INTO Students (name, surname, username, password, date_of_birth, class) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(insertStudentQuery)) {
			stmt.setString(1, "Mario");
			stmt.setString(2, "Rossi");
			stmt.setString(3, "S12345");
			stmt.setString(4, "password1234");
			stmt.setDate(5, Date.valueOf("2005-01-14"));
			stmt.setString(6, "1A");
			stmt.executeUpdate();
		}

		ResultSet rs = conn.createStatement()
				.executeQuery("SELECT id_student FROM Students WHERE username = 'S12345';");
		rs.next();
		int studentId = rs.getInt("id_student");

		Student student = new Student(studentId, "Mario", "Rossi", new SchoolClass("1A"));

		String insertParentQuery = "INSERT INTO Parents (name, surname, username, password, id_student) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement stmtParent = conn.prepareStatement(insertParentQuery)) {
			stmtParent.setString(1, "Giorgio");
			stmtParent.setString(2, "Rossi");
			stmtParent.setString(3, "P12345");
			stmtParent.setString(4, "rossi1234");
			stmtParent.setInt(5, studentId);
			stmtParent.executeUpdate();

		}

		rs = conn.createStatement().executeQuery("SELECT id_parent FROM Parents WHERE username = 'P12345';");
		rs.next();
		int parentId = rs.getInt("id_parent");

		parent = new Parent(parentId, "Giorgio", "Rossi", student);
	}

    private void deleteTestData() throws SQLException {
        conn.createStatement().executeUpdate("DELETE FROM Parents;");
        conn.createStatement().executeUpdate("DELETE FROM Students;");
    }

    @Test
    public void testGetParentById() throws Exception {
        Parent selectedParent = parentDao.getParentById(parent.getId());
        System.out.println(selectedParent.getId() + " " + selectedParent.getName() + " " + selectedParent.getSurname() + " " + selectedParent.getStudent());
        System.out.println(parent.getId() + " " + parent.getName() + " " + parent.getSurname() + " " + parent.getStudent());

        
        assertThat(selectedParent).isEqualTo(parent);
    }

    @Test
    public void testGetParentById_nonExistingId() {
        assertThatThrownBy(() -> parentDao.getParentById(-1))
            .isInstanceOf(ParentDaoException.class)
            .hasMessage("Parent doesn't exist");
    }

    @Test
    public void testGetParentByUsernameWithPassword() throws Exception {
        Parent selectedParent = parentDao.getParentByUsernameWithPassword("P12345", "rossi1234");
        
        assertThat(selectedParent).isEqualTo(parent);
    }

    @Test
    public void testGetParentByUsernameWithPassword_wrongCredentials() {
        assertThatThrownBy(() -> parentDao.getParentByUsernameWithPassword("wrongUsername", "wrongPassword"))
            .isInstanceOf(ParentDaoException.class)
            .hasMessage("Wrong credentials");
    }

    @After
    public void tearDown() throws SQLException {
        deleteTestData();
        conn.close();
    }
}
