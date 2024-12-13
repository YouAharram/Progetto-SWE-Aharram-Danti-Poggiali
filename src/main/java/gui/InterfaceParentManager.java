package gui;

import java.awt.event.ActionEvent;
import java.io.IOException;

import businessLogic.ParentController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class InterfaceParentManager {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private static ParentController parentController;

	@FXML
	private Button meetingsButton;
	
	@FXML
	private Label nameParent;
	
	@FXML
	private Label nameStudent;
	
	@FXML
	private Label nameClass;

	
	@FXML
	private Button homeworksLessonsButton;
	
	@FXML
	private Button reportsButton;
	
	@FXML
	private Button absencesButton;
	
	@FXML
	private Button gradesButton;
	
	
	public void initialize() {
		if (parentController != null) {
        	setControllerForAllScenes();
        	domainModel.Parent parent = parentController.getParent();
            String nameParentString = parent.getName() + " " + parent.getSurname();
            String nameStudentString = parent.getStudent().getName() + " " + parent.getStudent().getSurname();
            String nameClassString = parent.getStudent().getSchoolClass().getClassName();
            nameParent.setText(nameParentString);
            nameStudent.setText(nameStudentString);
            nameClass.setText(nameClassString);
        } else {
            System.out.println("ParentController non è stato inizializzato.");
        }
		
	}

	private void setControllerForAllScenes() {
		ParentMeetingSceneController.setParentController(parentController);
	}
	
	
	public void switchToMeetingsScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../resources/ParentMeetingScene.fxml"));
		stage = (Stage) meetingsButton.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public static void setController(ParentController parentController) {
		InterfaceParentManager.parentController = parentController;

	}

}
