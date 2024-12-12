package gui;

import java.awt.event.ActionEvent; 
import java.io.IOException;


import businessLogic.StudentController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class StudentSceneManager {
	
	private static StudentController studentController;
	
	@FXML
    private Label nameLabel;
    @FXML
    private Label surnameLabel;
	@FXML
    private Label classLabel;
	@FXML
	private Button gradesButton;
	@FXML
	private Button homeworkAndLessonButton;
	@FXML
	private Node disciplinaryReportButton;
	@FXML
	private Node absenceButton;
	@FXML
	private Node avarageGradeButton;

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void initialize() {
        if (studentController != null) {
        	setControllerForAllScene();
            String name = studentController.getStudent().getName();
            String surname = studentController.getStudent().getSurname();
            String className = studentController.getStudent().getSchoolClass().getClassName();
            nameLabel.setText(name);
            surnameLabel.setText(surname);
            classLabel.setText(className);
        } else {
            System.out.println("StudentController non è stato inizializzato.");
        }
    }


	private void setControllerForAllScene() {
		AbsenceStudentSceneController.setController(studentController);
		AvarageGradeStudentScene.setController(studentController);
		DisciplinaryReportStudentSceneManager.setController(studentController);
		GradesStuentSceneManager.setController(studentController);
		HomeworkLessonSceneManager.setController(studentController);
	}


	public void switchToGradesScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../resources/GradesSceneStudent.fxml"));
		stage = (Stage) gradesButton.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void switchToHomeworkAndLessonScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../resources/HomeworkAndLessonSceneStudent.fxml"));
		stage = (Stage) homeworkAndLessonButton.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToDisciplinaryReportScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../resources/DisciplinaryReportSceneStudent.fxml"));
		stage = (Stage) disciplinaryReportButton.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToAbsenceScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../resources/AbsenceSceneStudent.fxml"));
		stage = (Stage) absenceButton.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToAvarageGradeScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../resources/AvarageGradeSceneStudent.fxml"));
		stage = (Stage) avarageGradeButton.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public static void setController(StudentController studentController) {
		StudentSceneManager.studentController = studentController;
	}

}
