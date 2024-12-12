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
    private Label nameLabel; // Collegato al fx:id "nameLabel" nel file FXML
    @FXML
    private Label surnameLabel; // Collegato al fx:id "surnameLabel" nel file FXML
	
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
            // Ottieni nome e cognome dallo StudentController
            String name = studentController.getStudent().getName();
            String surname = studentController.getStudent().getSurname();

            // Popola le etichette
            nameLabel.setText(name);
            surnameLabel.setText(surname);
        } else {
            System.out.println("StudentController non è stato inizializzato.");
        }
    }


	public void switchToGradesScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../resources/GradesSceneStudent.fxml"));
		stage = (Stage) gradesButton.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		showGrades();
	}

	
	
	public void showGrades() {
		// TODO
	}
	
	public void switchToHomeworkAndLessonScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../resources/HomeworkAndLessonSceneStudent.fxml"));
		stage = (Stage) homeworkAndLessonButton.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		showGrades();
	}
	
	public void switchToDisciplinaryReportScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../resources/DisciplinaryReportSceneStudent.fxml"));
		stage = (Stage) disciplinaryReportButton.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		showGrades();
	}
	
	public void switchToAbsenceScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../resources/AbsenceSceneStudent.fxml"));
		stage = (Stage) absenceButton.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		showGrades();
	}
	
	public void switchToAvarageGradeScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../resources/AvarageGradeSceneStudent.fxml"));
		stage = (Stage) avarageGradeButton.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		showGrades();
	}
	
	public static void setController(StudentController studentController) {
		StudentSceneManager.studentController = studentController;
	}

}
