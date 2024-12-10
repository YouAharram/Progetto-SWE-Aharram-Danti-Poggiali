package orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.StudentDaoException;
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
	    String query = """
	        SELECT id_teaching, subject, id_teacher, class_name 
	        FROM Teachings 
	        WHERE id_teaching = ?;
	        """;

	    try (PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setInt(1, id);

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                TeacherDaoDatabase teacherDao = new TeacherDaoDatabase(conn);
	                return new TeachingAssignment(
	                    rs.getInt("id_teaching"),
	                    rs.getString("subject"),
	                    teacherDao.getTeacherById(rs.getInt("id_teacher")),
	                    new SchoolClass(rs.getString("class_name"))
	                );
	            } else {
	                throw new TeachingAssignmentDaoException(
	                    "No teaching assignment found with id: " + id);
	            }
	        }
	    } catch (SQLException | TeacherDaoException e) {
	        throw new TeachingAssignmentDaoException("Error retrieving teaching assignment with id: " + id);
	    }
	}


	@Override
	public Iterator<TeachingAssignment> getAllStudentTeachings(Student student)
			throws TeachingAssignmentDaoException, DaoConnectionException, StudentDaoException {
		DaoUtils.checkStudentExist(student, conn);
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
	public Iterator<TeachingAssignment> getAllTeacherTeachings(Teacher teacher) throws TeachingAssignmentDaoException, TeacherDaoException {
		DaoUtils.checkTeacherExist(teacher, conn);
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
