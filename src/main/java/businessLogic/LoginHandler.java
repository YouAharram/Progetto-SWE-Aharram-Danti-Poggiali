package businessLogic;

import abstractOrm.DaoFactory;
import exceptions.DaoConnectionException;

public abstract class LoginHandler {

	private LoginHandler next;

	public LoginHandler(LoginHandler next) {
		this.next = next;
	}

	public UserController validationCredentials(String username, String password, DaoFactory daoFacotry) throws DaoConnectionException, IllegalCredentialsException{
		if (next != null) {
			return next.validationCredentials(username, password, daoFacotry);
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