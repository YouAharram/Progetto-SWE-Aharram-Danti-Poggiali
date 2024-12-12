package gui;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomeworkLessonSceneManager {
	@FXML
	private Button back;
	private Stage stage;
	private Scene scene;
	private Parent root;

	public void switchToStudentScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../resources/StudentInterface.fxml"));
		stage = (Stage) back.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
