package gui;


import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Iterator;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.StudentDaoException;
import DaoExceptions.TeachingAssignmentDaoException;
import businessLogic.StudentController;
import domainModel.TeachingAssignment;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class GradesStuentSceneManager {
	@FXML
    private ChoiceBox<String> subjectsChoiceBox;  // Assumiamo che le materie siano stringhe
	@FXML
	private Button back;
	private Stage stage;
	private Scene scene;
	private Parent root;
	private static StudentController studentController;
	
    @FXML
    public void initialize() {
        populateSubjects();
    }
    
    public void populateSubjects() {
        if (studentController != null) {
            Iterator<TeachingAssignment> subjectsIterator = null;
			try {
				subjectsIterator = studentController.getTeachings();
			} catch (TeachingAssignmentDaoException e) {
				e.printStackTrace();
			} catch (DaoConnectionException e) {
				e.printStackTrace();
			} catch (StudentDaoException e) {
			}
            subjectsChoiceBox.getItems().clear();
            
            while (subjectsIterator.hasNext()) {
                subjectsChoiceBox.getItems().add(subjectsIterator.next().getSubject());  // Aggiungi ogni materia
            }
        }
    }

	public void switchToStudentScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../resources/StudentInterface.fxml"));
		stage = (Stage) back.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public static void setController(StudentController studentController) {
		GradesStuentSceneManager.studentController = studentController;
	}

}