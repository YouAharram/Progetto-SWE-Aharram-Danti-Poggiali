package businessLogic;

public interface LoginHandler {
	
	void setNextChain(LoginHandler nextChain);
	boolean validationCredentials(String username, String password);
}