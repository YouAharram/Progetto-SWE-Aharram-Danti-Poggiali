package orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.ParentDaoException;
import DaoExceptions.StudentDaoException;
import domainModel.Parent;
import domainModel.Student;
import domainModel.Teacher;

public class ParentDaoDatabase implements ParentDao {

	private Connection conn;

	public ParentDaoDatabase(Connection conn) {
		this.conn = conn;
	}

	
	public Parent getParentById(int id) throws ParentDaoException, StudentDaoException, DaoConnectionException {
		String query = "SELECT id_parent, surname, name, id_student FROM Parents WHERE id_parent = ?";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			if (!rs.next())
				throw new ParentDaoException("Parent doesn't exist");

			Student son = new StudentDaoDatabase(conn).getStudentById(rs.getInt("id_student"));

			return new Parent(id, rs.getString("name"), rs.getString("surname"), son);
		} catch (SQLException e) {
			throw new ParentDaoException("Database error while fetching parent data");
		}
	}


	@Override
	public Parent getParentByUsernameWithPassword(String username, String password) throws ParentDaoException, StudentDaoException {
		
		String query = "SELECT id_parent, surname, name, id_student FROM Parents WHERE username = ? AND password = ?";
		try (PreparedStatement stmt = conn.prepareStatement(query)){
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();

			if (!rs.next())
				throw new ParentDaoException("Wrong credentials");
			
			Student son = new StudentDaoDatabase(conn).getStudentById(rs.getInt("id_student"));

			return new Parent(rs.getInt("id_parent"), rs.getString("name"), rs.getString("surname"), son);
		} catch (SQLException e) {
			throw new ParentDaoException("Database error while fetching parent credential");
		}
	}


}