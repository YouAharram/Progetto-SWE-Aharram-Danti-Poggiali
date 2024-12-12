package gui;

import java.io.IOException;

import businessLogic.StudentController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AbsenceStudentSceneController {
	@FXML
	private Button back;
	private Stage stage;
	private Scene scene;
	private Parent root;
	private static StudentController studentController;

	public void switchToStudentScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../resources/StudentInterface.fxml"));
		stage = (Stage) back.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public static void setController(StudentController studentController) {
		AbsenceStudentSceneController.studentController = studentController;
	}

}