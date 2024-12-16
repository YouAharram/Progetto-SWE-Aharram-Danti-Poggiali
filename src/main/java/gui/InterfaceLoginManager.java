package gui;


import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.io.IOException;
import businessLogic.LoginController;
import businessLogic.LoginHandler.IllegalCredentialsException;
import exceptions.DaoConnectionException;

public class InterfaceLoginManager {
	
	@FXML
	private TextField txtUsername;
	
	@FXML
	private TextField txtPassword;
	
	private static LoginController loginController;
	
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