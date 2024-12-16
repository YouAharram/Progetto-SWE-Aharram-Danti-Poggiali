package gui;

import java.io.IOException;
import businessLogic.ParentController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
	
	@FXML
	private Button averageButton;
	
	
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
            HandlerError.showError("Parent controller not yet initialized");
        }
		
	}

	private void setControllerForAllScenes() {
		ParentMeetingSceneController.setController(parentController);
		ParentAverageSceneController.setController(parentController);
		ParentAbsencesSceneController.setController(parentController);
		ParentHomeworksLessonsSceneController.setController(parentController);
		ParentGradesSceneController.setController(parentController);
		ParentDisciplinaryReportsSceneController.setController(parentController);
	}
	
	
	public void switchToMeetingsScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../ParentMeetingScene.fxml"));
		stage = (Stage) meetingsButton.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToHomeworksLessonsScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../ParentHomeworksLessonsScene.fxml"));
		stage = (Stage) homeworksLessonsButton.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToAverageScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../ParentAverageScene.fxml"));
		stage = (Stage) averageButton.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToGradesScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../ParentGradesScene.fxml"));
		stage = (Stage) gradesButton.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToReportsScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../ParentDisciplinaryReportsScene.fxml"));
		stage = (Stage) reportsButton.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToAbsencesScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../ParentAbsencesScene.fxml"));
		stage = (Stage) absencesButton.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public static void setController(ParentController parentController) {
		InterfaceParentManager.parentController = parentController;

	}

}
