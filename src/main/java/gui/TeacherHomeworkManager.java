package gui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import businessLogic.TeacherController;
import businessLogic.TeacherController.IllegalHomeworkAccessException;
import domainModel.Homework;
import domainModel.TeachingAssignment;
import exceptions.DaoConnectionException;
import exceptions.HomeworkDaoException;
import exceptions.SchoolClassDaoException;
import exceptions.TeachingAssignmentDaoException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class TeacherHomeworkManager {
	@FXML
	private Button btnBack;
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Homework homeworkSelected;

	@FXML
	private DatePicker datePicker;

	List<Homework> homeworks = new ArrayList<>();
	private static TeacherController teacherController;

	@FXML
	private TextArea taDescription;

	@FXML
	private TableView<Homework> homeworkTableView;

	@FXML
	private TableColumn<Homework, String> subjectHomeworkColumn;

	@FXML
	private TableColumn<Homework, String> teacherHomeworkColumn;

	@FXML
	private TableColumn<Homework, String> descriptionHomeworkColumn;
	private static TeachingAssignment teachingAssignment;

	public void backAtTeacherScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../TeacherInterface.fxml"));
		stage = (Stage) btnBack.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void showHomework() { 
		homeworkTableView.getItems().clear();

		if (datePicker.getValue() == null) {
			return;
		}

		LocalDate selectedDate = datePicker.getValue();

		try {
			Iterator<Homework> homeworkIterator = teacherController.getClassHomeworksSubmissionDate(selectedDate,
					teachingAssignment.getSchoolClass());

			ObservableList<Homework> homeworksList = FXCollections.observableArrayList();
			homeworkIterator.forEachRemaining(homeworksList::add);

			homeworkTableView.setItems(homeworksList);

			subjectHomeworkColumn.setCellValueFactory(
					cellData -> new SimpleStringProperty(cellData.getValue().getTeaching().getSubject()));
			teacherHomeworkColumn.setCellValueFactory(
					cellData -> new SimpleStringProperty(cellData.getValue().getTeaching().getTeacher().getSurname()
							+ " " + cellData.getValue().getTeaching().getTeacher().getName()));
			descriptionHomeworkColumn
					.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

		} catch (DaoConnectionException | SchoolClassDaoException | HomeworkDaoException e) {
			HandlerError.showError(e.getMessage());
		}
	}

	public void addHomework() throws HomeworkDaoException, TeachingAssignmentDaoException, DaoConnectionException {
		if (datePicker.getValue() == null) {
			return;
		}
		teacherController.assignNewHomework(teachingAssignment, datePicker.getValue(), taDescription.getText(),
				datePicker.getValue());

	}

	public void deleteHomeWork() throws IllegalHomeworkAccessException, HomeworkDaoException, DaoConnectionException {
		if (homeworkSelected == null) {
			return;
		}
		teacherController.deleteHomework(homeworkSelected);

	}

	public void editHomework() throws HomeworkDaoException, IllegalHomeworkAccessException, DaoConnectionException {
		if (homeworkSelected == null) {
			return;
		}
		teacherController.editHomeworkDescription(homeworkSelected, taDescription.getText());
		teacherController.editHomeworkSubmissionDate(homeworkSelected, datePicker.getValue());
	}

	public void itemSelected() {
		homeworkSelected = homeworkTableView.getSelectionModel().getSelectedItem();
		taDescription.setText(homeworkSelected.getDescription());
		datePicker.setValue(homeworkSelected.getSubmissionDate());
	}

	protected static void setTeachingsAssignement(TeachingAssignment teachingAssignment) {
		TeacherHomeworkManager.teachingAssignment = teachingAssignment;
	}

	public static void setController(TeacherController teacherController) {
		TeacherHomeworkManager.teacherController = teacherController;
	}
}