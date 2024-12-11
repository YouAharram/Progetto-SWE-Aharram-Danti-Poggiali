package businessLogic;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.ParentDaoException;
import DaoExceptions.StudentDaoException;
import DaoExceptions.TeacherDaoException;

public interface LoginHandler {
	
	void setNextChain(LoginHandler nextChain);
	boolean validationCredentials(String username, String password) throws TeacherDaoException, DaoConnectionException, StudentDaoException, ParentDaoException;
}