package gui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import businessLogic.StudentController;
import domainModel.Absence;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AbsenceStudentSceneController {

    @FXML
    private Button back;

    @FXML
    private Button showAllButton;

    @FXML
    private Button showByDateButton;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableView<Absence> absenceTableView;

    @FXML
    private TableColumn<Absence, String> dateColumn;

    @FXML
    private TableColumn<Absence, String> statusColumn;

    @FXML
    private Label selectDateLabel;
    
    @FXML
    private Label attendanceStatusLabel;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private static StudentController studentController;

    public void switchToStudentScene() throws IOException {
        root = FXMLLoader.load(getClass().getResource("../resources/StudentInterface.fxml"));
        stage = (Stage) back.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void setController(StudentController studentController) {
        AbsenceStudentSceneController.studentController = studentController;
    }

    @FXML
    public void showAllAbsences() {
        try {
            Iterator<Absence> absencesIterator = studentController.getAllStudentAbsences();
            List<Absence> absences = new ArrayList<>();
            absencesIterator.forEachRemaining(absences::add);
            ObservableList<Absence> absencesList = FXCollections.observableArrayList(absences);
            absenceTableView.setItems(absencesList);

            dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
            statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty("Absent"));
        } catch (Exception e) {
            showError("Error retrieving absences: " + e.getMessage());
        }
    }

    @FXML
    public void showAbsenceByDate() {
        if (datePicker.getValue() == null) {
            showError("Please select a date.");
            return;
        }

        LocalDate selectedDate = datePicker.getValue();

        try {
            // Ottieni il risultato (assente o presente)
            boolean isAbsent = studentController.checkStudentAttendanceInDay(selectedDate);
            String statusText = isAbsent ? "Absent" : "Present";

            // Mostra il risultato nella label
            attendanceStatusLabel.setText("Date: " + selectedDate.toString() + " - " + statusText);
        } catch (Exception e) {
            showError("Error checking attendance: " + e.getMessage());
        }
    }


    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
