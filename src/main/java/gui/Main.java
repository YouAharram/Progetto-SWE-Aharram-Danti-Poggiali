package gui;

import java.io.IOException;

import businessLogic.LoginController;
import businessLogic.LoginHandler;
import businessLogic.ParentUsernameValidationHandler;
import businessLogic.StudentUsernameValidationHandler;
import businessLogic.TeacherUsernameValidationHandler;
import daoFactory.DaoFactory;
import daoFactory.DatabaseDaoFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		DaoFactory daoFacotry = new DatabaseDaoFactory();

		LoginHandler loginHandler = new StudentUsernameValidationHandler(new TeacherUsernameValidationHandler(
				new ParentUsernameValidationHandler(null)));

		LoginController loginController = new LoginController(loginHandler, daoFacotry);
		InterfaceLoginManager.setLoginController(loginController);

		try {
			Parent root = FXMLLoader.load(getClass().getResource("../resources/LoginInterface.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}