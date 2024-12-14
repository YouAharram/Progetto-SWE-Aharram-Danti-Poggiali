package gui;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.HomeworkDaoException;
import DaoExceptions.LessonDaoException;
import DaoExceptions.SchoolClassDaoException;
import businessLogic.TeacherController;
import businessLogic.TeacherController.IllegalLessonAccessException;
import domainModel.Homework;
import domainModel.Lesson;
import domainModel.SchoolClass;
import domainModel.TeachingAssignment;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class LessonTeacherManager {

	@FXML
	private Button btnBack;
	@FXML
	private DatePicker datePicker;

	private Stage stage;
	private Scene scene;
	private Parent root;
	List<Lesson> lessons = new ArrayList<>();
	private static TeacherController teacherController;
	
	@FXML
	private TextArea taDescription;

	@FXML
	private TableView<Lesson> lessonsTableView;

	@FXML
	private TableColumn<Lesson, String> subjectLessonColumn;

	@FXML
	private TableColumn<Lesson, String> teacherLessonColumn;

	@FXML
	private TableColumn<Lesson, String> startHourColumn;

	@FXML
	private TableColumn<Lesson, String> endHourColumn;

	@FXML
	private TableColumn<Lesson, String> descriptionLessonColumn;
	
	@FXML
	private ComboBox<String> cbSHours;
	
	@FXML
	private ComboBox<String> cbSMinutes;
	@FXML
	private ComboBox<String> cbFHours;
	
	@FXML
	private ComboBox<String> cbFMinutes;
	
	private static TeachingAssignment teachingAssignment;

	public void backAtTeacherScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../resources/TeacherInterface.fxml"));
		stage = (Stage) btnBack.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	public void initialize() {
	    for (int i = 0; i < 24; i++) {
	        cbSHours.getItems().add(String.format("%02d", i));
	        cbFHours.getItems().add(String.format("%02d", i));
	    }
	    // Popola il ComboBox per i minuti
	    for (int i = 0; i < 60; i++) {
	        cbSMinutes.getItems().add(String.format("%02d", i));
	        cbFMinutes.getItems().add(String.format("%02d", i));
	    }
	}
	
	
	@FXML
	public void showLesson() throws DaoConnectionException, LessonDaoException, SchoolClassDaoException {
		if (datePicker.getValue() == null) {
			return;
		}

		LocalDate selectedDate = datePicker.getValue();
		Iterator<Lesson> lessonsIterator = null;
		try {
			lessonsIterator = teacherController.getClassLessonsInDay(selectedDate, teachingAssignment.getSchoolClass());
		} catch (DaoConnectionException | LessonDaoException | SchoolClassDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		lessonsIterator.forEachRemaining(lessons::add);
		ObservableList<Lesson> lessonsList = FXCollections.observableArrayList(lessons);
		lessonsTableView.setItems(lessonsList);

		subjectLessonColumn.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getTeaching().getSubject()));
		teacherLessonColumn.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getTeaching().getTeacher().getSurname() + " "
						+ cellData.getValue().getTeaching().getTeacher().getName()));
		startHourColumn.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getStartHour().toString()));
		endHourColumn
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndHour().toString()));
		descriptionLessonColumn
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
	
//		lessonsTableView.getItems().clear();

	}

	public static void setController(TeacherController teacherController) {
		LessonTeacherManager.teacherController = teacherController;
	}

	public void addLesson() throws LessonDaoException, DaoConnectionException {
		teacherController.addNewLesson(teachingAssignment, datePicker.getValue(), taDescription.getText(), LocalTime.of(Integer.valueOf(cbSHours.getValue()),Integer.valueOf(cbSMinutes.getValue())) , LocalTime.of(Integer.valueOf(cbFHours.getValue()),Integer.valueOf(cbFMinutes.getValue())));
	}
	
	public void deleteLesson() throws LessonDaoException, IllegalLessonAccessException, DaoConnectionException {
		System.out.println(lessonsTableView.getSelectionModel().getSelectedIndex());
		teacherController.deleteLesson(lessons.get(lessonsTableView.getSelectionModel().getSelectedIndex()));
//		lessonsTableView.getSelectionModel().clearSelection();
	}
	
	public void editLesson() throws LessonDaoException, DaoConnectionException, IllegalLessonAccessException {
		teacherController.editLessonDateTime(lessons.get(lessonsTableView.getSelectionModel().getSelectedIndex()),datePicker.getValue(), LocalTime.of(Integer.valueOf(cbSHours.getValue()),Integer.valueOf(cbFMinutes.getValue())),LocalTime.of(Integer.valueOf(cbFHours.getValue()),Integer.valueOf(cbSMinutes.getValue())) );
		teacherController.editLessonDescription(lessons.get(lessonsTableView.getSelectionModel().getSelectedIndex()), taDescription.getText());
	}

	protected static void setTeachingsAssignement(TeachingAssignment teachingAssignment) {
		LessonTeacherManager.teachingAssignment = teachingAssignment;
	}
}
