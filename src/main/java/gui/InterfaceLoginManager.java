package gui;


import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import businessLogic.LoginController;
import businessLogic.LoginHandler.IllegalCredentialsException;
import exceptions.DaoConnectionException;

public class InterfaceLoginManager {
	
	@FXML
	private TextField txtUsername;
	
	@FXML
	private TextField txtPassword;
	
	private static LoginController loginController;
	
	public static String hashString(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			byte[] hashBytes = digest.digest(input.getBytes());

			StringBuilder hexString = new StringBuilder();
			for (byte b : hashBytes) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Hash algoritm not found", e);
		}
	}

	public void login() throws IOException {
		String username = txtUsername.getText();
		String password = txtPassword.getText();
		Stage stage = (Stage) txtUsername.getScene().getWindow();
		GuiInterfaceCreator.setStage(stage);
		try {
			loginController.login(username, password);
		} catch (DaoConnectionException | IllegalCredentialsException e) {
			HandlerError.showError(e.getMessage());
		}
		
	}
	
	public static void setLoginController(LoginController loginController) {
		InterfaceLoginManager.loginController = loginController;
	}
	
}