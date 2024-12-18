package gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import businessLogic.TeacherController;
import domainModel.SchoolClass;
import domainModel.Student;
import domainModel.TeachingAssignment;
import exceptions.DaoConnectionException;
import exceptions.SchoolClassDaoException;
import exceptions.StudentDaoException;
import exceptions.TeacherDaoException;
import exceptions.TeachingAssignmentDaoException;

public class InterfaceTeacherManager {

	static TeacherController teacherController;

	@FXML
	private Button btnLesson;

	@FXML
	private Button brtAbsence;

	@FXML
	private Label lblTeacherName;

	@FXML
	private Label lblTeacherSurname;

	@FXML
	private ChoiceBox<TeachingAssignment> cbTeachings;
	@FXML
	private TableView<ObservableList<String>> tvGradesStudents;
	private Stage stage;
	private Scene scene;
	private Parent root;

	private SchoolClass schoolClass;
	
	private TeachingAssignment teachingAssignment;

	@FXML
	public void initialize() {

		if (teacherController != null) {
			setControllerForAllScene();
			String name = teacherController.getTeacher().getName();
			String surname = teacherController.getTeacher().getSurname();
			lblTeacherName.setText(name);
			lblTeacherSurname.setText(surname);
		} else {
            HandlerError.showError("Parent controller not yet initialized");
        }

		setControllerForAllScene();
		populateTeachings();

	}

	public void showStudents() {
		setTeachingAssignment();
		tvGradesStudents.getItems().clear();
		tvGradesStudents.getColumns().clear();

		TableColumn<ObservableList<String>, String> column1 = new TableColumn<>("Number");
		TableColumn<ObservableList<String>, String> column2 = new TableColumn<>("Name");
		TableColumn<ObservableList<String>, String> column3 = new TableColumn<>("Surname");

		column1.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
		column2.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));
		column3.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(2)));

		tvGradesStudents.getColumns().add(column1);
		tvGradesStudents.getColumns().add(column2);
		tvGradesStudents.getColumns().add(column3);

		Iterator<Student> students = null;
		try {
			if (teachingAssignment != null) {
				students = teacherController.getStudentsByClass(teachingAssignment.getSchoolClass());
				ObservableList<ObservableList<String>> newRows = FXCollections.observableArrayList();
				int i = 1;

				while (students.hasNext()) {
					Student student = students.next();
					ObservableList<String> row = FXCollections.observableArrayList();
					row.add(String.valueOf(i));
					row.add(student.getName());
					row.add(student.getSurname());
					newRows.add(row);
					i++;
				}
				tvGradesStudents.setItems(newRows);
				
			}
			else {
				HandlerError.showError("Please select teaching and class");
			}
		} catch (StudentDaoException | DaoConnectionException | SchoolClassDaoException e) {
			HandlerError.showError(e.getMessage());
			return;
		}

	}

	public void populateTeachings() {
		if (teacherController != null) {
			Iterator<TeachingAssignment> teachings = null;
			try {
				teachings = teacherController.getAllMyTeachings();
			} catch (TeachingAssignmentDaoException | TeacherDaoException | DaoConnectionException e) {
				HandlerError.showError(e.getMessage());
			}
			while (teachings.hasNext()) {
				cbTeachings.getItems().add(teachings.next());
			}
			
			cbTeachings.setConverter(new StringConverter<TeachingAssignment>() {
				@Override
				public String toString(TeachingAssignment teaching) {
					return teaching != null ? teaching.getSubject() + " " + teaching.getSchoolClass().getClassName() : "";
				}

				@Override
				public TeachingAssignment fromString(String string) {
					return null;
				}
			});
		}
	}

	@FXML
	public void openLesson() throws IOException {
		setTeachingAssignment();
		if (teachingAssignment != null) {
			openWindow("../TeacherLessonScene.fxml", "Lesson");
			TeacherLessonManager
					.setTeachingsAssignement(cbTeachings.getValue());	
		}
		else {
			HandlerError.showError("Please select teaching and class");
		}

	}

	@FXML
	public void openHomework() throws IOException {
		setTeachingAssignment();
		if (teachingAssignment != null) {
			openWindow("../TeacherHomeworkScene.fxml", "Homework");
			TeacherHomeworkManager
					.setTeachingsAssignement(cbTeachings.getValue());
			
		}
		else {
			HandlerError.showError("Please select teaching and class");			
		}
	}

	@FXML
	public void openMeeting() throws IOException {
		setTeachingAssignment();
		openWindow("../TeacherMeetingScene.fxml", "Meeting");
	}

	@FXML
	public void openGrades() throws IOException {
		setTeachingAssignment();
		if (teachingAssignment != null) {
			TeacherGradeManager.setTeachingAssignment(cbTeachings.getValue());
			openWindow("../TeacherGradesScene.fxml", "Grades");
		} else {
			HandlerError.showError("Please select teaching and class");
		}

	}

	@FXML
	public void openAbsence() throws IOException {
		setTeachingAssignment();
		if (teachingAssignment != null) {
			TeacherAbsenceSceneManager.setSchoolClass(teachingAssignment.getSchoolClass());
			openWindow("../TeacherAbsenceScene.fxml", "Absences");
		} 
		else {
			HandlerError.showError("Please select teaching and class");
		}

	}

	@FXML
	public void openDisciplinaryReport() throws IOException {
		setTeachingAssignment();
		if (teachingAssignment != null) {
			TeacherDisciplinaryReportManager.setTeachingAssignment(cbTeachings.getValue());
			openWindow("../TeacherDisciplinaryReportScene.fxml", "Disciplinary report");

		} else {
			HandlerError.showError("Please select teaching and class");
		}
	}

	private void openWindow(String path, String nameWindow) throws IOException {
		root = FXMLLoader.load(getClass().getResource(path));
		stage = (Stage) btnLesson.getScene().getWindow();
		scene = new Scene(root);
		stage.setTitle(nameWindow);
		stage.setScene(scene);
		stage.show();
	}

	private void setControllerForAllScene() {
		TeacherLessonManager.setController(teacherController);
		TeacherMeetingManager.setTeacherController(teacherController);
		TeacherHomeworkManager.setController(teacherController);
		TeacherAbsenceSceneManager.setTeacherController(teacherController);
		TeacherGradeManager.setController(teacherController);
		TeacherDisciplinaryReportManager.setController(teacherController);
	}

	public static void setController(TeacherController teacherController) {
		InterfaceTeacherManager.teacherController = teacherController;
	}


	public void setTeachingAssignment() {
		this.teachingAssignment = cbTeachings.getValue();
	}
}