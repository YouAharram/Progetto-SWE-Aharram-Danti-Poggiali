package gui;

import java.io.IOException;
import java.util.Iterator;

import businessLogic.ParentController;
import domainModel.DisciplinaryReport;
import exceptions.DaoConnectionException;
import exceptions.DisciplinaryReportException;
import exceptions.StudentDaoException;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ParentDisciplinaryReportsSceneController {

	private static ParentController parentController;
	
	@FXML
	private Button back;

	@FXML
	private TableView<DisciplinaryReport> tableView;

	@FXML
	private TableColumn<DisciplinaryReport, String> dateColumn;

	@FXML
	private TableColumn<DisciplinaryReport, String> teacherColumn;

	@FXML
	private TableColumn<DisciplinaryReport, String> descriptionColumn;

	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	public void initialize() {
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
		teacherColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
				cellData.getValue().getTeacher().getSurname() + " " + cellData.getValue().getTeacher().getName()));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
		populateTableView();
	}

	public void switchToParentScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../ParentInterface.fxml"));
		stage = (Stage) back.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	private void populateTableView() {
		if (parentController == null) {
			throw new IllegalStateException("StudentController non inizializzato");
		}

		Iterator<DisciplinaryReport> reportsIterator = null;
		try {
			reportsIterator = parentController.getDisciplinaryReports();
		} catch (DisciplinaryReportException | DaoConnectionException | StudentDaoException e) {
			HandlerError.showError(e.getMessage());
		}

		ObservableList<DisciplinaryReport> reportsList = FXCollections.observableArrayList();
		reportsIterator.forEachRemaining(reportsList::add);

		tableView.setItems(reportsList);
	}
	
	public static void setController(ParentController parentController) {
		ParentDisciplinaryReportsSceneController.parentController = parentController;
	}


}
