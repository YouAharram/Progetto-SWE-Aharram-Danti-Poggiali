package gui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.HomeworkDaoException;
import DaoExceptions.LessonDaoException;
import DaoExceptions.SchoolClassDaoException;
import businessLogic.TeacherController;
import domainModel.Homework;
import domainModel.Lesson;
import domainModel.SchoolClass;
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
import javafx.stage.Stage;

public class LessonTeacherManager {
	
	
	@FXML
	private Button btnBack;
	@FXML
	private DatePicker datePicker;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private static TeacherController teacherController;
	private static SchoolClass schoolClass;
	
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
	
	public void backAtTeacherScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../resources/TeacherInterface.fxml"));
		stage = (Stage) btnBack.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void showLesson() throws DaoConnectionException, LessonDaoException, SchoolClassDaoException {
        System.out.println("dentro");
		if (datePicker.getValue() == null) {
            return;
        }

        LocalDate selectedDate = datePicker.getValue();

        Iterator<Lesson> lessonsIterator = null;
		try {
			lessonsIterator = teacherController.getClassLessonsInDay(selectedDate,schoolClass);
		} catch (DaoConnectionException | LessonDaoException |  SchoolClassDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	}
	
	public static void setController(TeacherController teacherController) {
		LessonTeacherManager.teacherController = teacherController;
	}
	
	protected static void setClass(SchoolClass schoolClass) {
		LessonTeacherManager.schoolClass = schoolClass;
		
	}
}
