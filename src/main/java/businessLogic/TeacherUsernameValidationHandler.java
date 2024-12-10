package businessLogic;

public class TeacherUsernameValidationHandler implements LoginHandler{
	private LoginHandler nextChain;

	@Override
	public void setNextChain(LoginHandler nextChain) {
		this.nextChain = nextChain;
	}

	@Override
	public boolean validationCredentials(String username, String password) {
		if(username != null && username.charAt(0)=='T') {
			System.out.println("Teacher sta cercando di fare il login");
		}else if(nextChain != null) {
			nextChain.validationCredentials(username, password);
		}
		return false;
	}
}