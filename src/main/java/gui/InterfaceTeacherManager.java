package gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class InterfaceTeacherManager {
	@FXML
	private Button btnLesson;
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	public void openLesson() throws IOException {
		System.out.println("XSA");
		openWindow("../resources/LessonInterface.fxml", "Lesson");
	}

	@FXML
	public void openHomework() throws IOException {
		openWindow("../resources/HomeworkInterface.fxml", "Homework");
	}
	
	@FXML
	public void openMeeting() throws IOException {
		openWindow("../resources/MeetingInterface.fxml", "Meeting");
	}
	
	private void openWindow(String path, String nameWindow) throws IOException {
		root = FXMLLoader.load(getClass().getResource(path));
		stage = (Stage) btnLesson.getScene().getWindow();
		scene = new Scene(root);
		stage.setTitle(nameWindow);
		stage.setScene(scene);
		stage.show();
	}
}