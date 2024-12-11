package businessLogic;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.ParentDaoException;
import DaoExceptions.StudentDaoException;
import DaoExceptions.TeacherDaoException;
import daoFactory.DaoFactory;
import domainModel.Student;

public class StudentUsernameValidationHandler implements LoginHandler {

	private LoginHandler nextChain;
	private DaoFactory daoFactory;

	public StudentUsernameValidationHandler(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public void setNextChain(LoginHandler nextChain) {
		this.nextChain = nextChain;
	}
	@Override
	public boolean validationCredentials(String username, String password) throws StudentDaoException, DaoConnectionException, TeacherDaoException, ParentDaoException {
		if(username != null && username.charAt(0)=='S') {
			System.out.println("Student sta cercando di fare il login");
			Student student = daoFactory.createStudentDao().getStudentByUsernameAndPassword(username, password);
		}else if(nextChain != null) {
			nextChain.validationCredentials(username,password);
		}
		return false;		
	}
}