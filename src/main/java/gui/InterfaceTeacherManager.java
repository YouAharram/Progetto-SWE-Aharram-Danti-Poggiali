package gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.util.ArrayList;
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
	ObservableList<ObservableList<String>> rows = FXCollections.observableArrayList();
	private Stage stage;
	private Scene scene;
	private Parent root;
	private int idTeaching;
	private int numberOfStudents = 0;

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
	public void showGrades() throws StudentDaoException, DaoConnectionException, SchoolClassDaoException,
			GradeDaoException, TeachingAssignmentDaoException {
		ConfigureColumnForGrade();
		Iterator<Student> students = teacherController.getStudentsByClass(new SchoolClass(cbClasses.getValue()));

		int rowindex = 0;
		while (students.hasNext()) {
			int c = 1;// Ad esempio, 5 studenti
			Student student = students.next();
			System.out.println(student.getName());
			ObservableList<String> row = rows.get(rowindex);
			rowindex++;
			row.set(0, String.valueOf(student.getName() + " " + student.getSurname()));
			Iterator<Grade> gradesPerStudent = teacherController.getAllStudentGradesByTeaching(student,
					new TeachingAssignment(cbTeachings.getSelectionModel().getSelectedIndex() + 1,
							cbTeachings.getValue(), teacherController.getTeacher(),
							schoolClass));
			while (gradesPerStudent.hasNext()) { // Ad esempio, 10 voti per studente
				row.set(c, String.valueOf(gradesPerStudent.next().getValue()));
				c++;
			}
		}
	}

	private void ConfigureColumnForGrade() throws StudentDaoException, DaoConnectionException, SchoolClassDaoException,
			GradeDaoException, TeachingAssignmentDaoException {
		// Svuota i dati del modello
		tvGradesStudents.getItems().clear();
		// Rimuove tutte le colonne
		tvGradesStudents.getColumns().clear();

		int size = getMaxNumberOfGrades() + 1; // 10 colonne (per esempio, 10 voti)

		for (int i = 0; i < size; i++) {
			TableColumn<ObservableList<String>, String> column;
			if (i == 0) {
				column = new TableColumn<>("Student: ");
			} else {
				column = new TableColumn<>("Grade " + (i));
			}

			final int colIndex = i; // Indice di colonna

			// Imposta la cella come elemento della lista corrispondente all'indice
			column.setCellValueFactory(
					cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().get(colIndex)));

			// Rendi la cella modificabile
			column.setCellFactory(TextFieldTableCell.forTableColumn());
			column.setOnEditCommit(event -> event.getRowValue().set(colIndex, event.getNewValue()));
			tvGradesStudents.getColumns().add(column);
		}
		System.out.println(numberOfStudents);
		for (int r = 0; r < numberOfStudents; r++) {
			ObservableList<String> row = FXCollections.observableArrayList();
			for (int c = 0; c < size; c++) {
				row.add("");
			}
			rows.add(row);
		}
		tvGradesStudents.setItems(rows);
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
		column1.setCellValueFactory(data -> 
		    new SimpleStringProperty(data.getValue().get(0))); // Colonna "Number"
		column2.setCellValueFactory(data -> 
		    new SimpleStringProperty(data.getValue().get(1))); // Colonna "Name"
		column3.setCellValueFactory(data -> 
		    new SimpleStringProperty(data.getValue().get(2))); // Colonna "Surname"

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
		    row.add(String.valueOf(i));  // Numero
		    row.add(student.getName()); // Nome
		    row.add(student.getSurname()); // Cognome
		    newRows.add(row);
		    i++;
		}

		// Imposta i dati nella TableView
		tvGradesStudents.setItems(newRows);
	}

	private int getMaxNumberOfGrades() throws StudentDaoException, DaoConnectionException, SchoolClassDaoException,
			GradeDaoException, TeachingAssignmentDaoException {
		Iterator<Student> students = teacherController.getStudentsByClass(new SchoolClass(cbClasses.getValue()));
		int maxNumberOfGrades = 0;
		while (students.hasNext()) {
			numberOfStudents++;
			Iterator<Grade> gradesPerStudent = teacherController.getAllStudentGradesByTeaching(students.next(),
					new TeachingAssignment(cbTeachings.getSelectionModel().getSelectedIndex() + 1,
							cbTeachings.getValue(), teacherController.getTeacher(),
							new SchoolClass(cbClasses.getValue())));
			List<Grade> grades = new ArrayList<Grade>();
			gradesPerStudent.forEachRemaining(grades::add);
			if (grades.size() > maxNumberOfGrades) {
				maxNumberOfGrades = grades.size();
			}
		}
		System.out.println("max: " + maxNumberOfGrades);
		return maxNumberOfGrades;
	}

	@FXML
	public void openLesson() throws IOException {
		openWindow("../resources/LessonInterface.fxml", "Lesson");
		LessonTeacherManager.setTeachingsAssignement(new TeachingAssignment(cbTeachings.getSelectionModel().getSelectedIndex()+1, cbTeachings.getValue(), teacherController.getTeacher(), new SchoolClass(cbClasses.getValue())));
	}
	@FXML
	public void openHomework() throws IOException {
		openWindow("../resources/HomeworkInterface.fxml", "Homework");
	}

	@FXML
	public void openMeeting() throws IOException {
		openWindow("../resources/MeetingSceneTeacher.fxml", "Meeting");
	}
	
	@FXML
	public void openGrades() throws IOException {
		openWindow("../resources/GradesTeacherInterface.fxml","Grades");
	}

	@FXML
	public void openAbsence() throws IOException {
	    if (cbClasses.getValue() != null && !cbClasses.getValue().isEmpty()) {
	        schoolClass = new SchoolClass(cbClasses.getValue());

	        AbsenceTeacherSceneManager.setSchoolClass(schoolClass);
	        AbsenceTeacherSceneManager.setTeacherController(teacherController); // Se non lo hai già fatto in un altro punto

	        openWindow("../resources/AbsenceSceneTeacher.fxml", "Absences");
	    } else {
	        System.out.println("Per favore, seleziona una classe prima di aprire la scheda delle assenze.");
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
		LessonTeacherManager.setController(teacherController);
	}
	
	
	public static void setController(TeacherController teacherController) {
		InterfaceTeacherManager.teacherController = teacherController;
	}
}