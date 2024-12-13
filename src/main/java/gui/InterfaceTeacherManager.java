package gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.GradeDaoException;
import DaoExceptions.SchoolClassDaoException;
import DaoExceptions.StudentDaoException;
import DaoExceptions.TeacherDaoException;
import DaoExceptions.TeachingAssignmentDaoException;
import businessLogic.TeacherController;
import domainModel.Grade;
import domainModel.SchoolClass;
import domainModel.Student;
import domainModel.TeachingAssignment;

public class InterfaceTeacherManager {

	private static TeacherController teacherController;

	@FXML
	private Button btnLesson;
	@FXML
	private Label lblTeacherName;
	@FXML
	private ChoiceBox<String> cbTeachings;
	@FXML
	private ChoiceBox<String> cbClasses;
	@FXML
	private TableView<String> tvGradesStudents = new TableView<>();
	private Stage stage;
	private Scene scene;
	private Parent root;
	private int idTeaching;

	@FXML
	public void initialize() {
		cbTeachings.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				handleChoiceBoxChange(newValue);
			}
		});
		populateTeachings();
		ConfigureColumn();

	}

	private void handleChoiceBoxChange(String newValue) {
		System.out.println("suuu " + newValue);
		populateClass();
	}

	public void populateTeachings() {
		if (teacherController != null) {
			Iterator<TeachingAssignment> teachings = null;
			try {
				teachings = teacherController.getAllMyTeachings();
			} catch (TeachingAssignmentDaoException | TeacherDaoException | DaoConnectionException e) {
				e.printStackTrace();
			}
			while (teachings.hasNext()) {
				cbTeachings.getItems().add(teachings.next().getSubject());
			}
		}
	}

	private void populateClass() {
		if (teacherController != null) {
			List<TeachingAssignment> classes = null;
			try {
				Iterator<TeachingAssignment> teachings = null;
				String subject = cbTeachings.getValue();
				teachings = teacherController.getAllMyTeachings();
				classes = StreamSupport
						.stream(Spliterators.spliteratorUnknownSize(teachings, Spliterator.ORDERED), false)
						.filter(t -> t.getSubject().equals(subject)).collect(Collectors.toList());
			} catch (TeachingAssignmentDaoException | TeacherDaoException | DaoConnectionException e) {
				e.printStackTrace();
			}
			for (TeachingAssignment ta : classes) {
				// Fai qualcosa con teaching
				System.out.println(ta.getSchoolClass().getClassName());
				cbClasses.getItems().add(ta.getSchoolClass().getClassName());
			}
		}
	}
	
	@FXML
	public void showGrades() throws StudentDaoException, DaoConnectionException, SchoolClassDaoException, GradeDaoException, TeachingAssignmentDaoException {
		Iterator<Student> students = teacherController.getStudentsByClass(new SchoolClass(cbClasses.getValue()));
		while(students.hasNext()) {
			teacherController.getAllStudentGradesByTeaching(students.next(), new TeachingAssignment(cbTeachings.getSelectionModel().getSelectedIndex(), cbTeachings.getValue(), teacherController.getTeacher(), new SchoolClass(cbClasses.getValue())));
		}
	}
	
	private void ConfigureColumn() {
		int size = 15;
		TableColumn<String, String> student = new TableColumn<String, String>("Student");
		tvGradesStudents.getColumns().add(student);
		for(int i=0; i < size ; i++) {
			String columnName = "Grade "+(i+1)+"°";
			TableColumn<String, String> column = new TableColumn<String, String>(columnName);
			//column.setCellValueFactory(cellData -> cellData.getValue().getda);
			tvGradesStudents.getColumns().add(column);
		}
		tvGradesStudents.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		ScrollPane scrollPane = new ScrollPane(tvGradesStudents);
        scrollPane.setFitToWidth(false); // Permette lo scorrimento orizzontale
        scrollPane.setFitToHeight(true);

	}

	@FXML
	public void openLesson() throws IOException {
		openWindow("../resources/LessonInterface.fxml", "Lesson");
	}

	@FXML
	public void openHomework() throws IOException {
		openWindow("../resources/HomeworkInterface.fxml", "Homework");
	}

	@FXML
	public void openMeeting() throws IOException {
		openWindow("../resources/MeetingInterface.fxml", "Meeting");
	}

	private void openWindow(String path, String nameWindow) throws IOException {
		root = FXMLLoader.load(getClass().getResource(path));
		stage = (Stage) btnLesson.getScene().getWindow();
		scene = new Scene(root);
		stage.setTitle(nameWindow);
		stage.setScene(scene);
		stage.show();
	}

	public static void setController(TeacherController teacherController) {
		InterfaceTeacherManager.teacherController = teacherController;
	}
}