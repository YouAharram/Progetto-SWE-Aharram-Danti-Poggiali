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
		root = FXMLLoader.load(getClass().getResource("../resources/TeacherInterface.fxml"));
		stage = (Stage) btnBack.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void showHomework() throws HomeworkDaoException {
		if (datePicker.getValue() == null) {
			return;
		}

		LocalDate selectedDate = datePicker.getValue();
		Iterator<Homework> homeworkIterator = null;
		try {
			homeworkIterator = teacherController.getClassHomeworksSubmissionDate(selectedDate,
					teachingAssignment.getSchoolClass());
		} catch (DaoConnectionException | SchoolClassDaoException e) {
			e.printStackTrace();
		}

		homeworkIterator.forEachRemaining(homeworks::add);
		ObservableList<Homework> homeworksList = FXCollections.observableArrayList(homeworks);
		homeworkTableView.setItems(homeworksList);

		subjectHomeworkColumn.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getTeaching().getSubject()));
		teacherHomeworkColumn.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getTeaching().getTeacher().getSurname() + " "
						+ cellData.getValue().getTeaching().getTeacher().getName()));
		descriptionHomeworkColumn
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
	}

	public void addHomework() {
		try {
			try {
				teacherController.assignNewHomework(teachingAssignment, datePicker.getValue(), taDescription.getText(),
						datePicker.getValue());
			} catch (HomeworkDaoException e) {
				HandlerError.showError("check arguments");
			}
		} catch (TeachingAssignmentDaoException | DaoConnectionException e) {
			HandlerError.showError("check connection");
		}
	}

	public void deleteHomeWork() {
		try {
			teacherController.deleteHomework(homeworks.get(homeworkTableView.getSelectionModel().getSelectedIndex()));
		} catch (IllegalHomeworkAccessException e) {
			HandlerError.showError("Not your homework");
			e.printStackTrace();
		} catch (HomeworkDaoException | DaoConnectionException e) {
			HandlerError.showError("check connection");
		}
	}

	public void editHomework() {
		Homework homework = homeworks.get(homeworkTableView.getSelectionModel().getSelectedIndex());
		try {
			teacherController.editHomeworkDescription(homework, taDescription.getText());
			teacherController.editHomeworkSubmissionDate(
					homeworks.get(homeworkTableView.getSelectionModel().getSelectedIndex()), datePicker.getValue());

		} catch (IllegalHomeworkAccessException e) {
			HandlerError.showError("Not your homework");
		} catch (HomeworkDaoException | DaoConnectionException e) {
			HandlerError.showError("check connection");
		}
	}

	public void itemSelected() {
		Homework homework = homeworkTableView.getSelectionModel().getSelectedItem();
		taDescription.setText(homework.getDescription());
		datePicker.setValue(homework.getSubmissionDate());
	}

	protected static void setTeachingsAssignement(TeachingAssignment teachingAssignment) {
		TeacherHomeworkManager.teachingAssignment = teachingAssignment;
	}

	public static void setController(TeacherController teacherController) {
		TeacherHomeworkManager.teacherController = teacherController;
	}
}