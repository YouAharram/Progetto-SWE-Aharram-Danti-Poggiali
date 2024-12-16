package gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import businessLogic.ParentController;
import domainModel.Absence;
import exceptions.AbsenceDaoException;
import exceptions.DaoConnectionException;
import exceptions.StudentDaoException;

public class ParentAbsencesSceneController {

	@FXML
	private Button back;

	@FXML
	private TableView<Absence> absenceTableView;

	@FXML
	private TableColumn<Absence, String> dateColumn;

	@FXML
	private TableColumn<Absence, String> statusColumn;

	@FXML
	private TableColumn<Absence, Void> justifyColumn;

	private Stage stage;
	private Scene scene;
	private Parent root;
	private static ParentController parentController;

	@FXML
	public void initialize() {
		loadAbsences();
	}

	private void loadAbsences() {
		Iterator<Absence> absencesIterator = null;
	
		try {
			absencesIterator = parentController.getAllStudentAbsences();
		} catch (AbsenceDaoException | DaoConnectionException | StudentDaoException e) {
			HandlerError.showError(e.getMessage());
		}
		

		List<Absence> absences = new ArrayList<>();
		absencesIterator.forEachRemaining(absences::add);
		ObservableList<Absence> absencesList = FXCollections.observableArrayList(absences);
		absenceTableView.setItems(absencesList);

		dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
		statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
				(cellData.getValue().isJustified()) ? "Justified" : "Not justified"));

		addJustifyButtonToTable();
	}

	
	private void addJustifyButtonToTable() {
	    Callback<TableColumn<Absence, Void>, TableCell<Absence, Void>> cellFactory = new Callback<>() {
	        @Override
	        public TableCell<Absence, Void> call(final TableColumn<Absence, Void> param) {
	            return new TableCell<>() {

	                private final Button btn = new Button("Justify");

	                {
	                    btn.setOnAction(event -> {
	                        Absence data = getTableView().getItems().get(getIndex());
	                        justifyAbsence(data);
	                    });
	                }

	                @Override
	                protected void updateItem(Void item, boolean empty) {
	                    super.updateItem(item, empty);
	                    if (empty) {
	                        setGraphic(null);
	                    } else {
	                        Absence absence = getTableView().getItems().get(getIndex());
	                        if (absence.isJustified()) {
	                            btn.setDisable(true);
	                        } else {
	                            btn.setDisable(false);
	                        }
	                        setGraphic(btn);
	                    }
	                }
	            };
	        }
	    };
	    justifyColumn.setCellFactory(cellFactory);
	}

	

	private void justifyAbsence(Absence absence) {
		loadAbsences();
		try {
			parentController.justifyAbsence(absence);
		} catch (AbsenceDaoException | DaoConnectionException e) {
			HandlerError.showError(e.getMessage());
		}

	}

	public void switchToParentScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../resources/ParentInterface.fxml"));
		stage = (Stage) back.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public static void setController(ParentController parentController) {
		ParentAbsencesSceneController.parentController = parentController;
	}
}
