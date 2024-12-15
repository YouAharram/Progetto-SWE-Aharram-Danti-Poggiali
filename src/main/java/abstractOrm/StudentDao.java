package abstractOrm;

import java.util.Iterator;

import domainModel.SchoolClass;
import domainModel.Student;
import exceptions.DaoConnectionException;
import exceptions.SchoolClassDaoException;
import exceptions.StudentDaoException;

public interface StudentDao {
		
	Iterator<Student> getStudentsByClass(SchoolClass schoolClass) throws StudentDaoException, DaoConnectionException, SchoolClassDaoException;

	Student getStudentByUsernameAndPassword(String username, String password) throws StudentDaoException;

}
