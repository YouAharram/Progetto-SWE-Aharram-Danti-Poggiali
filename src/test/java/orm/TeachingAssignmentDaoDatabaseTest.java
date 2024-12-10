package orm;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.TeacherDaoException;
import DaoExceptions.TeachingAssignmentDaoException;
import domainModel.SchoolClass;
import domainModel.Student;
import domainModel.Teacher;
import domainModel.TeachingAssignment;

public class TeachingAssignmentDaoDatabaseTest {
	
	private Connection conn;
	private TeachingAssignmentDaoDatabase teachingAssignmentDao;
	private String url = "jdbc:sqlite:database/testDB.db";
	private Student student;
	private Teacher teacher;
	private int studentId;
	private int teacherId;
	private SchoolClass schoolClass;
	private TeachingAssignment teaching;
	private TeachingAssignment secondTeaching;

	@Before
	public void openConnection() throws SQLException {
		conn = DriverManager.getConnection(url);
		teachingAssignmentDao = new TeachingAssignmentDaoDatabase(conn);
		createTestData();
	}
	
	private void createTestData() throws SQLException {
		deleteTestData();
		
		String insertClassQuery = "INSERT INTO Classes (name, classroom) VALUES ('1A', 'A01');";
		conn.createStatement().executeUpdate(insertClassQuery);
		
		String insertStudentQuery = "INSERT INTO Students (username, password, name, surname, date_of_birth, class) "
				+ "VALUES ( 's001', 'pass123', 'Mario', 'Rossi', '2005-03-15', '1A');";
		conn.createStatement().executeUpdate(insertStudentQuery);
		
		String insertTeacherQuery = "INSERT INTO Teachers (username, password, name, surname) "
				+ "VALUES  ('t001', 'pass123', 'Casimiro', 'Grumaioli');";
		conn.createStatement().executeUpdate(insertTeacherQuery);
		
		String getTeacherIdQuery = "SELECT id_teacher FROM Teachers WHERE username = 't001';";
		ResultSet rs = conn.createStatement().executeQuery(getTeacherIdQuery);
		rs.next();
		teacherId = rs.getInt("id_teacher");
		teacher = new Teacher(teacherId, "Casimiro", "Grumaioli");
		
		String insertTeachingQueryMath = "INSERT INTO Teachings (id_teacher, class_name, subject) "
				+ "VALUES  (" + teacherId + ", '1A', 'Matematica');";
		conn.createStatement().executeUpdate(insertTeachingQueryMath);
		
		String insertTeachingQueryScience = "INSERT INTO Teachings (id_teacher, class_name, subject) "
				+ "VALUES  (" + teacherId + ", '1A', 'Scienze');";
		conn.createStatement().executeUpdate(insertTeachingQueryScience);
		
		String getTeachingIdQuery = "SELECT id_teaching FROM Teachings WHERE subject = 'Matematica';";
		rs = conn.createStatement().executeQuery(getTeachingIdQuery);
		rs.next();
		int teachingId = rs.getInt("id_teaching");
		teaching = new TeachingAssignment(teachingId, "Matematica", teacher, new SchoolClass("1A"));

		String getSecondTeachingIdQuery = "SELECT id_teaching FROM Teachings WHERE subject = 'Scienze';";
		rs = conn.createStatement().executeQuery(getSecondTeachingIdQuery);
		rs.next();
		int secondTeachingId = rs.getInt("id_teaching");
		secondTeaching = new TeachingAssignment(secondTeachingId, "Scienze", teacher, new SchoolClass("1A"));
		
		String getStudentIdQuery = "SELECT id_student FROM Students WHERE username = 's001';";
		rs = conn.createStatement().executeQuery(getStudentIdQuery);
		rs.next();
		studentId = rs.getInt("id_student");
		student = new Student(studentId, "Mario", "Rossi", new SchoolClass("1A"));
	}

	@Test
	public void testGetAllTeacherTeachings() throws SQLException, TeacherDaoException, DaoConnectionException, TeachingAssignmentDaoException {
	    List<TeachingAssignment> expectedTeachings = List.of(teaching, secondTeaching);

	    Iterator<TeachingAssignment> actualTeachingsIterator = teachingAssignmentDao.getAllTeacherTeachings(teacher);
	    List<TeachingAssignment> actualTeachingsList = new ArrayList<>();
	    actualTeachingsIterator.forEachRemaining(actualTeachingsList::add);

	    assertThat(actualTeachingsList).containsExactlyInAnyOrderElementsOf(expectedTeachings);
	}

	@Test
	public void testGetAllStudentTeachings() throws SQLException, TeacherDaoException, DaoConnectionException, TeachingAssignmentDaoException {
	    List<TeachingAssignment> expectedTeachings = List.of(teaching, secondTeaching);

	    Iterator<TeachingAssignment> actualTeachingsIterator = teachingAssignmentDao.getAllStudentTeachings(student);
	    List<TeachingAssignment> actualTeachingsList = new ArrayList<>();
	    actualTeachingsIterator.forEachRemaining(actualTeachingsList::add);

	    assertThat(actualTeachingsList).containsExactlyInAnyOrderElementsOf(expectedTeachings);
	}

	@After
	public void closeConnection() throws SQLException {
		deleteTestData();
		conn.close();
	}
	
	private void deleteTestData() throws SQLException {
		String deleteTeachingsQuery = "DELETE FROM Teachings;";
		conn.createStatement().executeUpdate(deleteTeachingsQuery);
		
		String deleteTeachersQuery = "DELETE FROM Teachers;";
		conn.createStatement().executeUpdate(deleteTeachersQuery);

		String deleteStudentsQuery = "DELETE FROM Students;";
		conn.createStatement().executeUpdate(deleteStudentsQuery);
		
		String deleteClassesQuery = "DELETE FROM Classes;";
		conn.createStatement().executeUpdate(deleteClassesQuery);
	}
}
