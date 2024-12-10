package businessLogic;

import orm.MockUserDao;

public class LoginController {
	
	private LoginHandler teachHandler;
	MockUserDao dao = new MockUserDao();

	public LoginController(LoginHandler teachHandler) {
		this.teachHandler = teachHandler;
	}
	

	public boolean login(String username, String password) {
		return teachHandler.validationCredentials(username, password);
	}
}