package gui;

import javafx.scene.Parent;  
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.ParentDaoException;
import DaoExceptions.StudentDaoException;
import DaoExceptions.TeacherDaoException;
import businessLogic.LoginController;
import businessLogic.LoginHandler;
import businessLogic.ParentUsernameValidationHandler;
import businessLogic.StudentController;
import businessLogic.StudentUsernameValidationHandler;
import businessLogic.TeacherUsernameValidationHandler;
import daoFactory.DaoFactory;
import daoFactory.DatabaseDaoFactory;
import domainModel.SchoolClass;
import domainModel.Student;

public class InterfaceLoginManager {
	@FXML
	private TextField txtUsername;
	private Stage stage;
	private Scene scene;
	private Parent root;

//	@FXML
//	private TextField txtPassword;

	private DaoFactory daoFactory = new DatabaseDaoFactory();
	private Scene sceneManager;
	
//    public void setMainController(SceneController mainsceneManager) {
//		this.sceneManager = sceneManager;
//    }
	
	public static String hashString(String input) {
		try {
			// Crea un'istanza di MessageDigest per l'algoritmo SHA-256
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			// Calcola l'hash della stringa in input
			byte[] hashBytes = digest.digest(input.getBytes());

			// Converti l'hash in formato esadecimale
			StringBuilder hexString = new StringBuilder();
			for (byte b : hashBytes) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) {
					hexString.append('0'); // Aggiungi uno zero iniziale se necessario
				}
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Hash algoritm not found", e);
		}
	}

	
	public void login() throws IOException, StudentDaoException, DaoConnectionException{
		String username = txtUsername.getText();
		
		if(username.charAt(0) == 'T') {
			root = FXMLLoader.load(getClass().getResource("../resources/TeacherInterface.fxml"));
			stage = (Stage) txtUsername.getScene().getWindow();
		}
		if(username.charAt(0) == 'S') {
			StudentController studentController = new StudentController(new Student(1, "mario", "gino", new SchoolClass("1A")), daoFactory);
			StudentSceneManager.setController(studentController);
			root = FXMLLoader.load(getClass().getResource("../resources/StudentInterface.fxml"));
			stage = (Stage) txtUsername.getScene().getWindow();
		}
		if(username.charAt(0) == 'P') {
			
		}
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
//		String password = txtPassword.getText();
		
//		LoginHandler teacherHandler = new TeacherUsernameValidationHandler(daoFactory);
//		LoginHandler studentHandler = new StudentUsernameValidationHandler(daoFactory);
//		LoginHandler parentHandler = new ParentUsernameValidationHandler(daoFactory);
//		teacherHandler.setNextChain(studentHandler);
//		studentHandler.setNextChain(parentHandler);
//		
//		LoginController loginController = new LoginController(teacherHandler);
//		if (loginController.login(username, password)) {
//			try {
				// Carica la nuova scena
//				Parent loader = FXMLLoader.load(getClass().getResource("../resources/InterfacciaTeacher.fxml"));
//				Scene secondaryScene = new Scene(loader);
//				Stage newWindow = new Stage();
//				newWindow.setTitd.getText();
//				le("Teacher...");
//				newWindow.setScene(secondaryScene);

				// Ottieni lo stage corrente e chiudilo
//				Stage currentStage = (Stage) txtUsername.getScene().getWindow();
//				Stage currentStage =  (Stage)((Node) event.getSource()).getScene().getWindow();
//				sceneManager.switchToTeacherScene(currentStage);
//				currentStage.close();
				// Mostra la nuova finestra
//				newWindow.show();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
	}
}