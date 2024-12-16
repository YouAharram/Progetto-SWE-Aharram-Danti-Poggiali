package businessLogic;

import abstractOrm.DaoFactory;
import businessLogic.LoginHandler.IllegalCredentialsException;
import exceptions.DaoConnectionException;

public final class LoginController {

	private LoginHandler loginHandler;
	private DaoFactory daoFacotry;
	private InterfaceCreator interfaceCreator;

	public LoginController(LoginHandler loginHandler, DaoFactory daoFacotry, InterfaceCreator interfaceCreator) {
		this.loginHandler = loginHandler;
		this.daoFacotry = daoFacotry;
		this.interfaceCreator = interfaceCreator;
	}
	
	public void login(String username, String password) throws DaoConnectionException, IllegalCredentialsException {
		loginHandler.validationCredentials(username, password, daoFacotry, interfaceCreator);
	}
}