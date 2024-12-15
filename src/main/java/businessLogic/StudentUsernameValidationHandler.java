package businessLogic;

import abstractOrm.DaoFactory;
import domainModel.Student;
import exceptions.DaoConnectionException;
import exceptions.StudentDaoException;

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