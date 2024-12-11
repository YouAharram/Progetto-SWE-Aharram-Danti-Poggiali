package businessLogic;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.ParentDaoException;
import DaoExceptions.StudentDaoException;
import DaoExceptions.TeacherDaoException;
import daoFactory.DaoFactory;
import domainModel.Teacher;

public class TeacherUsernameValidationHandler implements LoginHandler{
	private LoginHandler nextChain;
	private DaoFactory daoFactory;
	
	public TeacherUsernameValidationHandler(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void setNextChain(LoginHandler nextChain) {
		this.nextChain = nextChain;
	}
	@Override
	public boolean validationCredentials(String username, String password) throws TeacherDaoException, DaoConnectionException, StudentDaoException, ParentDaoException {
		if(username != null && username.charAt(0)=='T') {
			System.out.println("Teacher sta cercando di fare il login");
			Teacher teacher = daoFactory.creatTeacherDao().getTeacherByUsernameAndPassword(username, password);
			return true;
		}else if(nextChain != null) {
			nextChain.validationCredentials(username, password);
		}
		return false;
	}
}