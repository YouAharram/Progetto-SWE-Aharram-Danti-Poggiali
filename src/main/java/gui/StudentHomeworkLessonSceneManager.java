package gui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import businessLogic.StudentController;
import domainModel.Homework;
import domainModel.Lesson;
import exceptions.AbsenceDaoException;
import exceptions.DaoConnectionException;
import exceptions.HomeworkDaoException;
import exceptions.LessonDaoException;
import exceptions.SchoolClassDaoException;
import exceptions.StudentDaoException;
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
import javafx.stage.Stage;

public class StudentHomeworkLessonSceneManager {

    @FXML
    private Button showButton;

    @FXML
    private Label attendanceLabel;
    
    @FXML
    private Button back;

    @FXML
    private DatePicker datePicker;

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
    private TableView<Homework> homeworkTableView;

    @FXML
    private TableColumn<Homework, String> subjectHomeworkColumn;

    @FXML
    private TableColumn<Homework, String> teacherHomeworkColumn;

    @FXML
    private TableColumn<Homework, String> descriptionHomeworkColumn;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private static StudentController studentController;

    public void switchToStudentScene() throws IOException {
        root = FXMLLoader.load(getClass().getResource("../StudentInterface.fxml"));
        stage = (Stage) back.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void showAnnotations() {
        if (datePicker.getValue() == null) {
            return;
        }
        
        LocalDate selectedDate = datePicker.getValue();

        try {
			String status = (studentController.checkStudentAttendanceInDay(selectedDate)) ? "Present" : "Absent";
			attendanceLabel.setText(status);
		} catch (AbsenceDaoException | DaoConnectionException | StudentDaoException e) {
			HandlerError.showError(e.getMessage());
		}
        
        Iterator<Lesson> lessonsIterator = null;
		try {
			lessonsIterator = studentController.getLessonInDate(selectedDate);
		} catch (DaoConnectionException | LessonDaoException |  SchoolClassDaoException e) {
			HandlerError.showError(e.getMessage());
		}
		
        List<Lesson> lessons = new ArrayList<>();
        lessonsIterator.forEachRemaining(lessons::add);
        ObservableList<Lesson> lessonsList = FXCollections.observableArrayList(lessons);
        lessonsTableView.setItems(lessonsList);

        subjectLessonColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeaching().getSubject()));
        teacherLessonColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeaching().getTeacher().getSurname() + " " + cellData.getValue().getTeaching().getTeacher().getName()));
        startHourColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartHour().toString()));
        endHourColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndHour().toString()));
        descriptionLessonColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        Iterator<Homework> homeworkIterator = null;
		try {
			homeworkIterator = studentController.getHomeworksBySubmissionDate(selectedDate);
		} catch (DaoConnectionException | HomeworkDaoException | SchoolClassDaoException e) {
			HandlerError.showError(e.getMessage());
		}
		
        List<Homework> homeworks = new ArrayList<>();
        homeworkIterator.forEachRemaining(homeworks::add);
        ObservableList<Homework> homeworkList = FXCollections.observableArrayList(homeworks);
        homeworkTableView.setItems(homeworkList);

        subjectHomeworkColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeaching().getSubject()));
        teacherHomeworkColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeaching().getTeacher().getSurname() + " " + cellData.getValue().getTeaching().getTeacher().getName()));
        descriptionHomeworkColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
    }

    public static void setController(StudentController studentController) {
        StudentHomeworkLessonSceneManager.studentController = studentController;
    }
}
