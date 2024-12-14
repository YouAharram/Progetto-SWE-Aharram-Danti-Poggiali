package businessLogic;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.StudentDaoException;
import daoFactory.DaoFactory;
import domainModel.Student;

public class StudentUsernameValidationHandler extends LoginHandler {

	public StudentUsernameValidationHandler(LoginHandler next) {
		super(next);
	}

	@Override
	public UserController validationCredentials(String username, String password, DaoFactory daoFactory) throws DaoConnectionException, IllegalCredentialsException {
		try {
			Student student = daoFactory.createStudentDao().getStudentByUsernameAndPassword(username, password);
			return new StudentController(student, daoFactory);
		} catch (StudentDaoException e) {
			return super.validationCredentials(username, password, daoFactory);
		}
	}

}