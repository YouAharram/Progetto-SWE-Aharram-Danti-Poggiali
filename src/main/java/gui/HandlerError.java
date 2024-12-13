package gui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

class HandlerError {
	
	static void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
	
}
