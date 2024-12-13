package gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.GradeDaoException;
import DaoExceptions.StudentDaoException;
import DaoExceptions.TeacherDaoException;
import DaoExceptions.TeachingAssignmentDaoException;
import businessLogic.TeacherController;
import domainModel.Grade;
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
	private int idTeaching;

	@FXML
	public void initialize() {
		populateTeachings();
		
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
			List<TeachingAssignment> classes = null;
			try {
				Iterator<TeachingAssignment> teachings = null;
				String subject = cbTeachings.getValue();
				teachings = teacherController.getAllMyTeachings();
			classes = StreamSupport.stream(
					    Spliterators.spliteratorUnknownSize(teachings, Spliterator.ORDERED), false)
					    .filter(t -> t.getSubject().equals(subject))
					    .collect(Collectors.toList());
			} catch (TeachingAssignmentDaoException | TeacherDaoException | DaoConnectionException e) {
				e.printStackTrace();
			}
//			while (classes.hasNext()) {
//				cbClasses.getItems().add(classes.next().getSchoolClass().getClassName());
//			}
			for (TeachingAssignment ta : classes) {
			    // Fai qualcosa con teaching
				System.out.println(ta.getSchoolClass().getClassName());
				cbClasses.getItems().add(ta.getSchoolClass().getClassName());
			}
		}
	}
	
	private void showGrades() {
//		idTeaching = cbClasses.get  
		 
	}
	
	private void elementChoosed() {
		System.out.println("funge");
		populateClass();
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