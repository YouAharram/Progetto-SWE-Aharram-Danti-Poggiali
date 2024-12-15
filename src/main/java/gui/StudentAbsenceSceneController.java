package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import DaoExceptions.AbsenceDaoException;
import DaoExceptions.DaoConnectionException;
import DaoExceptions.StudentDaoException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class StudentAbsenceSceneController {

    @FXML
    private Button back;

    @FXML
    private TableView<Absence> absenceTableView;

    @FXML
    private TableColumn<Absence, String> dateColumn;

    @FXML
    private TableColumn<Absence, String> statusColumn;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private static StudentController studentController;

    @FXML
    public void initialize() {
    	Iterator<Absence> absencesIterator = null;
		try {
			absencesIterator = studentController.getAllStudentAbsences();
		} catch (AbsenceDaoException | StudentDaoException | DaoConnectionException e) {
			HandlerError.showError(e.getMessage());
		}
		
        List<Absence> absences = new ArrayList<>();
        absencesIterator.forEachRemaining(absences::add);
        ObservableList<Absence> absencesList = FXCollections.observableArrayList(absences);
        absenceTableView.setItems(absencesList);

        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue().isJustified()) ? "Justified" : "Not justified"));
    }
    
    public void switchToStudentScene() throws IOException {
        root = FXMLLoader.load(getClass().getResource("../resources/StudentInterface.fxml"));
        stage = (Stage) back.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void setController(StudentController studentController) {
        StudentAbsenceSceneController.studentController = studentController;
    }
}
