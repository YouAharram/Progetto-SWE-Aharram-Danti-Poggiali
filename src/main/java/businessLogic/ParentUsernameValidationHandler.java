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
	public void validationCredentials(String username, String password, DaoFactory daoFactory, InterfaceCreator interfaceCreator) throws DaoConnectionException, IllegalCredentialsException {
		try {
			Parent parent = daoFactory.createParentDao().getParentByUsernameWithPassword(username, password);
			interfaceCreator.createParentInterface(new ParentController(parent, daoFactory));
		} catch (ParentDaoException | StudentDaoException e) {
			super.validationCredentials(username, password, daoFactory, interfaceCreator);
		}
	}

}