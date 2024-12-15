package businessLogic;

import abstractOrm.DaoFactory;
import domainModel.Teacher;
import exceptions.DaoConnectionException;
import exceptions.TeacherDaoException;

public class TeacherUsernameValidationHandler extends LoginHandler {

	public TeacherUsernameValidationHandler(LoginHandler next) {
		super(next);
	}

	@Override
	public UserController validationCredentials(String username, String password, DaoFactory daoFactory) throws DaoConnectionException, IllegalCredentialsException {
		try {
			Teacher teacher = daoFactory.creatTeacherDao().getTeacherByUsernameAndPassword(username, password);
			return new TeacherController(teacher, daoFactory);
		} catch (TeacherDaoException e) {
			return super.validationCredentials(username, password, daoFactory);
		}
	}

}