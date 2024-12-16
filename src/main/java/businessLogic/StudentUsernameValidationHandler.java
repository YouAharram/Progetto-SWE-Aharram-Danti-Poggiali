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
	public void validationCredentials(String username, String password, DaoFactory daoFactory, InterfaceCreator interfaceCreator) throws DaoConnectionException, IllegalCredentialsException {
		try {
			Student student = daoFactory.createStudentDao().getStudentByUsernameAndPassword(username, password);
			interfaceCreator.createStudentInterface(new StudentController(student, daoFactory));
		} catch (StudentDaoException e) {
			super.validationCredentials(username, password, daoFactory, interfaceCreator);
		}
	}

}