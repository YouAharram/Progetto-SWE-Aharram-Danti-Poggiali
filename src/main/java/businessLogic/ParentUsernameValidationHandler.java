package businessLogic;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.ParentDaoException;
import DaoExceptions.StudentDaoException;
import DaoExceptions.TeacherDaoException;
import daoFactory.DaoFactory;
import domainModel.Parent;

public class ParentUsernameValidationHandler implements LoginHandler {

	private LoginHandler nextChain;
	private DaoFactory daoFactory;

	public ParentUsernameValidationHandler(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public void setNextChain(LoginHandler nextChain) {
		this.nextChain = nextChain;
	}
	
	@Override
	public boolean validationCredentials(String username, String password) throws ParentDaoException, StudentDaoException, TeacherDaoException, DaoConnectionException {
		if(username != null && username.charAt(0)=='P') {
			System.out.println("Parent sta cercando di fare il login");
			Parent pareny = daoFactory.createParentDao().getParentByUsernameWithPassword(username, password);
		}else if(nextChain != null) {
			nextChain.validationCredentials(username, password);
		}
		return false;		
	}
}