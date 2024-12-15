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
import exceptions.GradeDaoException;
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
	private ChoiceBox<String> cbTeachings;
	@FXML
	private ChoiceBox<String> cbClasses;
	@FXML
	private TableView<ObservableList<String>> tvGradesStudents;
	private Stage stage;
	private Scene scene;
	private Parent root;

	private SchoolClass schoolClass;

	@FXML
	public void initialize() throws StudentDaoException, DaoConnectionException, SchoolClassDaoException,
			GradeDaoException, TeachingAssignmentDaoException {

		if (teacherController != null) {
			setControllerForAllScene();
			String name = teacherController.getTeacher().getName();
			String surname = teacherController.getTeacher().getSurname();
			lblTeacherName.setText(name);
			lblTeacherSurname.setText(surname);
		}

		setControllerForAllScene();
		cbTeachings.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				handleChoiceBoxChange(newValue);
			}
		});
		populateTeachings();

	}

	public void showStudents() throws StudentDaoException, DaoConnectionException, SchoolClassDaoException {
		// Svuota i dati del modello
		tvGradesStudents.getItems().clear();
		// Rimuove tutte le colonne
		tvGradesStudents.getColumns().clear();

		// Crea le colonne
		TableColumn<ObservableList<String>, String> column1 = new TableColumn<>("Number");
		TableColumn<ObservableList<String>, String> column2 = new TableColumn<>("Name");
		TableColumn<ObservableList<String>, String> column3 = new TableColumn<>("Surname");

		// Imposta il cell value factory per ogni colonna
		column1.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0))); // Colonna "Number"
		column2.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1))); // Colonna "Name"
		column3.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(2))); // Colonna "Surname"

		// Aggiunge le colonne alla TableView
		tvGradesStudents.getColumns().add(column1);
		tvGradesStudents.getColumns().add(column2);
		tvGradesStudents.getColumns().add(column3);

		// Ottieni gli studenti dalla classe selezionata
		Iterator<Student> students = teacherController.getStudentsByClass(new SchoolClass(cbClasses.getValue()));
		ObservableList<ObservableList<String>> newRows = FXCollections.observableArrayList();
		int i = 1;

		// Popola i dati
		while (students.hasNext()) {
			Student student = students.next();
			ObservableList<String> row = FXCollections.observableArrayList();
			row.add(String.valueOf(i)); // Numero
			row.add(student.getName()); // Nome
			row.add(student.getSurname()); // Cognome
			newRows.add(row);
			i++;
		}

		// Imposta i dati nella TableView
		tvGradesStudents.setItems(newRows);
	}

	private void handleChoiceBoxChange(String newValue) {
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
				cbClasses.getItems().add(ta.getSchoolClass().getClassName());
			}
		}
	}

	@FXML
	public void openLesson() throws IOException {
		openWindow("../resources/TeacherLessonInterface.fxml", "Lesson");
		TeacherLessonManager
				.setTeachingsAssignement(new TeachingAssignment(cbTeachings.getSelectionModel().getSelectedIndex() + 1,
						cbTeachings.getValue(), teacherController.getTeacher(), new SchoolClass(cbClasses.getValue())));
	}

	@FXML
	public void openHomework() throws IOException {
		openWindow("../resources/TeacherHomeworkInterface.fxml", "Homework");
		TeacherHomeworkManager
				.setTeachingsAssignement(new TeachingAssignment(cbTeachings.getSelectionModel().getSelectedIndex() + 1,
						cbTeachings.getValue(), teacherController.getTeacher(), new SchoolClass(cbClasses.getValue())));
	}

	@FXML
	public void openMeeting() throws IOException {
		openWindow("../resources/TeacherMeetingScene.fxml", "Meeting");
	}

	@FXML
	public void openGrades() throws IOException {
		TeacherGradeManager
				.setTeachingAssignment(new TeachingAssignment(cbTeachings.getSelectionModel().getSelectedIndex() + 1,
						cbTeachings.getValue(), teacherController.getTeacher(), new SchoolClass(cbClasses.getValue())));
		openWindow("../resources/TeacherGradesInterface.fxml", "Grades");
	}

	@FXML
	public void openAbsence() throws IOException {
		if (cbClasses.getValue() != null && !cbClasses.getValue().isEmpty()) {
			schoolClass = new SchoolClass(cbClasses.getValue());

			TeacherAbsenceSceneManager.setSchoolClass(schoolClass);
			TeacherAbsenceSceneManager.setTeacherController(teacherController); // Se non lo hai già fatto in un altro
																				// punto

			openWindow("../resources/TeacherAbsenceScene.fxml", "Absences");
		}
	}

	@FXML
	public void openDisciplinaryReport() throws IOException {
		TeacherDisciplinaryReportManager
				.setTeachingAssignment(new TeachingAssignment(cbTeachings.getSelectionModel().getSelectedIndex() + 1,
						cbTeachings.getValue(), teacherController.getTeacher(), new SchoolClass(cbClasses.getValue())));
		openWindow("../resources/TeacherDisciplinaryReportInterface.fxml", "Disciplinary report");
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
		TeacherGradeManager.setController(teacherController);
		TeacherDisciplinaryReportManager.setController(teacherController);
	}

	public static void setController(TeacherController teacherController) {
		InterfaceTeacherManager.teacherController = teacherController;
	}
}