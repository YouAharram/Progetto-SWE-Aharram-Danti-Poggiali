package businessLogic;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.ParentDaoException;
import DaoExceptions.StudentDaoException;
import daoFactory.DaoFactory;
import domainModel.Parent;

public class ParentUsernameValidationHandler extends LoginHandler {

	public ParentUsernameValidationHandler(LoginHandler next) {
		super(next);
	}

	@Override
	public UserController validationCredentials(String username, String password, DaoFactory daoFactory) throws DaoConnectionException, IllegalCredentialsException {
		try {
			Parent parent = daoFactory.createParentDao().getParentByUsernameWithPassword(username, password);
			return new ParentController(parent, daoFactory);
		} catch (ParentDaoException | StudentDaoException e) {
			return super.validationCredentials(username, password, daoFactory);
		}
	}

}