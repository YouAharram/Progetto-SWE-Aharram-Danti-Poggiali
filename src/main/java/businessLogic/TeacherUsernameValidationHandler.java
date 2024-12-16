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
	public void validationCredentials(String username, String password, DaoFactory daoFactory, InterfaceCreator interfaceCreator) throws DaoConnectionException, IllegalCredentialsException {
		try {
			Teacher teacher = daoFactory.creatTeacherDao().getTeacherByUsernameAndPassword(username, password);
			interfaceCreator.createTeacherInterface(new TeacherController(teacher, daoFactory));
		} catch (TeacherDaoException e) {
			super.validationCredentials(username, password, daoFactory, interfaceCreator);
		}
	}

}