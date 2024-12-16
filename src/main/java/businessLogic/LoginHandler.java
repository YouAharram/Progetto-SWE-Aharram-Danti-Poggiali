package businessLogic;

import abstractOrm.DaoFactory;
import exceptions.DaoConnectionException;

public abstract class LoginHandler {

	private LoginHandler next;

	public LoginHandler(LoginHandler next) {
		this.next = next;
	}

	public void validationCredentials(String username, String password, DaoFactory daoFacotry, InterfaceCreator interfaceCreator) throws DaoConnectionException, IllegalCredentialsException{
		if (next != null) {
			next.validationCredentials(username, password, daoFacotry, interfaceCreator);
			return;
		}
		throw new IllegalCredentialsException("Invalid username or password");
	}
	
	public class IllegalCredentialsException extends Exception{
		
		public IllegalCredentialsException(String message) {
			super(message);
		}

		private static final long serialVersionUID = 1L;
	}
}