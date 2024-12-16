package gui;

import java.io.IOException;

import businessLogic.InterfaceCreator;
import businessLogic.ParentController;
import businessLogic.StudentController;
import businessLogic.TeacherController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiInterfaceCreator implements InterfaceCreator {
	
	private static Stage stage;
	private Parent root;

	@Override
	public void createStudentInterface(StudentController studentController) {
		InterfaceStudentManager.setController(studentController);
		try {
			root = FXMLLoader.load(getClass().getResource("../resources/StudentInterface.fxml"));
		} catch (IOException e) {
			HandlerError.showError(e.getMessage());
		}
		showStage();
	}

	@Override
	public void createTeacherInterface(TeacherController teacherController) {
		InterfaceTeacherManager.setController(teacherController);
		try {
			root = FXMLLoader.load(getClass().getResource("../resources/TeacherInterface.fxml"));
		} catch (IOException e) {
			HandlerError.showError(e.getMessage());
		}
		showStage();
	}

	@Override
	public void createParentInterface(ParentController parentController) {
		InterfaceParentManager.setController(parentController);
		try {
			root = FXMLLoader.load(getClass().getResource("../resources/ParentInterface.fxml"));
		} catch (IOException e) {
			HandlerError.showError(e.getMessage());
		}
		showStage();
	}

	public static void setStage(Stage stage) {
		GuiInterfaceCreator.stage = stage;
	}

	private void showStage() {
		if (root != null) {
			stage.setScene(new Scene(root));
			stage.show();
		}
	}
}
