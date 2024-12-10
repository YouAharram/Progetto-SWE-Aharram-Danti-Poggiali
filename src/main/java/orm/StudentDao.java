package orm;

import java.util.Iterator;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.SchoolClassDaoException;
import DaoExceptions.StudentDaoException;
import domainModel.SchoolClass;
import domainModel.Student;

public interface StudentDao {
		
	Iterator<Student> getStudentsByClass(SchoolClass schoolClass) throws StudentDaoException, DaoConnectionException, SchoolClassDaoException;

	Student getStudentByUsernameAndPassword(String username, String password) throws StudentDaoException;

}
