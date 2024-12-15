package businessLogic;

import abstractOrm.DaoFactory;
import domainModel.Parent;
import exceptions.DaoConnectionException;
import exceptions.ParentDaoException;
import exceptions.StudentDaoException;

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