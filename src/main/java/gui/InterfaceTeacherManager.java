package gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.TeacherDaoException;
import DaoExceptions.TeachingAssignmentDaoException;
import businessLogic.TeacherController;
import domainModel.TeachingAssignment;

public class InterfaceTeacherManager {

	private static TeacherController teacherController;

	@FXML
	private Button btnLesson;
	@FXML
	private Label lblTeacherName;
	@FXML
	private ChoiceBox<String> cbTeachings;
	@FXML
	private ChoiceBox<String> cbClasses;
	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	public void initialize() {
		populateTeachings();
		populateClass();
	}

	private void populateTeachings() {
		if (teacherController != null) {
			Iterator<TeachingAssignment> teachings = null;
			try {
				teachings = teacherController.getAllMyTeachings();
			} catch (TeachingAssignmentDaoException | TeacherDaoException | DaoConnectionException e) {
				e.printStackTrace();
			}
			while (teachings.hasNext()) {
				cbTeachings.getItems().add(teachings.next().getSubject());
			}
		}
	}
	
	private void populateClass() {
		if (teacherController != null) {
			Iterator<TeachingAssignment> classes = null;
			try {
				classes = teacherController.getAllMyTeachings();
			} catch (TeachingAssignmentDaoException | TeacherDaoException | DaoConnectionException e) {
				e.printStackTrace();
			}
			while (classes.hasNext()) {
				cbClasses.getItems().add(classes.next().getSchoolClass().getClassName());
			}
		}
	}

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

	public static void setController(TeacherController teacherController) {
		InterfaceTeacherManager.teacherController = teacherController;
	}
}