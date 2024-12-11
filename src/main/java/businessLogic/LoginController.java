package businessLogic;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.ParentDaoException;
import DaoExceptions.StudentDaoException;
import DaoExceptions.TeacherDaoException;

public final class LoginController {
	
	private LoginHandler userHandler;

	public LoginController(LoginHandler userHandler) {
		this.userHandler = userHandler;
	}

	public boolean login(String username, String password) throws TeacherDaoException, DaoConnectionException, StudentDaoException, ParentDaoException {
		//return userHandler.validationCredentials(username, password);
		return true;
	}
}