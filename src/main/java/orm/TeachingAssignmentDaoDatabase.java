package orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.TeacherDaoException;
import DaoExceptions.TeachingAssignmentDaoException;
import domainModel.SchoolClass;
import domainModel.Student;
import domainModel.Teacher;
import domainModel.TeachingAssignment;

public class TeachingAssignmentDaoDatabase implements TeachingAssignmentDao {
	
	private Connection conn;

	public TeachingAssignmentDaoDatabase(Connection conn) {
		this.conn = conn;
	}

	public TeachingAssignment getTeachingById(int id) throws TeachingAssignmentDaoException {
		String query = "SELECT id_teaching, subject, id_teacher, class_name FROM Teachings WHERE id_teaching = " + id + ";";
		try {
			TeacherDaoDatabase teacherDao = new TeacherDaoDatabase(conn);
			ResultSet rs = getResultsFromDB(query);
			rs.next();
			return new TeachingAssignment(
						rs.getInt("id_teaching"), 
						rs.getString("subject"), 
						teacherDao.getTeacherById(rs.getInt("id_teacher")), 
						new SchoolClass(rs.getString("class_name")));
			}
		catch (SQLException | TeacherDaoException e) {
			throw new TeachingAssignmentDaoException("");
		}
	}

	private ResultSet getResultsFromDB(String query) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();
		return rs;
	}

	@Override
	public Iterator<TeachingAssignment> getAllStudentTeachings(Student student)
			throws TeachingAssignmentDaoException, DaoConnectionException {
		String query = """
	            SELECT id_teaching, subject, id_teacher, class_name 
	            FROM Students 
	            JOIN Classes ON Students.class = Classes.name 
	            JOIN Teachings ON Classes.name = Teachings.class_name 
	            WHERE id_student = ?;
	            """;
	    List<TeachingAssignment> teachings = new ArrayList<>();
	    
	    try (PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setInt(1, student.getId());
	        TeacherDaoDatabase teacherDao = new TeacherDaoDatabase(conn);
	        
	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                TeachingAssignment teaching = new TeachingAssignment(
	                        rs.getInt("id_teaching"), 
	                        rs.getString("subject"), 
	                        teacherDao.getTeacherById(rs.getInt("id_teacher")), 
	                        new SchoolClass(rs.getString("class_name"))
	                );
	                teachings.add(teaching);
	            }
	        }
	    } catch (SQLException | TeacherDaoException e) {
	        throw new TeachingAssignmentDaoException("Error fetching teaching assignments for student.");
	    }
	    return teachings.iterator();
	}

	@Override
	public Iterator<TeachingAssignment> getAllTeacherTeachings(Teacher teacher) throws TeachingAssignmentDaoException {
	    String query = """
	        SELECT id_teaching, subject, id_teacher, class_name 
	        FROM Teachings 
	        WHERE id_teacher = ?;
	        """;
	    List<TeachingAssignment> teachings = new ArrayList<>();

	    try (PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setInt(1, teacher.getId());

	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                TeachingAssignment teaching = new TeachingAssignment(
	                        rs.getInt("id_teaching"),
	                        rs.getString("subject"),
	                        teacher,
	                        new SchoolClass(rs.getString("class_name"))
	                );
	                teachings.add(teaching);
	            }
	        }
	    } catch (SQLException e) {
	        throw new TeachingAssignmentDaoException("Error fetching teaching assignments for teacher.");
	    }

	    return teachings.iterator();
	}

}
