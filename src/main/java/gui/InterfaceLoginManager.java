package gui;

import javafx.scene.Parent; 
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import businessLogic.LoginController;
import businessLogic.ParentController;
import businessLogic.StudentController;
import businessLogic.TeacherController;
import businessLogic.UserController;

public class InterfaceLoginManager {

	private Stage stage;
	private Scene scene;
	private Parent root;
	
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

	
	public void login() throws IOException{
		String username = txtUsername.getText();
		String password = txtPassword.getText();
		
		try {
			UserController controller = loginController.login(username, password);
			
			if(controller instanceof StudentController) {
				StudentSceneManager.setController((StudentController) controller);
				root = FXMLLoader.load(getClass().getResource("../resources/StudentInterface.fxml"));
				stage = (Stage) txtUsername.getScene().getWindow();
			}
			
			if(controller instanceof TeacherController) {
				InterfaceTeacherManager.setController((TeacherController) controller);
				root = FXMLLoader.load(getClass().getResource("../resources/TeacherInterface.fxml"));
				stage = (Stage) txtUsername.getScene().getWindow();
			}
			
			if(controller instanceof ParentController) {
				InterfaceParentManager.setController((ParentController) controller);
				root = FXMLLoader.load(getClass().getResource("../resources/ParentInterface.fxml"));
				stage = (Stage) txtUsername.getScene().getWindow();
			}
			
		} catch (Exception e) {
			HandlerError.showError(e.getMessage());
		}

		if (root != null) {
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}
	
	public static void setLoginController(LoginController loginController) {
		InterfaceLoginManager.loginController = loginController;
	}
	
}